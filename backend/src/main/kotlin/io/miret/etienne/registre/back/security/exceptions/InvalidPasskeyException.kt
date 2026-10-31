package io.miret.etienne.registre.back.security.exceptions

class InvalidPasskeyException(message: String, cause: Throwable? = null)
  : IllegalArgumentException(message, cause)
