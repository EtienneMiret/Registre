package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * The authenticator's attestation response produced during passkey
 * registration.
 *
 * Corresponds to the WebAuthn `AuthenticatorAttestationResponse` interface.
 *
 * @property clientDataJSON Base64URL-encoded JSON containing the challenge,
 *    origin, and type.
 * @property attestationObject Base64URL-encoded CBOR attestation object
 *    containing the authenticator data and attestation statement.
 */
@Serializable
data class RegistrationResponseData(
  val clientDataJSON: String,
  val attestationObject: String,
)
