package io.miret.etienne.registre.back.wd.repositories

import io.miret.etienne.registre.back.wd.model.Qualifier
import io.miret.etienne.registre.back.wd.model.Reference
import io.miret.etienne.registre.back.wd.model.Statement
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.core.publisher.Mono

@SpringBootTest
class ReferenceRepositoryTest {

  private val lotr = Reference(
    "http://www.wikidata.org/entity/Q164963",
    "The Lord of the Rings: The Two Towers",
    "2002 film by Peter Jackson",
    listOf(
      Statement(
        "http://www.wikidata.org/entity/P31",
        "http://www.wikidata.org/entity/Q11424",
      ),
      Statement(
        "http://www.wikidata.org/entity/P179",
        "http://www.wikidata.org/entity/Q190214",
        listOf(
          Qualifier(
            "http://www.wikidata.org/entity/P1545",
            "2"
          )
        )
      ),
      Statement(
        "http://www.wikidata.org/entity/P179",
        "http://www.wikidata.org/entity/Q26214973",
        listOf(
          Qualifier(
            "http://www.wikidata.org/entity/P1545",
            "2"
          )
        )
      )
    )
  )

  @AfterEach
  fun clearDatabase(@Autowired db: ReactiveMongoDatabaseFactory) {
    db.mongoDatabase
      .map { it.drop() }
      .flatMap { Mono.from(it) }
      .block()
  }

  @Test
  fun `should store and retrieve references`(
    @Autowired repository: ReferenceRepository,
  ) {
    val retrieved = runBlocking {
      repository.save(lotr)
      repository.findById(lotr.id)
    }

    assertThat(retrieved).isEqualTo(lotr)
  }

  @Test
  fun `should read a reference`(
    @Autowired repository: ReferenceRepository,
    @Autowired db: ReactiveMongoTemplate,
  ) {
    db.createCollection("references")
      .map {
        it.insertOne(
          Document(
            mapOf(
              "_id" to lotr.id,
              "label" to lotr.label,
              "description" to lotr.description,
              "properties" to lotr.properties.map { statement ->
                Document(
                  mapOf(
                    "property" to statement.property,
                    "value" to statement.value,
                    "qualifiers" to statement.qualifiers.map { qualifier ->
                      Document(
                        mapOf(
                          "property" to qualifier.property,
                          "value" to qualifier.value
                        )
                      )
                    }
                  )
                )
              }
            )
          )
        )
      }
      .flatMap { Mono.from(it) }
      .block()

    val actual = runBlocking {
      repository.findById(lotr.id)
    }

    assertThat(actual).isEqualTo(lotr)
  }

}
