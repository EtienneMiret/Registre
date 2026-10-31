package io.miret.etienne.registre.back.config

data class WebAuthnConfig(
  val rpId: String,
  val rpName: String,
  val origin: String,
)
