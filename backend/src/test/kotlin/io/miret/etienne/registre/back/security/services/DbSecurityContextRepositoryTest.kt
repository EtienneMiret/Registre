package io.miret.etienne.registre.back.security.services

import io.miret.etienne.registre.back.security.model.Session
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.SessionRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpCookie
import org.springframework.http.ResponseCookie
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.server.ServerWebExchange
import java.time.Clock
import java.time.Duration
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@ExtendWith(MockitoExtension::class)
class DbSecurityContextRepositoryTest {

  private val now = Instant.parse("2026-01-03T14:00:00.00Z")

  @InjectMocks
  private lateinit var repository: DbSecurityContextRepository

  @Mock
  private lateinit var sessionRepository: SessionRepository

  @Mock
  private lateinit var userRepository: UserRepository

  @Mock
  private lateinit var clock: Clock

  @Mock
  private lateinit var exchange: ServerWebExchange

  @Nested
  inner class Save {

    @Mock
    private lateinit var context: SecurityContext

    @Mock
    private lateinit var response: ServerHttpResponse

    @Test
    fun `should noop on null SecurityContext`() {
      repository.save(exchange, null).block()

      verifyNoInteractions(sessionRepository)
      verifyNoInteractions(exchange)
    }

    @Test
    fun `should noop when user is not authenticated`() {
      val authentication = TestingAuthenticationToken("user", "****")
      authentication.isAuthenticated = false
      whenever(context.authentication).thenReturn(authentication)

      repository.save(exchange, context).block()

      verifyNoInteractions(sessionRepository)
      verifyNoInteractions(exchange)
    }

    @Test
    fun `should noop when principal is not a User`() {
      val authentication = TestingAuthenticationToken("user", "****")
      authentication.isAuthenticated = true
      whenever(context.authentication).thenReturn(authentication)

      repository.save(exchange, context).block()

      verifyNoInteractions(sessionRepository)
      verifyNoInteractions(exchange)
    }

    @Test
    fun `should create new session`() {
      val user = User("123", "test", admin = false)
      val authentication = TestingAuthenticationToken(user, "****")
      authentication.isAuthenticated = true
      whenever(context.authentication).thenReturn(authentication)
      whenever(exchange.response).thenReturn(response)
      whenever(clock.instant()).thenReturn(now)

      repository.save(exchange, context).block()

      val session = runBlocking {
        argumentCaptor<Session> {
          verify(sessionRepository).save(capture())
        }.firstValue
      }
      val cookie = argumentCaptor<ResponseCookie> {
        verify(response).addCookie(capture())
      }.firstValue
      assertThat(session.userId).isEqualTo(user.id)
      assertThat(cookie.value).isEqualTo(session.id)
      assertThat(cookie.isSecure).isTrue()
      assertThat(cookie.isHttpOnly).isTrue()
      assertThat(cookie.sameSite).isEqualTo("Strict")
      val minDuration = Duration.ofDays(5)
      assertThat(session.expiresAt).isAfter(now.plus(minDuration))
      assertThat(cookie.maxAge).isGreaterThan(minDuration)
    }
  }

  @Nested
  inner class Load {

    @Mock
    private lateinit var request: ServerHttpRequest

    private val cookies = MultiValueMapAdapter(
      mutableMapOf<String, List<HttpCookie>>()
    )

    @BeforeEach
    fun init() {
      whenever(exchange.request).thenReturn(request)
      whenever(request.cookies).thenReturn(cookies)
    }

    @Test
    fun `should not return context when no cookie`() {
      val actual = repository.load(exchange).blockOptional()

      assertThat(actual).isEmpty()
    }

    @Test
    fun `should return context with user from session`() {
      val user = User("123", "test", false)
      val session = Session(userId = user.id, expiresAt = now.plusSeconds(120))
      cookies["session"] = HttpCookie("session", session.id)
      runBlocking {
        whenever(sessionRepository.findById(session.id))
          .thenReturn(session)
        whenever(userRepository.findById(user.id))
          .thenReturn(user)
      }
      whenever(clock.instant()).thenReturn(now)

      val actual = repository.load(exchange).blockOptional()

      assertThat(actual.getOrNull()?.authentication?.isAuthenticated).isTrue()
      assertThat(actual.getOrNull()?.authentication?.principal).isEqualTo(user)
    }

    @Test
    fun `should not return context when session is expired`() {
      val session = Session(userId = "123", expiresAt = now)
      cookies["session"] = HttpCookie("session", session.id)
      runBlocking {
        whenever(sessionRepository.findById(session.id))
          .thenReturn(session)
      }
      whenever(clock.instant()).thenReturn(now)

      val actual = repository.load(exchange).blockOptional()

      assertThat(actual).isEmpty()
    }

    @Test
    fun `should not return context when invalid session ID`() {
      val randomId = "invalid"
      cookies["session"] = HttpCookie("session", randomId)
      runBlocking {
        whenever(sessionRepository.findById(randomId))
          .thenReturn(null)
      }

      val actual = repository.load(exchange).blockOptional()

      assertThat(actual).isEmpty()
    }

  }

}
