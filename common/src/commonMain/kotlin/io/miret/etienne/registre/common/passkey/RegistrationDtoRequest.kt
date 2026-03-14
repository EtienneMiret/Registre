package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * The passkey credential submitted by the browser after a registration
 * ceremony.
 *
 * Corresponds to the WebAuthn `PublicKeyCredential` object returned by
 * `navigator.credentials.create()`.
 *
 * @property id Base64URL-encoded credential ID.
 * @property response The authenticator's attestation response.
 * @property transports Hints about the transports supported by the
 *    authenticator (e.g. `"usb"`, `"nfc"`, `"ble"`, `"internal"`), or `null` if
 *    not reported.
 */
@Serializable
data class RegistrationDtoRequest(
  val id: String,
  val response: RegistrationResponseData,
  val transports: Set<String>?,
)
