package io.miret.registre.back.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthService {

  @GetMapping("/whoami")
  fun whoami() = "anonymous"

}
