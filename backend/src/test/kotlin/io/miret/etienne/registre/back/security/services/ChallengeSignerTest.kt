package io.miret.etienne.registre.back.security.services

import io.miret.etienne.registre.back.parameters.model.Parameter
import io.miret.etienne.registre.back.parameters.repositories.ParameterRepository
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.dao.DuplicateKeyException
import java.time.Instant
import java.util.Base64

@ExtendWith(MockitoExtension::class)
class ChallengeSignerTest {

  private val expiresAt = Instant.parse("2026-01-03T14:05:00Z")
  private val key = Parameter(
    ChallengeSigner.KEY_PARAMETER,
    Base64.getEncoder().encodeToString(ByteArray(32) { it.toByte() }),
  )
  private val otherKey = Parameter(
    ChallengeSigner.KEY_PARAMETER,
    Base64.getEncoder().encodeToString(ByteArray(32) { (it + 1).toByte() }),
  )

  @Mock private lateinit var parameterRepository: ParameterRepository

  companion object {
    private const val PURPOSE = "registration:user-1"
  }

  @Nested
  inner class WithStoredKey {

    private lateinit var signer: ChallengeSigner

    @BeforeEach
    fun setUp() {
      runBlocking {
        whenever(parameterRepository.findById(ChallengeSigner.KEY_PARAMETER)).thenReturn(key)
      }
      signer = ChallengeSigner(parameterRepository)
    }

    @Test
    fun `should verify its own challenges`() {
      val result = runBlocking { signer.verify(PURPOSE, signer.sign(PURPOSE, expiresAt)) }

      assertThat(result).isEqualTo(expiresAt)
    }

    @Test
    fun `should issue different challenges`() {
      val challenges = runBlocking { (1..2).map { signer.sign(PURPOSE, expiresAt) } }

      assertThat(challenges[0]).isNotEqualTo(challenges[1])
    }

    @Test
    fun `should reject a tampered challenge`() {
      val challenge = runBlocking { signer.sign(PURPOSE, expiresAt) }

      // Every byte is covered by the signature, including the expiry date.
      for (i in challenge.indices) {
        val tampered = challenge.copyOf()
        tampered[i] = (tampered[i] + 1).toByte()

        assertThat(runBlocking { signer.verify(PURPOSE, tampered) }).isNull()
      }
    }

    @Test
    fun `should reject a challenge of the wrong size`() {
      val challenge = runBlocking { signer.sign(PURPOSE, expiresAt) }

      assertThat(runBlocking { signer.verify(PURPOSE, challenge.copyOf(challenge.size - 1)) }).isNull()
      assertThat(runBlocking { signer.verify(PURPOSE, challenge + 0) }).isNull()
      assertThat(runBlocking { signer.verify(PURPOSE, ByteArray(0)) }).isNull()
    }

    @Test
    fun `should reject a challenge issued for another purpose`() {
      val challenge = runBlocking { signer.sign(PURPOSE, expiresAt) }

      assertThat(runBlocking { signer.verify("registration:user-2", challenge) }).isNull()
      assertThat(runBlocking { signer.verify("authentication", challenge) }).isNull()
      assertThat(runBlocking { signer.verify("", challenge) }).isNull()
    }

    @Test
    fun `should reject a challenge signed with another key`() {
      val otherRepository = mock<ParameterRepository> {
        on { findById(ChallengeSigner.KEY_PARAMETER) } doReturn otherKey
      }
      val challenge = runBlocking { ChallengeSigner(otherRepository).sign(PURPOSE, expiresAt) }

      assertThat(runBlocking { signer.verify(PURPOSE, challenge) }).isNull()
    }

    @Test
    fun `should load the key only once`() {
      runBlocking {
        signer.sign(PURPOSE, expiresAt)
        signer.sign(PURPOSE, expiresAt)
      }

      runBlocking {
        verify(parameterRepository, times(1)).findById(ChallengeSigner.KEY_PARAMETER)
        verify(parameterRepository, never()).insert(any())
      }
    }

  }

  @Nested
  inner class WithoutStoredKey {

    @Test
    fun `should create and store a key`() {
      runBlocking {
        whenever(parameterRepository.findById(ChallengeSigner.KEY_PARAMETER)).thenReturn(null)
      }
      val signer = ChallengeSigner(parameterRepository)

      val challenge = runBlocking { signer.sign(PURPOSE, expiresAt) }

      val stored = runBlocking {
        argumentCaptor<Parameter> {
          verify(parameterRepository).insert(capture())
        }.firstValue
      }
      assertThat(stored.key).isEqualTo(ChallengeSigner.KEY_PARAMETER)
      assertThat(Base64.getDecoder().decode(stored.value)).hasSize(32)
      // A signer reading the stored key accepts the challenge.
      val otherRepository = mock<ParameterRepository> {
        on { findById(ChallengeSigner.KEY_PARAMETER) } doReturn stored
      }
      assertThat(runBlocking { ChallengeSigner(otherRepository).verify(PURPOSE, challenge) })
        .isEqualTo(expiresAt)
    }

    @Test
    fun `should use the key created concurrently by another instance`() {
      runBlocking {
        whenever(parameterRepository.findById(ChallengeSigner.KEY_PARAMETER))
          .thenReturn(null, key)
        whenever(parameterRepository.insert(any()))
          .thenThrow(DuplicateKeyException("duplicate"))
      }
      val signer = ChallengeSigner(parameterRepository)
      val otherRepository = mock<ParameterRepository> {
        on { findById(ChallengeSigner.KEY_PARAMETER) } doReturn key
      }

      val challenge = runBlocking { signer.sign(PURPOSE, expiresAt) }

      assertThat(runBlocking { ChallengeSigner(otherRepository).verify(PURPOSE, challenge) })
        .isEqualTo(expiresAt)
    }

  }

}
