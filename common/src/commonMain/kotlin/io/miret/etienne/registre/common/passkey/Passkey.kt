package io.miret.etienne.registre.common.passkey

import io.miret.etienne.registre.common.User
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Passkey(
  val id: String,
  val user: User,
  val name: String,
  val createdAt: Instant,
  val lastUsedAt: Instant?,
)
