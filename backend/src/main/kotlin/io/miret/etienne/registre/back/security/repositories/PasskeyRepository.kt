package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.Passkey
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PasskeyRepository : CoroutineCrudRepository<Passkey, String> {
  fun findAllByUserId(userId: String): Flow<Passkey>
}
