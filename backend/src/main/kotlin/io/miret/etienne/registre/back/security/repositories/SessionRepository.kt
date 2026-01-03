package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.Session
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface SessionRepository : CoroutineCrudRepository<Session, String>
