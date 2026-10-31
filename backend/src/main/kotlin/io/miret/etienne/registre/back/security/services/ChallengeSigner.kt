package io.miret.etienne.registre.back.security.services

import io.miret.etienne.registre.back.parameters.model.Parameter
import io.miret.etienne.registre.back.parameters.repositories.ParameterRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Issues and verifies self-contained WebAuthn challenges, so that the server
 * does not have to store a challenge until it is used.
 *
 * A challenge is made of random bytes and an expiry date, followed by an
 * HMAC-SHA256 of both and of the challenge's purpose. The purpose is not part
 * of the challenge: the verifier must provide the same one, so that a
 * challenge issued for one purpose (for example, registering a passkey for a
 * given user) is rejected for any other.
 *
 * The HMAC key is generated once and stored in the `parameters` collection,
 * so that it survives restarts and is shared between backend instances.
 *
 * This class does not prevent a challenge from being used twice: callers must
 * record the challenges they accept.
 */
@Service
class ChallengeSigner(
  private val parameterRepository: ParameterRepository,
) {

  private val random = SecureRandom()
  private val keyMutex = Mutex()
  @Volatile private var key: SecretKeySpec? = null

  /**
   * Returns a new challenge for [purpose], valid until [expiresAt].
   */
  suspend fun sign(purpose: String, expiresAt: Instant): ByteArray {
    val payload = ByteBuffer.allocate(PAYLOAD_SIZE)
      .put(ByteArray(RANDOM_SIZE).also(random::nextBytes))
      .putLong(expiresAt.epochSecond)
      .array()
    return payload + mac(payload, purpose)
  }

  /**
   * Checks the signature of [challenge].
   *
   * @return The expiry date of [challenge], or `null` if it was not issued by
   *   [sign] for [purpose]. The caller must check that the expiry date is not
   *   past.
   */
  suspend fun verify(purpose: String, challenge: ByteArray): Instant? {
    if (challenge.size != PAYLOAD_SIZE + MAC_SIZE) {
      return null
    }
    val payload = challenge.copyOfRange(0, PAYLOAD_SIZE)
    val signature = challenge.copyOfRange(PAYLOAD_SIZE, challenge.size)
    if (!MessageDigest.isEqual(signature, mac(payload, purpose))) {
      return null
    }
    return Instant.ofEpochSecond(ByteBuffer.wrap(payload, RANDOM_SIZE, Long.SIZE_BYTES).long)
  }

  // The payload has a fixed size, so appending the purpose is unambiguous.
  private suspend fun mac(payload: ByteArray, purpose: String): ByteArray =
    Mac.getInstance(ALGORITHM)
      .apply {
        init(key())
        update(payload)
      }
      .doFinal(purpose.toByteArray(Charsets.UTF_8))

  /**
   * Returns the HMAC key, loading it from the database (or creating it) on
   * first use.
   */
  private suspend fun key(): SecretKeySpec =
    key ?: keyMutex.withLock {
      key ?: SecretKeySpec(loadOrCreateKey(), ALGORITHM).also { key = it }
    }

  private suspend fun loadOrCreateKey(): ByteArray {
    parameterRepository.findById(KEY_PARAMETER)?.let {
      return Base64.getDecoder().decode(it.value)
    }
    val newKey = ByteArray(KEY_SIZE).also(random::nextBytes)
    return try {
      parameterRepository.insert(
        Parameter(KEY_PARAMETER, Base64.getEncoder().encodeToString(newKey))
      )
      newKey
    } catch (_: DuplicateKeyException) {
      // Another instance created the key first: use theirs.
      Base64.getDecoder().decode(parameterRepository.findById(KEY_PARAMETER)!!.value)
    }
  }

  companion object {
    const val KEY_PARAMETER = "passkeyChallengeKey"
    private const val ALGORITHM = "HmacSHA256"
    private const val KEY_SIZE = 32
    private const val RANDOM_SIZE = 16
    private const val PAYLOAD_SIZE = RANDOM_SIZE + Long.SIZE_BYTES
    private const val MAC_SIZE = 32
  }

}
