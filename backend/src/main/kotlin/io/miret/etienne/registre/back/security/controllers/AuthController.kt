package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.back.security.SessionAuthentication
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.OttRepository
import io.miret.etienne.registre.back.security.repositories.UserRepository
import io.miret.etienne.registre.back.security.services.DbSecurityContextRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import io.miret.etienne.registre.common.User as ApiUser

@RestController
@RequestMapping("/auth")
class AuthController(
  private val ottRepository: OttRepository,
  private val userRepository: UserRepository,
  private val securityContextRepository: DbSecurityContextRepository,
) {

  @GetMapping("/whoami")
  fun whoami(
    @AuthenticationPrincipal user: User,
  ): ApiUser = ApiUser(user.id, user.name)

  @PostMapping("/login")
  suspend fun login(
    @RequestBody token: String,
    exchange: ServerWebExchange,
  ): ApiUser {
    val ott = ottRepository.consume(token)
      ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
    val user = userRepository.findById(ott.userId)
      ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
    val context = SecurityContextImpl(SessionAuthentication(user))
    securityContextRepository.save(exchange, context).awaitSingleOrNull()
    return ApiUser(user.id, user.name)
  }

}
