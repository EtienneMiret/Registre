package io.miret.etienne.registre.back.security.services

import com.webauthn4j.async.WebAuthnAsyncManager
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.credential.CredentialRecordImpl
import com.webauthn4j.data.*
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.util.Base64UrlUtil
import io.miret.etienne.registre.back.config.RegistreConfig
import io.miret.etienne.registre.back.security.exceptions.InvalidChallengeException
import io.miret.etienne.registre.back.security.exceptions.InvalidClientDataException
import io.miret.etienne.registre.back.security.exceptions.InvalidPasskeyException
import io.miret.etienne.registre.back.security.model.Passkey
import io.miret.etienne.registre.back.security.model.UsedPasskeyChallenge
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.PasskeyRepository
import io.miret.etienne.registre.back.security.repositories.UsedPasskeyChallengeRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.common.passkey.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import tools.jackson.core.JacksonException
import java.time.Clock
import java.time.Duration
import java.time.Instant
import io.miret.etienne.registre.common.User as ApiUser

/**
 * Handles the WebAuthn passkey registration and authentication ceremonies.
 *
 * Each ceremony is a two-step process:
 * 1. **Options** — the server generates a challenge and returns options for
 *    the browser to pass to the WebAuthn API.
 * 2. **Completion** — the browser submits a signed response; the server
 *    validates the challenge, then verifies the cryptographic response via
 *    [WebAuthnAsyncManager].
 *
 * Challenges are signed by [ChallengeSigner] rather than stored, so that
 * requesting one (anonymously, for authentication) does not write to the
 * database. A challenge is only recorded once the ceremony succeeds, so that
 * it cannot be reused.
 */
@Service
class PasskeyService(
  private val asyncManager: WebAuthnAsyncManager,
  private val objectConverter: ObjectConverter,
  private val usedChallengeRepository: UsedPasskeyChallengeRepository,
  private val challengeSigner: ChallengeSigner,
  private val passkeyRepository: PasskeyRepository,
  private val userRepository: UserRepository,
  private val clock: Clock,
  private val config: RegistreConfig,
) {

  /** How long a challenge remains valid after it is issued. */
  private val challengeExpiry = Duration.ofMinutes(5)

  /** How long a used challenge is kept after it expires. */
  private val usedChallengeRetention = Duration.ofMinutes(5)

  /**
   * Supported public key algorithms in preference order, used when verifying
   * registration responses.
   */
  private val pubKeyCredParams = listOf(
    PublicKeyCredentialParameters(
      PublicKeyCredentialType.PUBLIC_KEY,
      COSEAlgorithmIdentifier.ES256),
    PublicKeyCredentialParameters(
      PublicKeyCredentialType.PUBLIC_KEY,
      COSEAlgorithmIdentifier.RS256),
  )

  /**
   * Builds a [ServerProperty] for challenge verification, binding the
   * configured origin, RP ID, and [challengeBase64] decoded to bytes.
   */
  private fun serverProperty(challengeBase64: String): ServerProperty =
    ServerProperty.builder()
      .origins(setOf(Origin.create(config.webauthn.origin)))
      .rpId(config.webauthn.rpId)
      .challenge(DefaultChallenge(Base64UrlUtil.decode(challengeBase64)))
      .build()

  /**
   * Generates a signed registration challenge for [user], without writing to
   * the database.
   *
   * The options exclude the user's existing passkeys, so that the browser
   * refuses to register a second passkey on the same authenticator (which
   * would otherwise replace the first one).
   *
   * @return [RegistrationOptions] to be forwarded to the browser as
   *   the argument to `navigator.credentials.create()`.
   */
  suspend fun generateRegistrationChallenge(user: User): RegistrationOptions {
    return RegistrationOptions(
      challenge = issueChallenge(registrationPurpose(user)),
      rp = RpInfo(config.webauthn.rpId, config.webauthn.rpName),
      user = ApiUser(
        id = Base64UrlUtil.encodeToString(user.id.toByteArray()),
        name = user.name,
      ),
      pubKeyCredParams = listOf(
        PubKeyCredParam("public-key", COSEAlgorithmIdentifier.ES256.value),
        PubKeyCredParam("public-key", COSEAlgorithmIdentifier.RS256.value),
      ),
      timeout = 60_000,
      attestation = "none",
      authenticatorSelection = AuthenticatorSelection(
        residentKey = "required",
        requireResidentKey = true,
        userVerification = UserVerification.REQUIRED,
      ),
      excludeCredentials = passkeyRepository.findAllByUserId(user.id)
        .map { CredentialDescriptor("public-key", it.id, emptySet()) }
        .toList(),
    )
  }

  /**
   * Verifies the browser's registration response and persists the new passkey
   * credential.
   *
   * Checks the challenge's signature and expiry, then delegates cryptographic
   * verification to [WebAuthnAsyncManager]. On success, records the challenge
   * as used.
   * Throws [InvalidChallengeException] if the challenge is invalid (including
   * when it was issued to another user or for authentication), expired, or
   * already used.
   *
   * @param user The authenticated user completing the registration.
   * @param request The credential submitted by the browser.
   */
  suspend fun completeRegistration(user: User, request: RegistrationDtoRequest) {
    val clientDataJson = decode("clientDataJSON", request.response.clientDataJSON)
    val attestationObject = decode("attestationObject", request.response.attestationObject)

    val challengeBase64 = extractChallenge(clientDataJson)
    val expiresAt = checkChallenge(challengeBase64, registrationPurpose(user))

    val registrationRequest = RegistrationRequest(
      attestationObject,
      clientDataJson,
      null as Set<String>?,
    )
    val registrationParameters = RegistrationParameters(
      serverProperty(challengeBase64),
      pubKeyCredParams,
      true,
      true,
    )
    val result = asyncManager.verify(registrationRequest, registrationParameters)
      .await()

    recordChallenge(challengeBase64, expiresAt)

    val authenticatorData = result.attestationObject!!.authenticatorData
    val attestedCredentialData = authenticatorData.attestedCredentialData!!

    passkeyRepository.insert(
      Passkey(
        id = Base64UrlUtil.encodeToString(attestedCredentialData.credentialId!!),
        userId = user.id,
        name = request.name,
        attestedCredentialDataCbor = objectConverter.cborMapper
          .writeValueAsBytes(attestedCredentialData),
        signCount = authenticatorData.signCount,
        backupEligible = authenticatorData.isFlagBE,
        createdAt = Instant.now(clock),
        lastUsedAt = null,
      )
    )
  }

  /**
   * Generates a signed authentication challenge, without writing to the
   * database.
   *
   * The challenge is not bound to a specific user because the user
   * identity is not known until the browser submits its response.
   *
   * @return [AuthenticationOptions] to be forwarded to the browser as
   *   the argument to `navigator.credentials.get()`.
   */
  suspend fun generateAuthenticationChallenge(): AuthenticationOptions {
    return AuthenticationOptions(
      challenge = issueChallenge(AUTHENTICATION_PURPOSE),
      rpId = config.webauthn.rpId,
      allowCredentials = emptyList(),
      userVerification = UserVerification.REQUIRED,
      timeout = 60_000,
    )
  }

  /**
   * Verifies the browser's authentication response and returns the
   * authenticated user.
   *
   * Looks up the stored credential by ID, checks the challenge's signature
   * and expiry, then delegates cryptographic verification to
   * [WebAuthnAsyncManager]. On success, records the challenge as used and
   * updates the stored sign count.
   *
   * @param request The credential assertion submitted by the browser.
   * @return The authenticated [User], or `null` if the credential ID
   *   is not found.
   * @throws InvalidPasskeyException if the response's user handle does not
   *   match the owner of the credential.
   */
  suspend fun completeAuthentication(request: AuthenticationDtoRequest): User? {
    val storedCredential = passkeyRepository.findById(request.id)
      ?: return null

    // Decode everything before checking the challenge, so that a malformed
    // request is rejected early.
    val credentialId = decode("id", request.id)
    val clientDataJson = decode("clientDataJSON", request.response.clientDataJSON)
    val authenticatorData = decode("authenticatorData", request.response.authenticatorData)
    val signature = decode("signature", request.response.signature)
    val userHandle = request.response.userHandle?.let { decode("userHandle", it) }

    // WebAuthn §7.2: when present, the user handle must name the owner of the
    // credential. Registration sets it to the bytes of the user ID.
    if (userHandle != null && !userHandle.contentEquals(storedCredential.userId.toByteArray())) {
      throw InvalidPasskeyException("User handle does not match passkey owner")
    }

    val challengeBase64 = extractChallenge(clientDataJson)
    val expiresAt = checkChallenge(challengeBase64, AUTHENTICATION_PURPOSE)

    val attestedCredentialData = objectConverter.cborMapper
      .readValue(storedCredential.attestedCredentialDataCbor, AttestedCredentialData::class.java)!!

    val credentialRecord = CredentialRecordImpl(
      null,
      null,
      storedCredential.backupEligible,
      null,
      storedCredential.signCount,
      attestedCredentialData,
      null,
      null,
      null,
      null,
    )

    val authenticationRequest = AuthenticationRequest(
      credentialId,
      userHandle,
      authenticatorData,
      clientDataJson,
      signature,
    )
    val authenticationParameters = AuthenticationParameters(
      serverProperty(challengeBase64),
      credentialRecord,
      null,
      true,
      true,
    )

    val result = asyncManager.verify(authenticationRequest, authenticationParameters).await()

    recordChallenge(challengeBase64, expiresAt)

    passkeyRepository.save(
      storedCredential.copy(
        signCount = result.authenticatorData!!.signCount,
        lastUsedAt = Instant.now(clock),
      )
    )

    return userRepository.findById(storedCredential.userId)
  }

  /**
   * Decodes the base64url [value] of the request field named [field].
   *
   * @throws InvalidClientDataException if [value] is not valid base64url.
   */
  private fun decode(field: String, value: String): ByteArray =
    try {
      Base64UrlUtil.decode(value)
    } catch (e: IllegalArgumentException) {
      throw InvalidClientDataException("Invalid base64 in $field", e)
    }

  /**
   * Extracts the base64url-encoded challenge from [clientDataJson].
   *
   * @throws InvalidClientDataException if [clientDataJson] is not a JSON
   *   object with a string `challenge` field.
   */
  private fun extractChallenge(clientDataJson: ByteArray): String {
    val clientData = try {
      val jsonString = String(clientDataJson, Charsets.UTF_8)
      objectConverter.jsonMapper.readValue(jsonString, Map::class.java)
    } catch (e: JacksonException) {
      throw InvalidClientDataException("Invalid JSON in clientDataJSON", e)
    }
    return clientData?.get("challenge") as? String
      ?: throw InvalidClientDataException("Missing challenge in clientDataJSON")
  }

  /**
   * Returns a new base64url-encoded challenge for [purpose].
   */
  private suspend fun issueChallenge(purpose: String): String =
    Base64UrlUtil.encodeToString(
      challengeSigner.sign(purpose, Instant.now(clock).plus(challengeExpiry))
    )

  /**
   * Checks that [challengeBase64] was issued for [purpose] and is not expired.
   *
   * @return The expiry date of the challenge.
   * @throws InvalidClientDataException if [challengeBase64] is not valid
   *   base64url.
   * @throws InvalidChallengeException if the challenge was not issued for
   *   [purpose], or is expired.
   */
  private suspend fun checkChallenge(challengeBase64: String, purpose: String): Instant {
    val expiresAt = challengeSigner.verify(purpose, decode("challenge", challengeBase64))
      ?: throw InvalidChallengeException("Invalid challenge")
    if (expiresAt < Instant.now(clock)) {
      throw InvalidChallengeException("Challenge expired")
    }
    return expiresAt
  }

  /**
   * Records [challengeBase64] as used, until a while after it expires.
   *
   * Must only be called once the browser's response is verified, so that
   * anonymous requests cannot fill the database. Inserting fails if the
   * challenge was already used, even by a concurrent request.
   *
   * @throws InvalidChallengeException if the challenge was already used, or
   *   expired before it was recorded.
   */
  private suspend fun recordChallenge(challengeBase64: String, expiresAt: Instant) {
    try {
      usedChallengeRepository.insert(
        UsedPasskeyChallenge(challengeBase64, expiresAt.plus(usedChallengeRetention))
      )
    } catch (e: DuplicateKeyException) {
      throw InvalidChallengeException("Challenge already used", e)
    }
    // The record of a previous use may have been deleted after the challenge
    // expired. So the insert above only proves that the challenge was not
    // used if it happened before the expiry.
    if (expiresAt <= Instant.now(clock)) {
      throw InvalidChallengeException("Challenge expired")
    }
  }

  private fun registrationPurpose(user: User) = "registration:${user.id}"

  companion object {
    private const val AUTHENTICATION_PURPOSE = "authentication"
  }

}
