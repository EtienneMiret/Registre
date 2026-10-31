package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.UsedPasskeyChallenge
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UsedPasskeyChallengeRepository : CoroutineCrudRepository<UsedPasskeyChallenge, String> {

  /**
   * Insert a new [UsedPasskeyChallenge] to the database.
   *
   * @throws org.springframework.dao.DuplicateKeyException in case the
   * challenge was already used.
   */
  suspend fun insert(challenge: UsedPasskeyChallenge)

}
