package io.miret.registre.front

import io.miret.registre.front.components.App
import io.miret.registre.front.store.state.HttpExchange
import react.create
import react.dom.client.createRoot
import redux.react.provider
import redux.Action
import web.dom.ElementId
import web.dom.document

fun main() {
  createRoot(document.getElementById(ElementId("root"))!!)
    .render(provider<Action, HttpExchange<String>>().create {
      store = io.miret.registre.front.store.store
      App()
    })
}
