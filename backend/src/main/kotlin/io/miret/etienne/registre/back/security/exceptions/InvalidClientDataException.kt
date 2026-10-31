package io.miret.etienne.registre.back.security.exceptions

class InvalidClientDataException(message: String, cause: Throwable? = null)
  : IllegalArgumentException(message, cause)
