package io.miret.registre.front.components

import react.FC
import react.Props

external interface WelcomeProps : Props {
  var name: String
}

val Welcome = FC<WelcomeProps>("Welcome") { props ->
  + "Hello ${props.name}!"
}
