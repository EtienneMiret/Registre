package io.miret.etienne.registre.common.passkey

import kotlinx.serialization.Serializable

/**
 * A public key credential parameter specifying a supported credential type and
 * signing algorithm.
 *
 * @property type The credential type. Always `"public-key"`.
 * @property alg The COSE algorithm identifier (e.g. `-7` for ES256, `-257` for
 *    RS256).
 */
@Serializable
data class PubKeyCredParam(val type: String, val alg: Long)
