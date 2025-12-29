package io.miret.etienne.registre.front.store.state

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
