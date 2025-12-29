package io.miret.etienne.registre.front.store.state

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface State {
  val username: HttpExchange<String>
}
