package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * A credential descriptor identifying a credential that may be used for
 * authentication.
 *
 * @property type The credential type. Always `"public-key"`.
 * @property id Base64URL-encoded credential ID.
 * @property transports Hints about the transports supported by the
 * authenticator (e.g. `"usb"`, `"nfc"`, `"ble"`, `"internal"`).
 */
@Serializable
data class AllowedCredential(
  val type: String,
  val id: String,
  val transports: Set<String>,
)
