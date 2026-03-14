package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * Criteria for authenticator selection during passkey registration.
 *
 * @property residentKey Resident key requirement: `"required"`, `"preferred"`,
 *    or `"discouraged"`.
 * @property requireResidentKey Legacy boolean form of the resident key
 *    requirement, kept for backwards compatibility with older authenticators.
 * @property userVerification User verification requirement.
 */
@Serializable
data class AuthenticatorSelection(
  val residentKey: String,
  val requireResidentKey: Boolean,
  val userVerification: UserVerification,
)
