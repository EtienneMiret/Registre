package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User verification requirement for a WebAuthn ceremony, as defined by the
 * WebAuthn specification.
 */
@Serializable
enum class UserVerification {
  @SerialName("required") REQUIRED,
  @SerialName("preferred") PREFERRED,
  @SerialName("discouraged") DISCOURAGED,
}
