package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * The passkey credential submitted by the browser after an authentication
 * ceremony.
 *
 * Corresponds to the WebAuthn `PublicKeyCredential` object returned by
 * `navigator.credentials.get()`.
 *
 * @property id Base64URL-encoded credential ID.
 * @property response The authenticator's assertion response.
 */
@Serializable
data class AuthenticationDtoRequest(
  val id: String,
  val response: AuthenticationResponseData,
)
