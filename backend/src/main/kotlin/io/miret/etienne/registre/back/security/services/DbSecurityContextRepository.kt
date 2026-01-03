package io.miret.etienne.registre.back.security.services

import io.miret.etienne.registre.back.security.SessionAuthentication
import io.miret.etienne.registre.back.security.model.Session
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.SessionRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseCookie
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Clock
import java.time.Duration
import java.time.Instant

@Service
class DbSecurityContextRepository : ServerSecurityContextRepository {

  private val cookieName = "session"

  private val sessionDuration = Duration.ofDays(14)

  private val sessionRepository: SessionRepository

  private val userRepository: UserRepository

  private val clock: Clock

  constructor(
    sessionRepository: SessionRepository,
    userRepository: UserRepository,
    clock: Clock,
  ) {
    this.sessionRepository = sessionRepository
    this.userRepository = userRepository
    this.clock = clock
  }

  override fun save(
    exchange: ServerWebExchange,
    context: SecurityContext?,
  ): Mono<Void> = mono {
    val sessionId = context?.authentication?.run {
      if (!isAuthenticated) {
        return@mono null
      }
      principal.let {
        if (it !is User) {
          return@mono null
        }
        val session = Session(
          userId = it.id,
          expiresAt = Instant.now(clock).plus(sessionDuration),
        )
        sessionRepository.save(session)
        return@run session.id
      }
    } ?: return@mono null

    val cookie = ResponseCookie.from(cookieName, sessionId)
      .secure(true)
      .httpOnly(true)
      .maxAge(sessionDuration)
      .sameSite("Strict")
      .build()
    exchange.response.addCookie(cookie)

    return@mono null
  }

  override fun load(
    exchange: ServerWebExchange,
  ): Mono<SecurityContext> = mono {
    val sessionId = exchange
      .request
      .cookies[cookieName]
      ?.firstOrNull()
      ?.value
      ?: return@mono null
    val session = sessionRepository.findById(sessionId)
      ?: return@mono null
    if (session.expiresAt < Instant.now(clock)) {
      return@mono null
    }
    val user = userRepository.findById(session.userId)
      ?: return@mono null
    val authentication = SessionAuthentication(user)
    return@mono SecurityContextImpl(authentication)
  }

}
