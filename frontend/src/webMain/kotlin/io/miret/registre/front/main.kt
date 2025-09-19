package io.miret.registre.front

import io.miret.registre.front.components.App
import react.create
import react.dom.client.createRoot
import web.dom.ElementId
import web.dom.document

fun main() {
  createRoot(document.getElementById(ElementId("root"))!!)
    .render(App.create())
}
