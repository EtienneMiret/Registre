package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * A credential descriptor identifying an existing credential, either allowed
 * for authentication or excluded from registration.
 *
 * Corresponds to the WebAuthn `PublicKeyCredentialDescriptor` dictionary.
 *
 * @property type The credential type. Always `"public-key"`.
 * @property id Base64URL-encoded credential ID.
 * @property transports Hints about the transports supported by the
 * authenticator (e.g. `"usb"`, `"nfc"`, `"ble"`, `"internal"`).
 */
@Serializable
data class CredentialDescriptor(
  val type: String,
  val id: String,
  val transports: Set<String>,
)
