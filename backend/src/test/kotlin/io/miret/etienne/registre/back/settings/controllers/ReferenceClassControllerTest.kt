package io.miret.etienne.registre.back.settings.controllers

import io.miret.etienne.registre.back.settings.model.Filter
import io.miret.etienne.registre.back.settings.model.ReferenceClass
import io.miret.etienne.registre.back.settings.repositories.ReferenceClassRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockitoExtension::class)
class ReferenceClassControllerTest {

  @InjectMocks
  private lateinit var controller: ReferenceClassController

  @Mock
  private lateinit var repository: ReferenceClassRepository

  private val humans = ReferenceClass(
    id = "abc-123",
    name = "Humans",
    filter = listOf(Filter("P31", "Q5")),
    properties = listOf("P1477", "P742"),
  )

  @Nested
  inner class List {

    @Test
    fun `should return all reference classes`() {
      whenever(repository.findAll()).thenReturn(flowOf(humans))

      val actual = runBlocking { controller.list().toList() }

      assertThat(actual).containsExactly(humans)
    }
  }

  @Nested
  inner class Get {

    @Test
    fun `should return reference class by id`() {
      runBlocking { whenever(repository.findById(humans.id)).thenReturn(humans) }

      val actual = runBlocking { controller.get(humans.id) }

      assertThat(actual).isEqualTo(humans)
    }

    @Test
    fun `should throw 404 when id not found`() {
      runBlocking { whenever(repository.findById("unknown")).thenReturn(null) }

      assertThatThrownBy { runBlocking { controller.get("unknown") } }
        .isInstanceOf(ResponseStatusException::class.java)
        .extracting { (it as ResponseStatusException).statusCode }
        .isEqualTo(HttpStatus.NOT_FOUND)
    }
  }

  @Nested
  inner class Create {

    @Test
    fun `should save and return the new reference class`() {
      runBlocking { whenever(repository.save(humans)).thenReturn(humans) }

      val actual = runBlocking { controller.create(humans) }

      assertThat(actual).isEqualTo(humans)
    }
  }

  @Nested
  inner class Update {

    @Test
    fun `should save with path id and return updated reference class`() {
      val incoming = humans.copy(id = "ignored", name = "Updated")
      val expected = incoming.copy(id = humans.id)
      runBlocking {
        whenever(repository.existsById(humans.id)).thenReturn(true)
        whenever(repository.save(expected)).thenReturn(expected)
      }

      val actual = runBlocking { controller.update(humans.id, incoming) }

      assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should throw 404 when id not found`() {
      runBlocking { whenever(repository.existsById("unknown")).thenReturn(false) }

      assertThatThrownBy { runBlocking { controller.update("unknown", humans) } }
        .isInstanceOf(ResponseStatusException::class.java)
        .extracting { (it as ResponseStatusException).statusCode }
        .isEqualTo(HttpStatus.NOT_FOUND)

      runBlocking { verifyNoMoreInteractions(repository) }
    }
  }

  @Nested
  inner class Delete {

    @Test
    fun `should delete reference class by id`() {
      runBlocking {
        whenever(repository.existsById(humans.id)).thenReturn(true)
      }

      runBlocking { controller.delete(humans.id) }

      runBlocking { verify(repository).deleteById(humans.id) }
    }

    @Test
    fun `should throw 404 when id not found`() {
      runBlocking { whenever(repository.existsById("unknown")).thenReturn(false) }

      assertThatThrownBy { runBlocking { controller.delete("unknown") } }
        .isInstanceOf(ResponseStatusException::class.java)
        .extracting { (it as ResponseStatusException).statusCode }
        .isEqualTo(HttpStatus.NOT_FOUND)

      runBlocking { verifyNoMoreInteractions(repository) }
    }
  }
}
