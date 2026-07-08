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
 * @property name Human-readable label chosen by the user to identify this
 *    credential (e.g. `"My iPhone"`, `"YubiKey"`).
 * @property response The authenticator's attestation response.
 */
@Serializable
data class RegistrationDtoRequest(
  val id: String,
  val name: String,
  val response: RegistrationResponseData,
)
