package io.miret.etienne.registre.back.wd.model

data class Statement(
  val property: String,
  val value: String,
  val qualifiers: List<Qualifier> = emptyList(),
)
