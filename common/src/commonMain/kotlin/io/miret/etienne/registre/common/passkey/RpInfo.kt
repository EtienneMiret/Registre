package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * Relying party information included in registration options.
 *
 * @property id The relying party identifier, typically the effective domain
 *    (e.g. `"example.com"`).
 * @property name Human-readable name for the relying party, displayed to the
 *    user by the authenticator.
 */
@Serializable
data class RpInfo(val id: String, val name: String)
