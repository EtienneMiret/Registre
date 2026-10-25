package io.miret.etienne.registre.back.security

import com.webauthn4j.util.exception.WebAuthnException
import io.miret.etienne.registre.back.security.exceptions.InvalidChallengeException
import io.miret.etienne.registre.back.security.exceptions.InvalidPasskeyException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice("io.miret.etienne.registre.back")
class SecurityControllerAdvice {

  private val logger = LoggerFactory.getLogger(SecurityControllerAdvice::class.java)

  @ExceptionHandler(InvalidChallengeException::class, produces = [MediaType.TEXT_PLAIN_VALUE])
  @ResponseStatus(HttpStatus.FORBIDDEN)
  fun handleInvalidChallenge(e: InvalidChallengeException): String {
    logger.info("Invalid challenge.", e)
    return e.message!!
  }

  @ExceptionHandler(InvalidPasskeyException::class, produces = [MediaType.TEXT_PLAIN_VALUE])
  @ResponseStatus(HttpStatus.FORBIDDEN)
  fun handleInvalidPasskey(e: InvalidPasskeyException): String {
    logger.info("Invalid passkey.", e)
    return e.message!!
  }

  @ExceptionHandler(WebAuthnException::class, produces = [MediaType.TEXT_PLAIN_VALUE])
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun handleWebAuthnException(e: WebAuthnException): String {
    logger.info("Failed to verify browser authentication response.", e)
    return "Invalid authentication response"
  }

}
