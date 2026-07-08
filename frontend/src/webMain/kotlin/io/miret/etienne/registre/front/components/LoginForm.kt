package io.miret.etienne.registre.front.components

import io.miret.etienne.registre.common.User
import io.miret.etienne.registre.common.passkey.AuthenticationDtoRequest
import io.miret.etienne.registre.common.passkey.AuthenticationOptions
import io.miret.etienne.registre.common.passkey.AuthenticationResponseData
import io.miret.etienne.registre.front.lib.decodeFromBase64Url
import io.miret.etienne.registre.front.lib.encodeToBase64Url
import io.miret.etienne.registre.front.lib.toRequirement
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
import web.authn.AuthenticatorAssertionResponse
import web.authn.PublicKeyCredential
import web.authn.PublicKeyCredentialRequestOptions
import web.credentials.CredentialRequestOptions
import web.credentials.get
import web.html.*
import web.http.*
import web.navigator.navigator

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

  button {
    type = ButtonType.button
    onClick = { _ ->
      MainScope().launch {
        // 1. Fetch authentication challenge from the server
        val challengeResponse = fetch("/api/auth/passkey/authentication/challenge")
        if (!challengeResponse.ok) {
          error = challengeResponse.status
          return@launch
        }
        val options = Json.decodeFromString<AuthenticationOptions>(challengeResponse.text())

        // 2. Invoke the browser WebAuthn API
        val credential = navigator.credentials.get(
          CredentialRequestOptions(
            publicKey = PublicKeyCredentialRequestOptions(
              challenge = decodeFromBase64Url(options.challenge),
              rpId = options.rpId,
              timeout = options.timeout,
              userVerification = options.userVerification.toRequirement(),
            )
          )
        ) as PublicKeyCredential?
        if (credential == null) {
          return@launch
        }

        // 3. Build the request DTO from the browser credential
        val assertionResponse = credential.response
            as AuthenticatorAssertionResponse
        val request = AuthenticationDtoRequest(
          id = credential.id,
          response = AuthenticationResponseData(
            clientDataJSON = encodeToBase64Url(assertionResponse.clientDataJSON),
            authenticatorData = encodeToBase64Url(assertionResponse.authenticatorData),
            signature = encodeToBase64Url(assertionResponse.signature),
            userHandle = assertionResponse.userHandle?.let { encodeToBase64Url(it) },
          ),
        )

        // 4. Submit the credential to the server
        val authResponse = fetch(
          "/api/auth/passkey/authentication",
          RequestInit(
            method = RequestMethod.POST,
            body = BodyInit(Json.encodeToString(request)),
            headers = Headers().apply { set("Content-Type", "application/json") },
          ),
        )
        if (authResponse.ok) {
          val user = Json.decodeFromString<User>(authResponse.text())
          dispatch(usernameLoaded(user.name))
        } else {
          error = authResponse.status
        }
      }
    }
    +"Se connecter avec une clé d'accès"
  }

  error?.let { status ->
    p { +"Erreur $status lors de la connexion ☹️." }
  }
}
