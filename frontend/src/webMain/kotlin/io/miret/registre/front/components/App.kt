package io.miret.registre.front.components

import io.miret.registre.front.store.actions.usernameLoadFailed
import io.miret.registre.front.store.actions.usernameLoadStarted
import io.miret.registre.front.store.actions.usernameLoaded
import io.miret.registre.front.store.state.HttpExchange
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import redux.Action
import redux.react.useDispatch
import redux.react.useSelector
import web.http.fetch
import web.http.text

val App = FC<Props>("App") {
  val state = useSelector<HttpExchange<String>, HttpExchange<String>> { it }
  val dispatch = useDispatch<Action>()

  useEffectOnce {
    dispatch(usernameLoadStarted(Unit))
    val response = fetch("/api/auth/whoami")
    if (response.ok) {
      dispatch(usernameLoaded(response.text()))
    } else {
      dispatch(usernameLoadFailed(response.status))
    }
  }

  h1 {
    +"Registre"
  }

  main {
    when (state) {
      is HttpExchange.Idle, is HttpExchange.Pending -> p {
        +"Chargement en cours…"
      }

      is HttpExchange.Success -> Welcome {
        name = state.data
      }

      is HttpExchange.Error -> p {
        +"Erreur ${state.status} lors de la connexion ☹️."
      }
    }
  }
}
