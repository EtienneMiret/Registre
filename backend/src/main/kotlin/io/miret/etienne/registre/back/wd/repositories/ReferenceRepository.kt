package io.miret.etienne.registre.back.wd.repositories

import io.miret.etienne.registre.back.wd.model.Reference
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReferenceRepository: ReactiveCrudRepository<Reference, String>
