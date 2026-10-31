package io.miret.etienne.registre.front.lib

import io.miret.etienne.registre.common.passkey.UserVerification
import web.authn.UserVerificationRequirement
import web.authn.discouraged
import web.authn.preferred
import web.authn.required

fun UserVerification.toRequirement(): UserVerificationRequirement = when (this) {
  UserVerification.REQUIRED -> UserVerificationRequirement.required
  UserVerification.PREFERRED -> UserVerificationRequirement.preferred
  UserVerification.DISCOURAGED -> UserVerificationRequirement.discouraged
}
