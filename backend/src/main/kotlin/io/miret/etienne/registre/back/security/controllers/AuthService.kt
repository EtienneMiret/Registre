package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.common.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthService {

  @GetMapping("/whoami")
  fun whoami(): User = User("anonymous", "Anonymous")

}
