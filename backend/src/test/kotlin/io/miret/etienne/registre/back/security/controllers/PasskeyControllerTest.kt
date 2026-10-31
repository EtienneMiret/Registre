package io.miret.etienne.registre.back.security.controllers

import io.miret.etienne.registre.back.parameters.model.Parameter
import io.miret.etienne.registre.back.security.model.UsedPasskeyChallenge
import io.miret.etienne.registre.back.security.services.ChallengeSigner
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.count
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@SpringBootTest
class PasskeyControllerTest {

  private lateinit var client: WebTestClient

  @BeforeEach
  fun setUp(@Autowired context: ApplicationContext) {
    client = WebTestClient.bindToApplicationContext(context).build()
  }

  @AfterEach
  fun clearDatabase(
    @Autowired factory: ReactiveMongoDatabaseFactory,
  ) {
    runBlocking {
      val db = factory.mongoDatabase.awaitSingle()
      Mono.from(db.drop()).awaitSingleOrNull()
    }
  }

  @Test
  fun `authentication challenge should be available anonymously and not cached`() {
    client.get().uri("/auth/passkey/authentication/challenge")
      .exchange()
      .expectStatus().isOk
      .expectHeader().value("Cache-Control") {
        assertThat(it).contains("no-store")
      }
      .expectBody()
      .jsonPath("$.challenge").isNotEmpty
  }

  @Test
  fun `authentication challenge should only store the signing key`(
    @Autowired mongoTemplate: ReactiveMongoTemplate,
  ) {
    repeat(10) {
      client.get().uri("/auth/passkey/authentication/challenge")
        .exchange()
        .expectStatus().isOk
    }

    runBlocking {
      assertThat(mongoTemplate.count<UsedPasskeyChallenge>(Query()).awaitSingle()).isZero()
      val parameters = mongoTemplate.findAll(Parameter::class.java).collectList().awaitSingle()
      assertThat(parameters.map { it.key }).containsExactly(ChallengeSigner.KEY_PARAMETER)
    }
  }

}
