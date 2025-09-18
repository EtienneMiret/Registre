package io.miret.registre.client

import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import react.useState
import web.dom.ElementId
import web.dom.document
import web.http.fetch
import web.http.text

fun main() {
  createRoot(document.getElementById(ElementId("root"))!!)
    .render(App.create())
}

val App = FC<Props>("App") {
  val (user, setUser) = useState("<NONE>")
  val (status, setStatus) = useState(HttpStatus.INITIAL)

  useEffectOnce {
    setStatus(HttpStatus.LOADING)
    val response = fetch("/api/auth/whoami")
    if (response.ok) {
      setUser(response.text())
      setStatus(HttpStatus.SUCCESS)
    } else {
      setStatus(HttpStatus.ERROR)
    }
  }

  h1 {
    +"Registre"
  }

  main {
    when (status) {
      HttpStatus.INITIAL, HttpStatus.LOADING -> p {
        +"Chargement en cours…"
      }

      HttpStatus.SUCCESS -> Welcome {
        name = user
      }

      HttpStatus.ERROR -> p {
        +"Erreur lors de la connexion ☹️."
      }
    }
  }
}
