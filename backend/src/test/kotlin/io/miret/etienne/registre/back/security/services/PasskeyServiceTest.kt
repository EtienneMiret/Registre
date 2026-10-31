package io.miret.etienne.registre.back.security.services

import com.webauthn4j.async.WebAuthnAsyncManager
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.*
import com.webauthn4j.data.attestation.AttestationObject
import com.webauthn4j.data.attestation.authenticator.AAGUID
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData
import com.webauthn4j.data.attestation.authenticator.RSACOSEKey
import com.webauthn4j.data.attestation.statement.AttestationStatement
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput
import com.webauthn4j.util.Base64UrlUtil
import io.miret.etienne.registre.back.config.RegistreConfig
import io.miret.etienne.registre.back.config.WebAuthnConfig
import io.miret.etienne.registre.back.parameters.model.Parameter
import io.miret.etienne.registre.back.parameters.repositories.ParameterRepository
import io.miret.etienne.registre.back.security.exceptions.InvalidClientDataException
import io.miret.etienne.registre.back.security.exceptions.InvalidPasskeyException
import io.miret.etienne.registre.back.security.model.Passkey
import io.miret.etienne.registre.back.security.model.UsedPasskeyChallenge
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.PasskeyRepository
import io.miret.etienne.registre.back.security.repositories.UsedPasskeyChallengeRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.common.passkey.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.dao.DuplicateKeyException
import tools.jackson.databind.json.JsonMapper
import tools.jackson.dataformat.cbor.CBORMapper
import java.time.Clock
import java.time.Instant
import java.util.Base64
import java.util.concurrent.CompletableFuture

@ExtendWith(MockitoExtension::class)
class PasskeyServiceTest {

  private val now = Instant.parse("2026-01-03T14:00:00.00Z")

  private val config = RegistreConfig(
    mongoConnectionString = "mongodb://localhost",
    webauthn = WebAuthnConfig(
      rpId = "example.com",
      rpName = "Example",
      origin = "https://example.com",
    ),
  )

  @Mock private lateinit var asyncManager: WebAuthnAsyncManager
  @Mock private lateinit var objectConverter: ObjectConverter
  @Mock private lateinit var usedChallengeRepository: UsedPasskeyChallengeRepository
  @Mock private lateinit var parameterRepository: ParameterRepository
  @Mock private lateinit var passkeyRepository: PasskeyRepository
  @Mock private lateinit var userRepository: UserRepository
  @Mock private lateinit var clock: Clock

  private lateinit var challengeSigner: ChallengeSigner
  private lateinit var service: PasskeyService

  @BeforeEach
  fun setUp() {
    runBlocking {
      lenient().`when`(parameterRepository.findById(ChallengeSigner.KEY_PARAMETER))
        .thenReturn(
          Parameter(
            ChallengeSigner.KEY_PARAMETER,
            Base64.getEncoder().encodeToString(ByteArray(32) { it.toByte() }),
          )
        )
    }
    challengeSigner = ChallengeSigner(parameterRepository)
    service = PasskeyService(
      asyncManager,
      objectConverter,
      usedChallengeRepository,
      challengeSigner,
      passkeyRepository,
      userRepository,
      clock,
      config,
    )
  }

  /**
   * Returns a base64url-encoded challenge, signed for [purpose]: either
   * `authentication`, or `registration:` followed by the user's ID.
   */
  private fun signedChallenge(purpose: String, expiresAt: Instant): String =
    Base64UrlUtil.encodeToString(runBlocking { challengeSigner.sign(purpose, expiresAt) })

  /**
   * Builds a base64url-encoded clientDataJSON whose `challenge` field
   * matches [challengeBase64].
   */
  private fun clientDataJson(challengeBase64: String): String {
    val json = """{"challenge":"$challengeBase64"}"""
    return Base64UrlUtil.encodeToString(json.toByteArray(Charsets.UTF_8))
  }

  @Nested
  inner class GenerateRegistrationChallenge {

    private val user = User("user-1", "Alice", admin = false)

    @BeforeEach
    fun stubPasskeys() {
      whenever(passkeyRepository.findAllByUserId("user-1"))
        .thenReturn(flowOf(passkey("cred-1"), passkey("cred-2")))
    }

    @Test
    fun `should not write to the database`() {
      whenever(clock.instant()).thenReturn(now)

      runBlocking { service.generateRegistrationChallenge(user) }

      verifyNoInteractions(usedChallengeRepository)
      runBlocking { verify(parameterRepository, never()).insert(any()) }
    }

    @Test
    fun `should return options with a challenge signed for the user`() {
      whenever(clock.instant()).thenReturn(now)

      val options = runBlocking {
        service.generateRegistrationChallenge(user)
      }

      val challenge = Base64UrlUtil.decode(options.challenge)
      runBlocking {
        assertThat(challengeSigner.verify("registration:user-1", challenge))
          .isEqualTo(now.plusSeconds(300))
        assertThat(challengeSigner.verify("registration:user-2", challenge)).isNull()
        assertThat(challengeSigner.verify("authentication", challenge)).isNull()
      }
      assertThat(options.rp).isEqualTo(RpInfo("example.com", "Example"))
      assertThat(options.attestation).isEqualTo("none")
      assertThat(options.authenticatorSelection.userVerification)
        .isEqualTo(UserVerification.REQUIRED)
    }

    @Test
    fun `should exclude existing passkeys of the user`() {
      whenever(clock.instant()).thenReturn(now)

      val options = runBlocking {
        service.generateRegistrationChallenge(user)
      }

      assertThat(options.excludeCredentials).containsExactly(
        CredentialDescriptor("public-key", "cred-1", emptySet()),
        CredentialDescriptor("public-key", "cred-2", emptySet()),
      )
    }

    private fun passkey(id: String) = Passkey(
      id = id,
      userId = "user-1",
      name = "Passkey $id",
      attestedCredentialDataCbor = byteArrayOf(),
      signCount = 0,
      backupEligible = false,
      createdAt = now.minusSeconds(78000),
      lastUsedAt = null,
    )

  }

  @Nested
  inner class GenerateAuthenticationChallenge {

    @Test
    fun `should not write to the database`() {
      whenever(clock.instant()).thenReturn(now)

      runBlocking { service.generateAuthenticationChallenge() }

      verifyNoInteractions(usedChallengeRepository)
      runBlocking { verify(parameterRepository, never()).insert(any()) }
    }

    @Test
    fun `should return options with a signed challenge`() {
      whenever(clock.instant()).thenReturn(now)

      val options = runBlocking {
        service.generateAuthenticationChallenge()
      }

      val challenge = Base64UrlUtil.decode(options.challenge)
      runBlocking {
        assertThat(challengeSigner.verify("authentication", challenge))
          .isEqualTo(now.plusSeconds(300))
        assertThat(challengeSigner.verify("registration:user-1", challenge)).isNull()
      }
      assertThat(options.rpId).isEqualTo("example.com")
      assertThat(options.allowCredentials).isEmpty()
      assertThat(options.userVerification).isEqualTo(UserVerification.REQUIRED)
    }

    @Test
    fun `should return a different challenge each time`() {
      whenever(clock.instant()).thenReturn(now)

      val challenges = runBlocking {
        (1..2).map { service.generateAuthenticationChallenge().challenge }
      }

      assertThat(challenges[0]).isNotEqualTo(challenges[1])
    }
  }

  @Nested
  inner class CompleteRegistration {

    @Mock private lateinit var cborMapper: CBORMapper

    private val user = User("user-1", "Alice", admin = false)
    private val expiresAt = now.plusSeconds(60)
    // Lazy, because the signer is only created by the outer setUp().
    private val challengeId by lazy { signedChallenge("registration:user-1", expiresAt) }

    private fun request(
      challenge: String = challengeId,
      attestationObject: String = Base64UrlUtil.encodeToString(byteArrayOf()),
    ) = RegistrationDtoRequest(
      id = "cred-id",
      name = "My passkey",
      response = RegistrationResponseData(
        clientDataJSON = clientDataJson(challenge),
        attestationObject = attestationObject,
      ),
    )

    @BeforeEach
    fun setup() {
      lenient().`when`(objectConverter.jsonMapper).thenReturn(JsonMapper())
    }

    @Test
    fun `should throw without checking challenge when attestationObject is not valid base64`() {
      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request(attestationObject = "!!!")) }
      }.isInstanceOf(InvalidClientDataException::class.java)
        .hasMessage("Invalid base64 in attestationObject")

      verifyNoInteractions(usedChallengeRepository, asyncManager)
    }

    @Test
    fun `should throw when challenge is not signed`() {
      assertInvalidChallenge(
        request(Base64UrlUtil.encodeToString(ByteArray(56))),
        "Invalid challenge",
      )
    }

    @Test
    fun `should throw when challenge belongs to a different user`() {
      assertInvalidChallenge(
        request(signedChallenge("registration:other-user", expiresAt)),
        "Invalid challenge",
      )
    }

    @Test
    fun `should throw when challenge was issued for authentication`() {
      assertInvalidChallenge(
        request(signedChallenge("authentication", expiresAt)),
        "Invalid challenge",
      )
    }

    @Test
    fun `should throw when challenge is expired`() {
      whenever(clock.instant()).thenReturn(now)

      assertInvalidChallenge(
        request(signedChallenge("registration:user-1", now.minusSeconds(1))),
        "Challenge expired",
      )
    }

    @Test
    fun `should throw when challenge was already used`() {
      stubVerification()
      runBlocking {
        whenever(usedChallengeRepository.insert(any()))
          .thenThrow(DuplicateKeyException("duplicate"))
      }

      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge already used")

      runBlocking { verify(passkeyRepository, never()).insert(any()) }
    }

    @Test
    fun `should not record challenge when response is invalid`() {
      whenever(clock.instant()).thenReturn(now)
      whenever(asyncManager.verify(any<RegistrationRequest>(), any<RegistrationParameters>()))
        .thenReturn(CompletableFuture.failedFuture(IllegalArgumentException("bad attestation")))

      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request()) }
      }.hasMessageContaining("bad attestation")

      verifyNoInteractions(usedChallengeRepository)
    }

    @Test
    fun `should throw when challenge expires before being recorded`() {
      stubVerification()
      whenever(clock.instant()).thenReturn(now, expiresAt)

      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge expired")

      runBlocking { verify(passkeyRepository, never()).insert(any()) }
    }

    @Test
    fun `should save credential on success`() {
      val credentialIdBase64 = Base64UrlUtil.encodeToString(byteArrayOf(10, 20, 30))
      stubVerification()

      runBlocking { service.completeRegistration(user, request()) }

      runBlocking {
        verify(usedChallengeRepository).insert(
          UsedPasskeyChallenge(challengeId, expiresAt.plusSeconds(300))
        )
      }
      val saved = runBlocking {
        argumentCaptor<Passkey> {
          verify(passkeyRepository).insert(capture())
        }.firstValue
      }
      assertThat(saved.id).isEqualTo(credentialIdBase64)
      assertThat(saved.userId).isEqualTo(user.id)
      assertThat(saved.name).isEqualTo("My passkey")
      assertThat(saved.attestedCredentialDataCbor).isEqualTo(byteArrayOf(1, 2, 3, 4))
      assertThat(saved.signCount).isEqualTo(1L)
      assertThat(saved.backupEligible).isTrue()
      assertThat(saved.createdAt).isEqualTo(now)
      assertThat(saved.lastUsedAt).isNull()
    }

    /**
     * Stubs the mocks for a response whose challenge is valid, and whose
     * verification by the [WebAuthnAsyncManager] succeeds.
     */
    private fun stubVerification() {
      val attestedCredentialData = AttestedCredentialData(
        AAGUID.ZERO,
        byteArrayOf(10, 20, 30),
        RSACOSEKey(null, null, null, null, null),
      )
      val authenticatorData = AuthenticatorData<RegistrationExtensionAuthenticatorOutput>(
        ByteArray(32),
        AuthenticatorData.BIT_BE,
        1L,
        attestedCredentialData,
      )
      val attestationObject = AttestationObject(authenticatorData, mock<AttestationStatement>())
      val registrationData = RegistrationData(attestationObject, null, null, null, null, null)
      lenient().`when`(objectConverter.cborMapper).thenReturn(cborMapper)
      lenient().`when`(cborMapper.writeValueAsBytes(attestedCredentialData))
        .thenReturn(byteArrayOf(1, 2, 3, 4))
      whenever(asyncManager.verify(any<RegistrationRequest>(), any<RegistrationParameters>()))
        .thenReturn(CompletableFuture.completedFuture(registrationData))
      whenever(clock.instant()).thenReturn(now)
    }

    private fun assertInvalidChallenge(request: RegistrationDtoRequest, message: String) {
      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage(message)

      verifyNoInteractions(usedChallengeRepository, asyncManager)
      runBlocking { verify(passkeyRepository, never()).insert(any()) }
    }
  }

  @Nested
  inner class CompleteAuthentication {

    @Mock private lateinit var cborMapper: CBORMapper

    private val credentialId =
      Base64UrlUtil.encodeToString(byteArrayOf(1, 2, 3))
    private val expiresAt = now.plusSeconds(60)
    // Lazy, because the signer is only created by the outer setUp().
    private val challengeId by lazy { signedChallenge("authentication", expiresAt) }

    private fun request(
      clientDataJSON: String = clientDataJson(challengeId),
      authenticatorData: String = Base64UrlUtil.encodeToString(byteArrayOf()),
      signature: String = Base64UrlUtil.encodeToString(byteArrayOf()),
      userHandle: String? = null,
    ) = AuthenticationDtoRequest(
      id = credentialId,
      response = AuthenticationResponseData(
        clientDataJSON = clientDataJSON,
        authenticatorData = authenticatorData,
        signature = signature,
        userHandle = userHandle,
      ),
    )

    private fun storedCredential() = Passkey(
      id = credentialId,
      userId = "user-1",
      name = "My passkey",
      attestedCredentialDataCbor = byteArrayOf(),
      signCount = 0,
      backupEligible = false,
      createdAt = now.minusSeconds(78000),
      lastUsedAt = null,
    )

    @Test
    fun `should return null when credential is not found`() {
      runBlocking {
        whenever(passkeyRepository.findById(credentialId)).thenReturn(null)
      }

      val result = runBlocking { service.completeAuthentication(request()) }

      assertThat(result).isNull()
    }

    @Test
    fun `should throw when clientDataJSON is not valid base64`() {
      assertInvalidClientData("not base64!", "Invalid base64 in clientDataJSON")
    }

    @Test
    fun `should throw without consuming challenge when authenticatorData is not valid base64`() {
      assertInvalidBase64(request(authenticatorData = "!!!"), "authenticatorData")
    }

    @Test
    fun `should throw without consuming challenge when signature is not valid base64`() {
      assertInvalidBase64(request(signature = "!!!"), "signature")
    }

    @Test
    fun `should throw without consuming challenge when userHandle is not valid base64`() {
      assertInvalidBase64(request(userHandle = "!!!"), "userHandle")
    }

    @Test
    fun `should throw when clientDataJSON is not valid JSON`() {
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertInvalidClientData(encode("{not json"), "Invalid JSON in clientDataJSON")
    }

    @Test
    fun `should throw when clientDataJSON is not a JSON object`() {
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertInvalidClientData(encode("[1, 2]"), "Invalid JSON in clientDataJSON")
    }

    @Test
    fun `should throw when clientDataJSON is null`() {
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertInvalidClientData(encode("null"), "Missing challenge in clientDataJSON")
    }

    @Test
    fun `should throw when challenge is missing`() {
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertInvalidClientData(encode("{}"), "Missing challenge in clientDataJSON")
    }

    @Test
    fun `should throw when challenge is not a string`() {
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertInvalidClientData(
        encode("""{"challenge":42}"""),
        "Missing challenge in clientDataJSON",
      )
    }

    @Test
    fun `should throw when challenge is not signed`() {
      val unsigned = Base64UrlUtil.encodeToString(ByteArray(56))

      assertInvalidChallenge(request(clientDataJson(unsigned)), "Invalid challenge")
    }

    @Test
    fun `should throw when challenge was tampered with`() {
      val bytes = Base64UrlUtil.decode(challengeId)
      bytes[20] = (bytes[20] + 1).toByte()
      val tampered = Base64UrlUtil.encodeToString(bytes)

      assertInvalidChallenge(request(clientDataJson(tampered)), "Invalid challenge")
    }

    @Test
    fun `should throw when challenge was issued for registration`() {
      val registrationChallenge = signedChallenge("registration:user-1", expiresAt)

      assertInvalidChallenge(
        request(clientDataJson(registrationChallenge)),
        "Invalid challenge",
      )
    }

    @Test
    fun `should throw when challenge is expired`() {
      val expired = signedChallenge("authentication", now.minusSeconds(1))
      whenever(clock.instant()).thenReturn(now)

      assertInvalidChallenge(request(clientDataJson(expired)), "Challenge expired")
    }

    @Test
    fun `should throw when challenge is not valid base64`() {
      runBlocking {
        whenever(passkeyRepository.findById(credentialId))
          .thenReturn(storedCredential())
      }
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request(clientDataJson("!!!"))) }
      }.isInstanceOf(InvalidClientDataException::class.java)
        .hasMessage("Invalid base64 in challenge")
    }

    @Test
    fun `should throw when challenge was already used`() {
      stubVerification()
      runBlocking {
        whenever(usedChallengeRepository.insert(any()))
          .thenThrow(DuplicateKeyException("duplicate"))
      }

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge already used")

      runBlocking { verify(passkeyRepository, never()).save(any()) }
    }

    @Test
    fun `should not record challenge when response is invalid`() {
      stubVerification(CompletableFuture.failedFuture(IllegalArgumentException("bad signature")))

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request()) }
      }.hasMessageContaining("bad signature")

      verifyNoInteractions(usedChallengeRepository)
    }

    /**
     * Stubs the mocks for a response whose challenge is valid, and whose
     * verification by the [WebAuthnAsyncManager] gives [result] (by default,
     * a successful one).
     */
    private fun stubVerification(
      result: CompletableFuture<AuthenticationData> = CompletableFuture.completedFuture(
        AuthenticationData(
          null,
          null,
          AuthenticatorData(ByteArray(32), 0, 42L),
          null,
          null,
          null,
          null,
          null,
        )
      ),
    ) {
      val attestedCredentialData = AttestedCredentialData(
        AAGUID.ZERO,
        Base64UrlUtil.decode(credentialId),
        RSACOSEKey(null, null, null, null, null),
      )
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())
      whenever(objectConverter.cborMapper).thenReturn(cborMapper)
      whenever(cborMapper.readValue(any<ByteArray>(), eq(AttestedCredentialData::class.java)))
        .thenReturn(attestedCredentialData)
      runBlocking {
        whenever(passkeyRepository.findById(credentialId))
          .thenReturn(storedCredential())
        whenever(asyncManager.verify(any<AuthenticationRequest>(), any<AuthenticationParameters>()))
          .thenReturn(result)
      }
      whenever(clock.instant()).thenReturn(now)
    }

    @Test
    fun `should throw when challenge expires before being recorded`() {
      stubVerification()
      whenever(clock.instant()).thenReturn(now, expiresAt)

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge expired")

      runBlocking { verify(passkeyRepository, never()).save(any()) }
    }

    @Test
    fun `should throw without consuming challenge when userHandle names another user`() {
      runBlocking {
        whenever(passkeyRepository.findById(credentialId))
          .thenReturn(storedCredential())
      }
      val otherUser = Base64UrlUtil.encodeToString("user-2".toByteArray())

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request(userHandle = otherUser)) }
      }.isInstanceOf(InvalidPasskeyException::class.java)
        .hasMessage("User handle does not match passkey owner")

      verifyNoInteractions(usedChallengeRepository, asyncManager)
    }

    @Test
    fun `should accept userHandle naming the passkey owner`() {
      val user = User("user-1", "Alice", admin = false)
      stubVerification()
      runBlocking {
        whenever(userRepository.findById("user-1")).thenReturn(user)
      }
      val owner = Base64UrlUtil.encodeToString("user-1".toByteArray())

      val result = runBlocking { service.completeAuthentication(request(userHandle = owner)) }

      assertThat(result).isEqualTo(user)
    }

    @Test
    fun `should return authenticated user on success`() {
      val user = User("user-1", "Alice", admin = false)
      stubVerification()
      runBlocking {
        whenever(userRepository.findById("user-1")).thenReturn(user)
      }

      val result = runBlocking { service.completeAuthentication(request()) }

      assertThat(result).isEqualTo(user)
      runBlocking {
        verify(usedChallengeRepository).insert(
          UsedPasskeyChallenge(challengeId, expiresAt.plusSeconds(300))
        )
      }
      val saved = runBlocking {
        argumentCaptor<Passkey> {
          verify(passkeyRepository).save(capture())
        }.firstValue
      }
      assertThat(saved.signCount).isEqualTo(42L)
      assertThat(saved.lastUsedAt).isEqualTo(now)
    }

    private fun encode(json: String) =
      Base64UrlUtil.encodeToString(json.toByteArray(Charsets.UTF_8))

    private fun assertInvalidClientData(clientDataJson: String, message: String) {
      runBlocking {
        whenever(passkeyRepository.findById(credentialId))
          .thenReturn(storedCredential())
      }

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request(clientDataJson)) }
      }.isInstanceOf(InvalidClientDataException::class.java)
        .hasMessage(message)
    }

    private fun assertInvalidBase64(request: AuthenticationDtoRequest, field: String) {
      runBlocking {
        whenever(passkeyRepository.findById(credentialId))
          .thenReturn(storedCredential())
      }

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request) }
      }.isInstanceOf(InvalidClientDataException::class.java)
        .hasMessage("Invalid base64 in $field")

      verifyNoInteractions(usedChallengeRepository)
    }

    private fun assertInvalidChallenge(request: AuthenticationDtoRequest, message: String) {
      runBlocking {
        whenever(passkeyRepository.findById(credentialId))
          .thenReturn(storedCredential())
      }
      whenever(objectConverter.jsonMapper).thenReturn(JsonMapper())

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage(message)

      verifyNoInteractions(usedChallengeRepository, asyncManager)
    }

  }

}
