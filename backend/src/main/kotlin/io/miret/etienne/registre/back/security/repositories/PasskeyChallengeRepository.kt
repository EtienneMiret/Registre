package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.PasskeyChallenge
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PasskeyChallengeRepository : CoroutineCrudRepository<PasskeyChallenge, String>
