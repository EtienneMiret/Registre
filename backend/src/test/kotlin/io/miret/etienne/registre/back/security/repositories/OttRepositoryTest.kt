package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.OneTimeToken
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import reactor.core.publisher.Mono

@SpringBootTest
class OttRepositoryTest {

    @AfterEach
    fun clearDatabase(
        @Autowired db: ReactiveMongoDatabaseFactory
    ) {
        db.mongoDatabase
            .map { it.drop() }
            .flatMap { Mono.from(it) }
            .block()
    }

    @Test
    fun `should acquire only once`(
        @Autowired repository: OttRepository
    ) {
        val token = OneTimeToken("123-456", "admin")
        runBlocking {
            repository.save(token)
        }

        val found = runBlocking {
            repository.consume(token.id)
        }
        val notFound = runBlocking {
            repository.consume(token.id)
        }

        assertThat(found).isEqualTo(token)
        assertThat(notFound).isNull()
    }

}
