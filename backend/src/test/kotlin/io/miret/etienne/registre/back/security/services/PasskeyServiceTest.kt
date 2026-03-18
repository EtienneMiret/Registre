package io.miret.etienne.registre.back.security.services

import com.webauthn4j.async.WebAuthnAsyncManager
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.util.Base64UrlUtil
import io.miret.etienne.registre.back.config.RegistreConfig
import io.miret.etienne.registre.back.config.WebAuthnConfig
import io.miret.etienne.registre.back.security.model.PasskeyChallenge
import io.miret.etienne.registre.back.security.model.PasskeyCredential
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.PasskeyChallengeRepository
import io.miret.etienne.registre.back.security.repositories.PasskeyCredentialRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.common.passkey.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Clock
import java.time.Instant

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

  private val objectConverter = ObjectConverter()

  @Mock private lateinit var asyncManager: WebAuthnAsyncManager
  @Mock private lateinit var challengeRepository: PasskeyChallengeRepository
  @Mock private lateinit var credentialRepository: PasskeyCredentialRepository
  @Mock private lateinit var userRepository: UserRepository
  @Mock private lateinit var clock: Clock

  private lateinit var service: PasskeyService

  @BeforeEach
  fun setUp() {
    service = PasskeyService(
      asyncManager,
      objectConverter,
      challengeRepository,
      credentialRepository,
      userRepository,
      clock,
      config,
    )
  }

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

    @Test
    fun `should save challenge bound to user`() {
      whenever(clock.instant()).thenReturn(now)

      runBlocking { service.generateRegistrationChallenge(user) }

      val challenge = runBlocking {
        argumentCaptor<PasskeyChallenge> {
          verify(challengeRepository).save(capture())
        }.firstValue
      }
      assertThat(challenge.userId).isEqualTo("user-1")
      assertThat(challenge.expiresAt).isAfter(now)
    }

    @Test
    fun `should return options matching saved challenge`() {
      whenever(clock.instant()).thenReturn(now)

      val options = runBlocking {
        service.generateRegistrationChallenge(user)
      }

      val challenge = runBlocking {
        argumentCaptor<PasskeyChallenge> {
          verify(challengeRepository).save(capture())
        }.firstValue
      }
      assertThat(options.challenge).isEqualTo(challenge.challengeBase64)
      assertThat(options.rp).isEqualTo(RpInfo("example.com", "Example"))
      assertThat(options.attestation).isEqualTo("none")
      assertThat(options.authenticatorSelection.userVerification)
        .isEqualTo(UserVerification.REQUIRED)
    }
  }

  @Nested
  inner class GenerateAuthenticationChallenge {

    @Test
    fun `should save challenge with no user`() {
      whenever(clock.instant()).thenReturn(now)

      runBlocking { service.generateAuthenticationChallenge() }

      val challenge = runBlocking {
        argumentCaptor<PasskeyChallenge> {
          verify(challengeRepository).save(capture())
        }.firstValue
      }
      assertThat(challenge.userId).isNull()
      assertThat(challenge.expiresAt).isAfter(now)
    }

    @Test
    fun `should return options matching saved challenge`() {
      whenever(clock.instant()).thenReturn(now)

      val options = runBlocking {
        service.generateAuthenticationChallenge()
      }

      val challenge = runBlocking {
        argumentCaptor<PasskeyChallenge> {
          verify(challengeRepository).save(capture())
        }.firstValue
      }
      assertThat(options.challenge).isEqualTo(challenge.challengeBase64)
      assertThat(options.rpId).isEqualTo("example.com")
      assertThat(options.allowCredentials).isEmpty()
      assertThat(options.userVerification).isEqualTo(UserVerification.REQUIRED)
    }
  }

  @Nested
  inner class CompleteRegistration {

    private val user = User("user-1", "Alice", admin = false)
    private val challengeBase64 =
      Base64UrlUtil.encodeToString(byteArrayOf(1, 2, 3))

    private fun request() = RegistrationDtoRequest(
      id = "cred-id",
      name = "My passkey",
      response = RegistrationResponseData(
        clientDataJSON = clientDataJson(challengeBase64),
        attestationObject = Base64UrlUtil.encodeToString(byteArrayOf()),
      ),
    )

    @Test
    fun `should throw when challenge is not found`() {
      runBlocking {
        whenever(challengeRepository.findById(challengeBase64))
          .thenReturn(null)
      }

      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge not found")
    }

    @Test
    fun `should throw when challenge is expired`() {
      val expired = PasskeyChallenge(
        challengeBase64 = challengeBase64,
        userId = user.id,
        expiresAt = now.minusSeconds(1),
      )
      runBlocking {
        whenever(challengeRepository.findById(challengeBase64))
          .thenReturn(expired)
      }
      whenever(clock.instant()).thenReturn(now)

      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge expired")
    }

    @Test
    fun `should throw when challenge belongs to a different user`() {
      val mismatch = PasskeyChallenge(
        challengeBase64 = challengeBase64,
        userId = "other-user",
        expiresAt = now.plusSeconds(60),
      )
      runBlocking {
        whenever(challengeRepository.findById(challengeBase64))
          .thenReturn(mismatch)
      }
      whenever(clock.instant()).thenReturn(now)

      assertThatThrownBy {
        runBlocking { service.completeRegistration(user, request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge user mismatch")
    }
  }

  @Nested
  inner class CompleteAuthentication {

    private val credentialId =
      Base64UrlUtil.encodeToString(byteArrayOf(1, 2, 3))
    private val challengeBase64 =
      Base64UrlUtil.encodeToString(byteArrayOf(4, 5, 6))

    private fun request() = AuthenticationDtoRequest(
      id = credentialId,
      response = AuthenticationResponseData(
        clientDataJSON = clientDataJson(challengeBase64),
        authenticatorData = Base64UrlUtil.encodeToString(byteArrayOf()),
        signature = Base64UrlUtil.encodeToString(byteArrayOf()),
        userHandle = null,
      ),
    )

    private fun storedCredential() = PasskeyCredential(
      id = credentialId,
      userId = "user-1",
      name = "My passkey",
      attestedCredentialDataCbor = byteArrayOf(),
      signCount = 0,
      backupEligible = false,
    )

    @Test
    fun `should return null when credential is not found`() {
      runBlocking {
        whenever(credentialRepository.findById(credentialId)).thenReturn(null)
      }

      val result = runBlocking { service.completeAuthentication(request()) }

      assertThat(result).isNull()
    }

    @Test
    fun `should throw when challenge is not found`() {
      runBlocking {
        whenever(credentialRepository.findById(credentialId))
          .thenReturn(storedCredential())
        whenever(challengeRepository.findById(challengeBase64)).thenReturn(null)
      }

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge not found")
    }

    @Test
    fun `should throw when challenge is expired`() {
      val expired = PasskeyChallenge(
        challengeBase64 = challengeBase64,
        userId = null,
        expiresAt = now.minusSeconds(1),
      )
      runBlocking {
        whenever(credentialRepository.findById(credentialId))
          .thenReturn(storedCredential())
        whenever(challengeRepository.findById(challengeBase64))
          .thenReturn(expired)
      }
      whenever(clock.instant()).thenReturn(now)

      assertThatThrownBy {
        runBlocking { service.completeAuthentication(request()) }
      }.isInstanceOf(IllegalStateException::class.java)
        .hasMessage("Challenge expired")
    }
  }
}
