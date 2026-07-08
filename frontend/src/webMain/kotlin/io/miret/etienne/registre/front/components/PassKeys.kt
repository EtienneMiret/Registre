package io.miret.etienne.registre.front.components

import io.miret.etienne.registre.common.passkey.Passkey
import io.miret.etienne.registre.common.passkey.RegistrationDtoRequest
import io.miret.etienne.registre.common.passkey.RegistrationOptions
import io.miret.etienne.registre.common.passkey.RegistrationResponseData
import io.miret.etienne.registre.front.lib.decodeFromBase64Url
import io.miret.etienne.registre.front.lib.encodeToBase64Url
import io.miret.etienne.registre.front.lib.toRequirement
import js.reflect.unsafeCast
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import web.authn.*
import web.credentials.CredentialCreationOptions
import web.credentials.create
import web.html.*
import web.http.*
import web.navigator.navigator

val PassKeys = FC<Props>("PassKeys") {
  var passkeys by useState<List<Passkey>?>(null)
  var loadError by useState<Short?>(null)
  var registering by useState(false)
  var newPasskeyName by useState("")
  var registrationError by useState<Short?>(null)

  suspend fun loadPasskeys() {
    val response = fetch("/api/auth/users/@me/passkeys")
    if (response.ok) {
      passkeys = Json.decodeFromString(response.text())
    } else {
      loadError = response.status
    }
  }

  useEffectOnce {
    MainScope().launch { loadPasskeys() }
  }

  h2 { +"Clés d’accès" }

  when {
    loadError != null -> p { +"Erreur $loadError lors du chargement des clés d’accès." }
    passkeys == null -> p { +"Chargement…" }
    passkeys!!.isEmpty() -> p { +"Aucune clé d’accès enregistrée." }
    else -> ul {
      passkeys!!.forEach { passkey ->
        li {
          key = Key(passkey.id)
          +"${passkey.name} — créée le ${passkey.createdAt}"
          passkey.lastUsedAt?.let { +" — dernière utilisation le $it" }
        }
      }
    }
  }

  if (!registering) {
    button {
      type = ButtonType.button
      onClick = { registering = true }
      +"Enregistrer une nouvelle clé d’accès"
    }
  } else {
    form {
      onSubmit = { event ->
        event.preventDefault()
        MainScope().launch {
          // 1. Fetch registration challenge
          val challengeResponse = fetch(
            "/api/auth/passkey/registration/challenge",
            RequestInit(method = RequestMethod.POST),
          )
          if (!challengeResponse.ok) {
            registrationError = challengeResponse.status
            return@launch
          }
          val options = Json.decodeFromString<RegistrationOptions>(challengeResponse.text())

          // 2. Invoke the browser WebAuthn API
          val credential = navigator.credentials.create(
            CredentialCreationOptions(
              publicKey = PublicKeyCredentialCreationOptions(
                challenge = decodeFromBase64Url(options.challenge),
                rp = PublicKeyCredentialRpEntity(
                  id = options.rp.id,
                  name = options.rp.name,
                ),
                user = PublicKeyCredentialUserEntity(
                  id = decodeFromBase64Url(options.user.id),
                  name = options.user.name,
                  displayName = options.user.name,
                ),
                pubKeyCredParams = unsafeCast(
                  options.pubKeyCredParams.map { param ->
                    PublicKeyCredentialParameters(
                      type = PublicKeyCredentialType.publicKey,
                      alg = unsafeCast(param.alg.toInt()),
                    )
                  }.toTypedArray()
                ),
                timeout = options.timeout.toInt(),
                attestation = AttestationConveyancePreference.none,
                authenticatorSelection = AuthenticatorSelectionCriteria(
                  residentKey = when (options.authenticatorSelection.residentKey) {
                    "required" -> ResidentKeyRequirement.required
                    "preferred" -> ResidentKeyRequirement.preferred
                    else -> ResidentKeyRequirement.discouraged
                  },
                  requireResidentKey = options.authenticatorSelection.requireResidentKey,
                  userVerification = options.authenticatorSelection.userVerification.toRequirement(),
                ),
              ),
            ),
          ) as? PublicKeyCredential
          if (credential == null) {
            registering = false
            return@launch
          }

          // 3. Build the request DTO from the browser credential
          val attestationResponse = credential.response as AuthenticatorAttestationResponse
          val request = RegistrationDtoRequest(
            id = credential.id,
            name = newPasskeyName,
            response = RegistrationResponseData(
              clientDataJSON = encodeToBase64Url(attestationResponse.clientDataJSON),
              attestationObject = encodeToBase64Url(attestationResponse.attestationObject),
            ),
          )

          // 4. Submit to server
          val registrationResponse = fetch(
            "/api/auth/passkey/registration",
            RequestInit(
              method = RequestMethod.POST,
              body = BodyInit(Json.encodeToString(request)),
              headers = Headers().apply { set("Content-Type", "application/json") },
            ),
          )
          if (registrationResponse.ok) {
            registering = false
            newPasskeyName = ""
            registrationError = null
            loadPasskeys()
          } else {
            registrationError = registrationResponse.status
          }
        }
      }

      input {
        type = InputType.text
        value = newPasskeyName
        onChange = { event -> newPasskeyName = event.target.value }
        placeholder = "Nom de la clé"
        required = true
      }
      button {
        type = ButtonType.submit
        +"Confirmer"
      }
      button {
        type = ButtonType.button
        onClick = { _ ->
          registering = false
          newPasskeyName = ""
          registrationError = null
        }
        +"Annuler"
      }
    }

    registrationError?.let { status ->
      p { +"Erreur $status lors de l’enregistrement ☹️." }
    }
  }
}
