package io.miret.registre.back.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "references")
data class Reference(
  @Id val id: String,
  val label: String,
  val description: String? = null,
  val properties: List<Statement> = emptyList(),
)
