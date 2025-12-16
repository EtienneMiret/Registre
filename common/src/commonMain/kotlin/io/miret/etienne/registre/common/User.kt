package io.miret.etienne.registre.common

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val name: String)
