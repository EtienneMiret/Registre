package io.miret.etienne.registre.back

import io.miret.etienne.registre.back.security.model.PasskeyCredential
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.DefaultApplicationArguments
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.index.IndexField
import org.springframework.data.mongodb.core.indexOps
import java.time.Duration

@SpringBootTest
class IndexInitializerTest {

  @ParameterizedTest
  @ValueSource(strings = ["sessions", "passkey_challenges"])
  fun `should create a TTL index on expiresAt`(
    collection: String,
    @Autowired initializer: IndexInitializer,
    @Autowired db: ReactiveMongoTemplate,
  ) {
    initializer.run(DefaultApplicationArguments())

    val indexes = db.indexOps(collection)
      .indexInfo
      .collectList()
      .block()

    assertThat(indexes).anySatisfy {
      assertThat(it.indexFields.map(IndexField::getKey))
        .containsExactly("expiresAt")
      assertThat(it.expireAfter).hasValue(Duration.ZERO)
    }
  }

  @Test
  fun `should create a regular index on userId`(
    @Autowired initializer: IndexInitializer,
    @Autowired db: ReactiveMongoTemplate,
  ) {
    initializer.run(DefaultApplicationArguments())

    val indexes = db.indexOps<PasskeyCredential>()
      .indexInfo
      .collectList()
      .block()

    assertThat(indexes).anySatisfy {
      assertThat(it.indexFields.map(IndexField::getKey))
        .containsExactly("userId")
    }
  }

}
