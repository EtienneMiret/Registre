package io.miret.etienne.registre.front.components

import io.miret.etienne.registre.common.User
import io.miret.etienne.registre.front.store.actions.usernameLoaded
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.useState
import redux.Action
import redux.react.useDispatch
import web.html.ButtonType
import web.html.InputType
import web.html.submit
import web.html.text
import web.http.*

val LoginForm = FC<Props>("LoginForm") {
  val dispatch = useDispatch<Action>()
  var token by useState("")
  var error by useState<Short?>(null)

  form {
    onSubmit = { event ->
      event.preventDefault()
      MainScope().launch {
        val response = fetch(
          "/api/auth/login",
          RequestInit(method = RequestMethod.POST, body = BodyInit(token)),
        )
        if (response.ok) {
          val user = Json.decodeFromString<User>(response.text())
          dispatch(usernameLoaded(user.name))
        } else {
          error = response.status
        }
      }
    }
    input {
      type = InputType.text
      value = token
      onChange = { event -> token = event.target.value }
      placeholder = "Jeton de connexion"
    }
    button {
      type = ButtonType.submit
      +"Se connecter"
    }
  }

  error?.let { status ->
    p { +"Erreur $status lors de la connexion ☹️." }
  }
}
