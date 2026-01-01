package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tokens")
data class OneTimeToken(
    @Id val id: String,
    var userId: String,
)
