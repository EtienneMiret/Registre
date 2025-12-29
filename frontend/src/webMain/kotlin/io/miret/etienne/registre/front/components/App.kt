package io.miret.etienne.registre.front.components

import io.miret.etienne.registre.common.User
import io.miret.etienne.registre.front.store.actions.usernameLoadFailed
import io.miret.etienne.registre.front.store.actions.usernameLoadStarted
import io.miret.etienne.registre.front.store.actions.usernameLoaded
import io.miret.etienne.registre.front.store.state.HttpExchange
import io.miret.etienne.registre.front.store.state.HttpExchanges.fold
import io.miret.etienne.registre.front.store.state.State
import kotlinx.serialization.json.Json
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
  val state = useSelector<State, HttpExchange<String>> { it.username }
  val dispatch = useDispatch<Action>()

  useEffectOnce {
    dispatch(usernameLoadStarted())
    val response = fetch("/api/auth/whoami")
    if (response.ok) {
      val user = Json.decodeFromString<User>(response.text())
      dispatch(usernameLoaded(user.name))
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
