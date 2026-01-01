package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.OneTimeToken
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OttRepository: CoroutineCrudRepository<OneTimeToken, String> {

    @Query(delete = true, value = "{_id: ?0}")
    suspend fun consume(id: String): OneTimeToken?

}
