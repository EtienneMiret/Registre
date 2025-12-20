package io.miret.registre.back.repositories

import io.miret.registre.back.model.Reference
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReferenceRepository: ReactiveCrudRepository<Reference, String>
