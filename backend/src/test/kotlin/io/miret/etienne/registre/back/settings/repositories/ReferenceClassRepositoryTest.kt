package io.miret.etienne.registre.back.settings.repositories

import io.miret.etienne.registre.back.settings.model.Filter
import io.miret.etienne.registre.back.settings.model.ReferenceClass
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
class ReferenceClassRepositoryTest {

  private val humans = ReferenceClass(
    id = "abc-123",
    name = "Humans",
    filter = listOf(
      Filter("P31", "Q5"),
    ),
    properties = listOf(
      "P1477",
      "P742",
    ),
  )

  @AfterEach
  fun clearDatabase(@Autowired db: ReactiveMongoDatabaseFactory) {
    db.mongoDatabase
      .map { it.drop() }
      .flatMap { Mono.from(it) }
      .block()
  }

  @Test
  fun `should store and retrieve a reference class`(
    @Autowired repository: ReferenceClassRepository,
  ) {
    val retrieved = runBlocking {
      repository.save(humans)
      repository.findById(humans.id)
    }

    assertThat(retrieved).isEqualTo(humans)
  }

  @Test
  fun `should read a reference class`(
    @Autowired repository: ReferenceClassRepository,
    @Autowired db: ReactiveMongoTemplate,
  ) {
    db.createCollection("reference_classes")
      .map {
        it.insertOne(
          Document(
            mapOf(
              "_id" to humans.id,
              "name" to humans.name,
              "filter" to humans.filter.map { f ->
                Document(mapOf("property" to f.property, "value" to f.value))
              },
              "properties" to humans.properties,
            )
          )
        )
      }
      .flatMap { Mono.from(it) }
      .block()

    val actual = runBlocking {
      repository.findById(humans.id)
    }

    assertThat(actual).isEqualTo(humans)
  }

}
