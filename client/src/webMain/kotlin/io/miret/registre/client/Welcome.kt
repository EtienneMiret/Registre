package io.miret.registre.client

import react.FC
import react.Props

external interface WelcomeProps : Props {
  var name: String
}

val Welcome = FC<WelcomeProps>("Welcome") { props ->
  + "Hello ${props.name}!"
}
