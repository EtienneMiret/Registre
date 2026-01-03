package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document(collection = "sessions")
data class Session(
  @Id val id: String = UUID.randomUUID().toString(),
  val userId: String,
  val expiresAt: Instant,
)
