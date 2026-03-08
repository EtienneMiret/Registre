package io.miret.etienne.registre.back.settings.repositories

import io.miret.etienne.registre.back.settings.model.ReferenceClass
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ReferenceClassRepository : CoroutineCrudRepository<ReferenceClass, String>
