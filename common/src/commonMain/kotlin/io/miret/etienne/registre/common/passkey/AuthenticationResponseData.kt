package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * The authenticator's assertion response produced during passkey
 * authentication.
 *
 * Corresponds to the WebAuthn `AuthenticatorAssertionResponse` interface.
 *
 * @property clientDataJSON Base64URL-encoded JSON containing the challenge,
 *    origin, and type.
 * @property authenticatorData Base64URL-encoded authenticator data including
 *    the RP ID hash, flags, and signature counter.
 * @property signature Base64URL-encoded signature over the authenticator data
 *    and client data hash, verifiable with the credential's public key.
 * @property userHandle Base64URL-encoded user handle returned by the
 *    authenticator, or `null` if the authenticator did not provide one.
 */
@Serializable
data class AuthenticationResponseData(
  val clientDataJSON: String,
  val authenticatorData: String,
  val signature: String,
  val userHandle: String?,
)
