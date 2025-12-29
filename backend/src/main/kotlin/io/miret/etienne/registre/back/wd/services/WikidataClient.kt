package io.miret.etienne.registre.back.wd.services

import io.miret.etienne.registre.back.wd.model.Reference
import io.miret.etienne.registre.back.wd.model.Statement
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.wikidata.wdtk.datamodel.interfaces.*
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher
import org.wikidata.wdtk.datamodel.interfaces.Statement as WikidataStatement

@Service
class WikidataClient {

  private val unknown = "[inconnu]"

  /**
   * Supported languages, ordered by preference.
   */
  private val languages = listOf(
    "fr",
    "mul",
    "en",
    "en-us",
    "en-gb",
  )

  private lateinit var wbdf: WikibaseDataFetcher

  @PostConstruct
  fun initialize() {
    wbdf = WikibaseDataFetcher.getWikidataDataFetcher()
    wbdf.filter.languageFilter = languages.toSet()
    wbdf.filter.siteLinkFilter = setOf()
  }

  fun fetchReference(id: Long): Reference? {
    return wbdf.getEntityDocument("Q$id")
      .let { if (it is ItemDocument) it.asReference() else null }
  }

  /**
   * Returns the best available language from the map,
   * or null if none are available.
   */
  private fun Map<String, MonolingualTextValue>.bestLanguage(): String? {
    return languages.firstOrNull { it in this }
      ?.let { this[it] }
      ?.text
  }

  /**
   * Converts a Wikidata [ItemDocument] to a Registre [Reference].
   */
  private fun ItemDocument.asReference(): Reference {
    return Reference(
      id = entityId.id,
      label = labels.bestLanguage() ?: unknown,
      description = descriptions.bestLanguage(),
      properties = allStatements.run {
        val list = mutableListOf<Statement>()
        forEach { stmt ->
          list.add(stmt.asStatement())
        }
        list
      }
    )
  }

  /**
   * Converts a Wikidata [Statement](org.wikidata.wdtk.datamodel.interfaces.Statement)
   * to a Registre [Statement].
   */
  private fun WikidataStatement.asStatement() = Statement(
    mainSnak.propertyId.id,
    mainSnak.accept(object : SnakVisitor<String> {
      override fun visit(snak: ValueSnak): String = snak.value.asString()
      override fun visit(snak: SomeValueSnak): String = unknown
      override fun visit(snak: NoValueSnak): String = "[aucun]"
    }))

  private fun Value.asString(): String = accept(object : ValueVisitor<String> {
    override fun visit(value: EntityIdValue): String = value.id
    override fun visit(value: GlobeCoordinatesValue): String = "${value.latitude}°, ${value.longitude}°"
    override fun visit(value: MonolingualTextValue): String = value.text
    override fun visit(value: QuantityValue): String = value.numericValue.toString()
    override fun visit(value: StringValue): String = value.string
    override fun visit(value: TimeValue): String = "${value.year}-${value.month}-${value.day}"
    override fun visit(value: UnsupportedValue) = unknown
  })

}
