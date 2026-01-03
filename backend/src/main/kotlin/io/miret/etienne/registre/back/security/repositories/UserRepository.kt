package io.miret.etienne.registre.back.security.repositories

import io.miret.etienne.registre.back.security.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, String>
