package io.miret.etienne.registre.common.passkey

import io.miret.etienne.registre.common.User
import kotlinx.serialization.Serializable

@Serializable
data class Passkey(
  val id: String,
  val user: User,
  val name: String,
)
