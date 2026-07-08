package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * A short-lived WebAuthn challenge stored in MongoDB while a passkey ceremony
 * is in progress.
 *
 * One document is created when the server issues registration or authentication
 * options, and deleted (or expired) once the ceremony completes or times out.
 *
 * @property challengeBase64 Base64URL-encoded random challenge that was
 *   sent to the browser. Serves as the document `_id`. Verified against
 *   the value in `clientDataJSON` when the browser submits its response.
 * @property userId MongoDB `_id` of the user this challenge belongs to,
 *   or `null` for an authentication ceremony where the user is not yet
 *   known.
 * @property expiresAt Instant after which this challenge must be
 *   rejected, even if the signature is otherwise valid.
 */
@Document(collection = "passkey_challenges")
data class PasskeyChallenge(
  @Id val challengeBase64: String,
  val userId: String?,
  val expiresAt: Instant,
)
