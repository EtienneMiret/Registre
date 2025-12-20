package io.miret.registre.back.model

data class Statement(
  val property: String,
  val value: String,
  val qualifiers: List<Qualifier> = emptyList(),
)
