package io.miret.registre.front.store.state

sealed class HttpExchange<out T> {
  class Idle<out T> : HttpExchange<T>()
  class Pending<out T> : HttpExchange<T>()
  data class Success<out T>(val data: T) : HttpExchange<T>()
  data class Error<out T>(val status: Short) : HttpExchange<T>()
}
