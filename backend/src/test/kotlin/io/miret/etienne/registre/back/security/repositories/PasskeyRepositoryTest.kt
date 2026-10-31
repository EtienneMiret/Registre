package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.Passkey
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
import java.time.Instant

@SpringBootTest
class PasskeyRepositoryTest {

  @AfterEach
  fun clearDatabase(
    @Autowired factory: ReactiveMongoDatabaseFactory,
  ) {
    runBlocking {
      val db = factory.mongoDatabase.awaitSingle()
      Mono.from(db.drop())
        .awaitSingleOrNull()
    }
  }

  @Test
  fun `insert should insert a passkey`(
    @Autowired repository: PasskeyRepository,
  ) {
    val passkey = Passkey(
      id = "123",
      userId = "root",
      name = "YubiKey",
      attestedCredentialDataCbor = byteArrayOf(),
      signCount = 0,
      backupEligible = false,
      createdAt = Instant.parse("2026-10-25T19:25:46Z"),
      lastUsedAt = null,
    )

    runBlocking {
      repository.insert(passkey)
    }

    val found = runBlocking {
      repository.existsById(passkey.id)
    }
    assertThat(found).isTrue()
  }

  @Test
  fun `insert should not insert a passkey when it already exist`(
    @Autowired repository: PasskeyRepository,
  ) {
    val existingPasskey = Passkey(
      id = "123",
      userId = "root",
      name = "YubiKey",
      attestedCredentialDataCbor = byteArrayOf(),
      signCount = 0,
      backupEligible = false,
      createdAt = Instant.parse("2026-10-25T19:25:46Z"),
      lastUsedAt = null,
    )
    val newPasskey = existingPasskey.copy(userId = "me")
    runBlocking {
      repository.save(existingPasskey)
    }

    assertThatThrownBy { runBlocking { repository.insert(newPasskey) } }
      .isInstanceOf(DuplicateKeyException::class.java)
  }

}
