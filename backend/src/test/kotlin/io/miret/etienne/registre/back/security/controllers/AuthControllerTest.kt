package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.back.security.model.OneTimeToken
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.OttRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.back.security.services.DbSecurityContextRepository
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import io.miret.etienne.registre.common.User as ApiUser

@ExtendWith(MockitoExtension::class)
class AuthControllerTest {

  @InjectMocks
  private lateinit var controller: AuthController

  @Mock
  private lateinit var ottRepository: OttRepository

  @Mock
  private lateinit var userRepository: UserRepository

  @Mock
  private lateinit var securityContextRepository: DbSecurityContextRepository

  @Mock
  private lateinit var exchange: ServerWebExchange

  private val user = User(id = "user-1", name = "Alice", admin = false)
  private val token = OneTimeToken(id = "token-abc", userId = user.id)

  @Nested
  inner class Login {

    @Test
    fun `should throw FORBIDDEN when token is not found`() {
      runBlocking { whenever(ottRepository.consume(token.id)).thenReturn(null) }

      assertThatThrownBy { runBlocking { controller.login(token.id, exchange) } }
        .isInstanceOf(ResponseStatusException::class.java)
        .extracting { (it as ResponseStatusException).statusCode }
        .isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should throw FORBIDDEN when user is not found`() {
      runBlocking {
        whenever(ottRepository.consume(token.id)).thenReturn(token)
        whenever(userRepository.findById(user.id)).thenReturn(null)
      }

      assertThatThrownBy { runBlocking { controller.login(token.id, exchange) } }
        .isInstanceOf(ResponseStatusException::class.java)
        .extracting { (it as ResponseStatusException).statusCode }
        .isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should save security context and return user`() {
      runBlocking {
        whenever(ottRepository.consume(token.id)).thenReturn(token)
        whenever(userRepository.findById(user.id)).thenReturn(user)
      }
      whenever(securityContextRepository.save(eq(exchange), anyOrNull())).thenReturn(Mono.empty())

      val actual = runBlocking { controller.login(token.id, exchange) }

      assertThat(actual).isEqualTo(ApiUser(user.id, user.name))
      verify(securityContextRepository).save(eq(exchange), any())
    }
  }
}