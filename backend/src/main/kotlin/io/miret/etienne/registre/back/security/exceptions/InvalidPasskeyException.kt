package io.miret.etienne.registre.back.security.exceptions

class InvalidPasskeyException(message: String, cause: Throwable?)
  : IllegalArgumentException(message, cause)
