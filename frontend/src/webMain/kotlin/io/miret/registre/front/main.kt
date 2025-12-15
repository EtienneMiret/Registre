package io.miret.registre.front

import io.miret.registre.front.components.App
import io.miret.registre.front.store.registreStore
import io.miret.registre.front.store.state.State
import react.create
import react.dom.client.createRoot
import redux.Action
import redux.react.provider
import web.dom.ElementId
import web.dom.document

fun main() {
  createRoot(document.getElementById(ElementId("root"))!!)
    .render(provider<Action, State>().create {
      store = registreStore
      App()
    })
}
