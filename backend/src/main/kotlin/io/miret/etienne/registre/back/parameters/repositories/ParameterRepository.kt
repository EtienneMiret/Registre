package io.miret.etienne.registre.back.parameters.repositories

import io.miret.etienne.registre.back.parameters.model.Parameter
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ParameterRepository : CoroutineCrudRepository<Parameter, String> {

  /**
   * Insert a new [Parameter] to the database.
   *
   * @throws org.springframework.dao.DuplicateKeyException in case a parameter
   * with the same key already exists.
   */
  suspend fun insert(parameter: Parameter)

}
