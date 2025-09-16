package io.miret.registre.client

import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main
import web.dom.ElementId
import web.dom.document

fun main() {
  createRoot(document.getElementById(ElementId("root"))!!)
    .render(App.create())
}

val App = FC<Props>("App") {
  h1 {
    +"Registre"
  }

  main {
    Welcome {
      name = "World"
    }
  }
}
