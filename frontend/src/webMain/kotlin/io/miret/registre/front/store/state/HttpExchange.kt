package io.miret.registre.front.store.state

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface HttpExchange<out T> {
  val type: String

  @JsPlainObject
  interface Idle<out T> : HttpExchange<T>

  @JsPlainObject
  interface Pending<out T> : HttpExchange<T>

  @JsPlainObject
  interface Success<out T> : HttpExchange<T> {
    val data: T
  }

  @JsPlainObject
  interface Error<out T> : HttpExchange<T> {
    val status: Short
  }

}

object HttpExchanges {

  object Type {
    const val Idle = "idle"
    const val Pending = "pending"
    const val Success = "success"
    const val Error = "error"
  }

  fun <T> idle(): HttpExchange.Idle<T> = HttpExchange.Idle(Type.Idle)
  fun <T> pending(): HttpExchange.Pending<T> = HttpExchange.Pending(Type.Pending)
  fun <T> success(data: T): HttpExchange.Success<T> = HttpExchange.Success(Type.Success, data)
  fun <T> error(status: Short): HttpExchange.Error<T> = HttpExchange.Error(Type.Error, status)

  @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
  fun <T, R> HttpExchange<T>.fold(
    idle: () -> R,
    pending: () -> R,
    success: (T) -> R,
    error: (Short) -> R,
  ): R = when (type) {
    Type.Idle -> idle()
    Type.Pending -> pending()
    Type.Success -> success((this as HttpExchange.Success<T>).data)
    Type.Error -> error((this as HttpExchange.Error<T>).status)
    else -> throw IllegalStateException("Unknown type: $type")
  }

}
