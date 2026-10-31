package io.miret.etienne.registre.back.config

data class RegistreConfig(
  val mongoConnectionString: String,
  val webauthn: WebAuthnConfig,
)
