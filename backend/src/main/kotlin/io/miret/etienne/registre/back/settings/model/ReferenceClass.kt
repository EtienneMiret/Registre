package io.miret.etienne.registre.back.settings.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "reference_classes")
data class ReferenceClass(

  @Id val id: String = UUID.randomUUID().toString(),

  val name: String,

  /** References from this class must match all of these filters. */
  val filter: List<Filter>,

  /**
   * Properties of the WikiData model which are relevant to this class.
   * Set of Wikidata property IDs (e.g., Q123) in display order. */
  val properties: List<String> = emptyList(),

)
