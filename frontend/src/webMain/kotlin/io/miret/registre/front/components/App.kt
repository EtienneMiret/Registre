package io.miret.registre.front.components

import io.miret.registre.front.store.actions.usernameLoadFailed
import io.miret.registre.front.store.actions.usernameLoadStarted
import io.miret.registre.front.store.actions.usernameLoaded
import io.miret.registre.front.store.state.HttpExchange
import io.miret.registre.front.store.state.HttpExchanges.fold
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
    state.fold(
      idle = {
        p {
          +"Connectez-vous pour accéder à Registre"
        }
      },
      pending = {
        p {
          +"Chargement en cours…"
        }
      },
      success = { data ->
        Welcome {
          name = data
        }
      },
      error = { status ->
        p {
          +"Erreur $status lors de la connexion ☹️."
        }
      },
    )
  }
}
