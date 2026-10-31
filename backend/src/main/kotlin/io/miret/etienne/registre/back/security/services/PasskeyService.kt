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
import io.miret.etienne.registre.back.security.model.Passkey
import io.miret.etienne.registre.back.security.model.PasskeyChallenge
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.PasskeyChallengeRepository
import io.miret.etienne.registre.back.security.repositories.PasskeyRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.common.passkey.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
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
 * 1. **Options** — the server generates a random challenge, persists it,
 *    and returns options for the browser to pass to the WebAuthn API.
 * 2. **Completion** — the browser submits a signed response; the server
 *    retrieves and validates the stored challenge, then verifies the
 *    cryptographic response via [WebAuthnAsyncManager].
 */
@Service
class PasskeyService(
  private val asyncManager: WebAuthnAsyncManager,
  private val objectConverter: ObjectConverter,
  private val challengeRepository: PasskeyChallengeRepository,
  private val passkeyRepository: PasskeyRepository,
  private val userRepository: UserRepository,
  private val clock: Clock,
  private val config: RegistreConfig,
) {

  /** How long a challenge remains valid after it is issued. */
  private val challengeExpiry = Duration.ofMinutes(5)

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
   * Generates a registration challenge for [user] and persists it.
   *
   * The options exclude the user's existing passkeys, so that the browser
   * refuses to register a second passkey on the same authenticator (which
   * would otherwise replace the first one).
   *
   * @return [RegistrationOptions] to be forwarded to the browser as
   *   the argument to `navigator.credentials.create()`.
   */
  suspend fun generateRegistrationChallenge(user: User): RegistrationOptions {
    val challenge = DefaultChallenge()
    val challengeBase64 = Base64UrlUtil.encodeToString(challenge.value)
    challengeRepository.save(
      PasskeyChallenge(
        id = challengeBase64,
        userId = user.id,
        expiresAt = Instant.now(clock).plus(challengeExpiry),
      )
    )
    return RegistrationOptions(
      challenge = challengeBase64,
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
   * Retrieves and deletes the stored challenge for [user], then delegates
   * cryptographic verification to [WebAuthnAsyncManager].
   * Throws [InvalidChallengeException] if the challenge is missing, expired, or
   * belongs to a different user.
   *
   * @param user The authenticated user completing the registration.
   * @param request The credential submitted by the browser.
   */
  suspend fun completeRegistration(user: User, request: RegistrationDtoRequest) {
    val clientDataJson = decode("clientDataJSON", request.response.clientDataJSON)
    val attestationObject = decode("attestationObject", request.response.attestationObject)

    val storedChallenge = consumeChallenge(clientDataJson, user.id)

    val registrationRequest = RegistrationRequest(
      attestationObject,
      clientDataJson,
      null as Set<String>?,
    )
    val registrationParameters = RegistrationParameters(
      serverProperty(storedChallenge.id),
      pubKeyCredParams,
      true,
      true,
    )
    val result = asyncManager.verify(registrationRequest, registrationParameters)
      .await()

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
   * Generates an authentication challenge and persists it.
   *
   * The challenge is not bound to a specific user because the user
   * identity is not known until the browser submits its response.
   *
   * @return [AuthenticationOptions] to be forwarded to the browser as
   *   the argument to `navigator.credentials.get()`.
   */
  suspend fun generateAuthenticationChallenge(): AuthenticationOptions {
    val challenge = DefaultChallenge()
    val challengeBase64 = Base64UrlUtil.encodeToString(challenge.value)
    challengeRepository.save(
      PasskeyChallenge(
        id = challengeBase64,
        userId = null,
        expiresAt = Instant.now(clock).plus(challengeExpiry),
      )
    )
    return AuthenticationOptions(
      challenge = challengeBase64,
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
   * Looks up the stored credential by ID, retrieves and deletes the
   * stored challenge, then delegates cryptographic verification to
   * [WebAuthnAsyncManager]. Updates the stored sign count and backup
   * state on success.
   *
   * @param request The credential assertion submitted by the browser.
   * @return The authenticated [User], or `null` if the credential ID
   *   is not found.
   */
  suspend fun completeAuthentication(request: AuthenticationDtoRequest): User? {
    val storedCredential = passkeyRepository.findById(request.id)
      ?: return null

    // Decode everything before consuming the challenge, so that a malformed
    // request is rejected without using it up.
    val credentialId = decode("id", request.id)
    val clientDataJson = decode("clientDataJSON", request.response.clientDataJSON)
    val authenticatorData = decode("authenticatorData", request.response.authenticatorData)
    val signature = decode("signature", request.response.signature)
    val userHandle = request.response.userHandle?.let { decode("userHandle", it) }

    val storedChallenge = consumeChallenge(clientDataJson, null)

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
      serverProperty(storedChallenge.id),
      credentialRecord,
      null,
      true,
      true,
    )

    val result = asyncManager.verify(authenticationRequest, authenticationParameters).await()

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
   * Extracts the challenge from [clientDataJson], looks it up in
   * the repository, validates it, and deletes it so it cannot be
   * reused.
   *
   * @param clientDataJson Decoded `clientDataJSON` from the browser
   *   response.
   * @param expectedUserId The challenge's [PasskeyChallenge.userId] must
   *   equal this value: the user's ID for registration, `null` for
   *   authentication.
   * @return The consumed [PasskeyChallenge].
   * @throws InvalidClientDataException if [clientDataJson] is not a JSON
   *   object with a string `challenge` field.
   * @throws InvalidChallengeException if the challenge is not found,
   *   expired, or owned by a different user.
   */
  private suspend fun consumeChallenge(
    clientDataJson: ByteArray,
    expectedUserId: String?,
  ): PasskeyChallenge {
    val clientData = try {
      val jsonString = String(clientDataJson, Charsets.UTF_8)
      objectConverter.jsonMapper.readValue(jsonString, Map::class.java)
    } catch (e: JacksonException) {
      throw InvalidClientDataException("Invalid JSON in clientDataJSON", e)
    }
    val challengeBase64 = clientData?.get("challenge") as? String
      ?: throw InvalidClientDataException("Missing challenge in clientDataJSON")

    val challenge = challengeRepository.deleteChallengeById(challengeBase64)
      ?: throw InvalidChallengeException("Challenge not found")
    if (challenge.expiresAt < Instant.now(clock)) {
      throw InvalidChallengeException("Challenge expired")
    }
    if (challenge.userId != expectedUserId) {
      throw InvalidChallengeException("Challenge user mismatch")
    }
    return challenge
  }

}
