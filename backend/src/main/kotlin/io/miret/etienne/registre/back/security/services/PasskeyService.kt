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
import io.miret.etienne.registre.back.security.model.PasskeyChallenge
import io.miret.etienne.registre.back.security.model.PasskeyCredential
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.PasskeyChallengeRepository
import io.miret.etienne.registre.back.security.repositories.PasskeyCredentialRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.common.passkey.*
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Service
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
  private val credentialRepository: PasskeyCredentialRepository,
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
   * @return [RegistrationOptions] to be forwarded to the browser as
   *   the argument to `navigator.credentials.create()`.
   */
  suspend fun generateRegistrationChallenge(user: User): RegistrationOptions {
    val challenge = DefaultChallenge()
    val challengeBase64 = Base64UrlUtil.encodeToString(challenge.value)
    challengeRepository.save(
      PasskeyChallenge(
        challengeBase64 = challengeBase64,
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
    )
  }

  /**
   * Verifies the browser's registration response and persists the new passkey
   * credential.
   *
   * Retrieves and deletes the stored challenge for [user], then delegates
   * cryptographic verification to [WebAuthnAsyncManager].
   * Throws [IllegalStateException] if the challenge is missing, expired, or
   * belongs to a different user.
   *
   * @param user The authenticated user completing the registration.
   * @param request The credential submitted by the browser.
   */
  suspend fun completeRegistration(user: User, request: RegistrationDtoRequest) {
    val storedChallenge = consumeChallenge(request.response.clientDataJSON, user.id)

    val registrationRequest = RegistrationRequest(
      Base64UrlUtil.decode(request.response.attestationObject),
      Base64UrlUtil.decode(request.response.clientDataJSON),
      null as Set<String>?,
    )
    val registrationParameters = RegistrationParameters(
      serverProperty(storedChallenge.challengeBase64),
      pubKeyCredParams,
      true,
      true,
    )
    val result = asyncManager.verify(registrationRequest, registrationParameters)
      .await()

    val authenticatorData = result.attestationObject!!.authenticatorData
    val attestedCredentialData = authenticatorData.attestedCredentialData!!

    credentialRepository.save(
      PasskeyCredential(
        id = Base64UrlUtil.encodeToString(attestedCredentialData.credentialId!!),
        userId = user.id,
        name = request.name,
        attestedCredentialDataCbor = objectConverter.cborMapper
          .writeValueAsBytes(attestedCredentialData),
        signCount = authenticatorData.signCount,
        backupEligible = authenticatorData.isFlagBE(),
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
        challengeBase64 = challengeBase64,
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
    val storedCredential = credentialRepository.findById(request.id)
      ?: return null

    val storedChallenge = consumeChallenge(request.response.clientDataJSON, null)

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

    val userHandle = request.response.userHandle?.let { Base64UrlUtil.decode(it) }

    val authenticationRequest = AuthenticationRequest(
      Base64UrlUtil.decode(request.id),
      userHandle,
      Base64UrlUtil.decode(request.response.authenticatorData),
      Base64UrlUtil.decode(request.response.clientDataJSON),
      Base64UrlUtil.decode(request.response.signature),
    )
    val authenticationParameters = AuthenticationParameters(
      serverProperty(storedChallenge.challengeBase64),
      credentialRecord,
      null,
      true,
      true,
    )

    val result = asyncManager.verify(authenticationRequest, authenticationParameters).await()

    credentialRepository.save(
      storedCredential.copy(signCount = result.authenticatorData!!.signCount)
    )

    return userRepository.findById(storedCredential.userId)
  }

  /**
   * Extracts the challenge from [clientDataJSONBase64], looks it up in
   * the repository, validates it, and deletes it so it cannot be
   * reused.
   *
   * @param clientDataJSONBase64 Base64URL-encoded `clientDataJSON` from
   *   the browser response.
   * @param expectedUserId When non-null, the challenge's
   *   [PasskeyChallenge.userId] must match this value.
   * @return The consumed [PasskeyChallenge].
   * @throws IllegalStateException if the challenge is not found,
   *   expired, or owned by a different user.
   */
  private suspend fun consumeChallenge(
    clientDataJSONBase64: String,
    expectedUserId: String?,
  ): PasskeyChallenge {
    val clientDataBytes = Base64UrlUtil.decode(clientDataJSONBase64)
    val clientDataJson = String(clientDataBytes, Charsets.UTF_8)
    @Suppress("UNCHECKED_CAST")
    val clientData = objectConverter.jsonMapper
      .readValue(clientDataJson, Map::class.java) as Map<String, Any?>
    val challengeBase64 = clientData["challenge"] as String

    val challenge = challengeRepository.findById(challengeBase64)
      ?: throw IllegalStateException("Challenge not found")
    if (challenge.expiresAt < Instant.now(clock)) {
      challengeRepository.delete(challenge)
      throw IllegalStateException("Challenge expired")
    }
    if (expectedUserId != null && challenge.userId != expectedUserId) {
      throw IllegalStateException("Challenge user mismatch")
    }
    challengeRepository.delete(challenge)
    return challenge
  }

}
