package io.miret.etienne.registre.back.wd.repositories

import io.miret.etienne.registre.back.wd.model.Reference
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ReferenceRepository: CoroutineCrudRepository<Reference, String>
