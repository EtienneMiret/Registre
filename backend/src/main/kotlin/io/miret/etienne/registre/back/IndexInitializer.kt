package io.miret.etienne.registre.back

import io.miret.etienne.registre.back.security.model.PasskeyChallenge
import io.miret.etienne.registre.back.security.model.Session
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.index.Index
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Creates, at application startup, the MongoDB TTL indexes that delete
 * expired documents.
 *
 * Note that MongoDB refuses to alter `expireAfterSeconds` on an existing
 * index: changing the value here requires a `collMod` command (or dropping
 * the index) on existing databases.
 */
@Component
class IndexInitializer(
  private val mongoTemplate: ReactiveMongoTemplate,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments): Unit = runBlocking {
    listOf(Session::class.java, PasskeyChallenge::class.java).forEach { type ->
      mongoTemplate.indexOps(type)
        .createIndex(
          Index()
            .on("expiresAt", Sort.Direction.ASC)
            .expire(Duration.ZERO),
        )
        .awaitSingle()
    }
  }

}
