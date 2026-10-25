package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.PasskeyChallenge
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
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import reactor.core.publisher.Mono
import java.time.Instant

@SpringBootTest
class PasskeyChallengeRepositoryTest {

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
  fun `deleteChallengeById should delete atomically`(
    @Autowired repository: PasskeyChallengeRepository,
  ) {
    val challenge = PasskeyChallenge(
      id = "123-456-789",
      userId = null,
      expiresAt = Instant.parse("2895-10-25T19:54:39Z"),
    )
    runBlocking { repository.save(challenge) }

    val result = runBlocking {
      (1..100).map {
        async { repository.deleteChallengeById(challenge.id) }
      }.awaitAll()
    }

    assertThat(result.filterNotNull()).hasSize(1)
  }

}
