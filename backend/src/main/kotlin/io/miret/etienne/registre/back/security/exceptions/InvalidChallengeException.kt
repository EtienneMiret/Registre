package io.miret.etienne.registre.back.security.exceptions

class InvalidChallengeException(
  msg: String,
  cause: Throwable? = null,
) : IllegalStateException(msg, cause)
