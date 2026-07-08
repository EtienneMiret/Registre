package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.back.security.SessionAuthentication
import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.services.DbSecurityContextRepository
import io.miret.etienne.registre.back.security.services.PasskeyService
import io.miret.etienne.registre.common.passkey.AuthenticationDtoRequest
import io.miret.etienne.registre.common.passkey.AuthenticationOptions
import io.miret.etienne.registre.common.passkey.RegistrationDtoRequest
import io.miret.etienne.registre.common.passkey.RegistrationOptions
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import io.miret.etienne.registre.common.User as ApiUser

@RestController
@RequestMapping("/auth/passkey")
class PasskeyController(
  private val passkeyService: PasskeyService,
  private val securityContextRepository: DbSecurityContextRepository,
) {

  @PostMapping("/registration/challenge")
  suspend fun registrationChallenge(
    @AuthenticationPrincipal user: User,
  ): RegistrationOptions = passkeyService.generateRegistrationChallenge(user)

  @PostMapping("/registration")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  suspend fun register(
    @AuthenticationPrincipal user: User,
    @RequestBody request: RegistrationDtoRequest,
  ) {
    passkeyService.completeRegistration(user, request)
  }

  @GetMapping("/authentication/challenge")
  suspend fun authenticationChallenge(): AuthenticationOptions =
    passkeyService.generateAuthenticationChallenge()

  @PostMapping("/authentication")
  suspend fun authenticate(
    @RequestBody request: AuthenticationDtoRequest,
    exchange: ServerWebExchange,
  ): ApiUser {
    val user = passkeyService.completeAuthentication(request)
      ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
    val securityContext = SecurityContextImpl(SessionAuthentication(user))
    securityContextRepository.save(exchange, securityContext).awaitSingleOrNull()
    return ApiUser(user.id, user.name)
  }

}
