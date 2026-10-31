package io.miret.etienne.registre.back.parameters.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * A server-side parameter, such as a generated secret, stored as a key-value
 * pair in MongoDB.
 *
 * @property key Name of the parameter. Serves as the document `_id`.
 * @property value Value of the parameter.
 */
@Document(collection = "parameters")
data class Parameter(
  @Id val key: String,
  val value: String,
)
