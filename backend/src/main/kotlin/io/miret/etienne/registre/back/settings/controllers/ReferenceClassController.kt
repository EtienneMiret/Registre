package io.miret.etienne.registre.back.settings.controllers

import io.miret.etienne.registre.back.settings.model.ReferenceClass
import io.miret.etienne.registre.back.settings.repositories.ReferenceClassRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/settings/reference-classes")
@PreAuthorize("hasRole('ADMIN')")
class ReferenceClassController(
  private val repository: ReferenceClassRepository,
) {

  @GetMapping
  suspend fun list(): Flow<ReferenceClass> =
    repository.findAll()

  @GetMapping("/{id}")
  suspend fun get(@PathVariable id: String): ReferenceClass =
    repository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  suspend fun create(@RequestBody referenceClass: ReferenceClass): ReferenceClass =
    repository.save(referenceClass)

  @PutMapping("/{id}")
  suspend fun update(
    @PathVariable id: String,
    @RequestBody referenceClass: ReferenceClass,
  ): ReferenceClass {
    if (!repository.existsById(id)) throw ResponseStatusException(HttpStatus.NOT_FOUND)
    return repository.save(referenceClass.copy(id = id))
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  suspend fun delete(@PathVariable id: String) {
    if (!repository.existsById(id)) throw ResponseStatusException(HttpStatus.NOT_FOUND)
    repository.deleteById(id)
  }

}
