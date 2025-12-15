package io.miret.registre.front.store.state

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface State {
  val username: HttpExchange<String>
}
