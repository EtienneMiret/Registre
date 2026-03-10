package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.back.security.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.miret.etienne.registre.common.User as ApiUser

@RestController
@RequestMapping("/auth")
class AuthController {

  @GetMapping("/whoami")
  fun whoami(
    @AuthenticationPrincipal user: User,
  ): ApiUser = ApiUser(user.id, user.name)

}
