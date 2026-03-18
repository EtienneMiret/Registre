package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.back.security.model.User
import io.miret.etienne.registre.back.security.repositories.PasskeyCredentialRepository
import io.miret.etienne.registre.common.passkey.Passkey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.miret.etienne.registre.common.User as ApiUser

@RestController
@RequestMapping("/auth/users")
class UserController(
  private val credentialRepository: PasskeyCredentialRepository,
) {

  @GetMapping("/@me/passkeys")
  fun myPasskeys(@AuthenticationPrincipal user: User): Flow<Passkey> =
    credentialRepository.findAllByUserId(user.id)
      .map { Passkey(
        id = it.id,
        user = ApiUser(user.id, user.name),
        name = it.name,
      ) }

}
