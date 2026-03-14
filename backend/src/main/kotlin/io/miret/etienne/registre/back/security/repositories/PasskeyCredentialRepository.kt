package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.PasskeyCredential
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PasskeyCredentialRepository : CoroutineCrudRepository<PasskeyCredential, String> {
  fun findAllByUserId(userId: String): Flow<PasskeyCredential>
}
