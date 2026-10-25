package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.PasskeyChallenge
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PasskeyChallengeRepository : CoroutineCrudRepository<PasskeyChallenge, String> {

  /**
   * Find the specified challenge and delete it from DB, atomically.
   */
  suspend fun deleteChallengeById(id: String): PasskeyChallenge?

}
