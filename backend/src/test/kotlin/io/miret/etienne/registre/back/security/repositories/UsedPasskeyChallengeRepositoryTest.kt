package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.UsedPasskeyChallenge
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import reactor.core.publisher.Mono
import java.time.Instant

@SpringBootTest
class UsedPasskeyChallengeRepositoryTest {

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
  fun `insert should succeed only once per challenge`(
    @Autowired repository: UsedPasskeyChallengeRepository,
  ) {
    val challenge = UsedPasskeyChallenge(
      id = "123-456-789",
      deleteAt = Instant.parse("2895-10-25T19:54:39Z"),
    )

    val results = runBlocking {
      (1..100).map {
        async { runCatching { repository.insert(challenge) } }
      }.awaitAll()
    }

    assertThat(results.filter { it.isSuccess }).hasSize(1)
    assertThat(results.mapNotNull { it.exceptionOrNull() })
      .allMatch { it is DuplicateKeyException }
  }

}
