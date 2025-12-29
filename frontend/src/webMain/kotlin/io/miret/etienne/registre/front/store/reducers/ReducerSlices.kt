package io.miret.etienne.registre.front.store.reducers

import io.miret.etienne.registre.front.store.state.HttpExchange
import kotlinx.js.JsPlainObject
import redux.Action
import redux.Reducer

@JsPlainObject
external interface ReducerSlices {
  val username: Reducer<HttpExchange<String>, Action>
}
