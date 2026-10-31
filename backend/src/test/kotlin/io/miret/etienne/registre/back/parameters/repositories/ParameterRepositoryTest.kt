package io.miret.etienne.registre.back.parameters.repositories

import io.miret.etienne.registre.back.parameters.model.Parameter
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import reactor.core.publisher.Mono

@SpringBootTest
class ParameterRepositoryTest {

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
  fun `insert should insert a parameter`(
    @Autowired repository: ParameterRepository,
  ) {
    runBlocking { repository.insert(Parameter("answer", "42")) }

    val found = runBlocking { repository.findById("answer") }
    assertThat(found).isEqualTo(Parameter("answer", "42"))
  }

  @Test
  fun `insert should not overwrite an existing parameter`(
    @Autowired repository: ParameterRepository,
  ) {
    runBlocking { repository.insert(Parameter("answer", "42")) }

    assertThatThrownBy {
      runBlocking { repository.insert(Parameter("answer", "43")) }
    }.isInstanceOf(DuplicateKeyException::class.java)
    val found = runBlocking { repository.findById("answer") }
    assertThat(found).isEqualTo(Parameter("answer", "42"))
  }

}
