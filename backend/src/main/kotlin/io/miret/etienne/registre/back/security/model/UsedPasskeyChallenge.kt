package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * A signed challenge that was used in a successful passkey registration or
 * login, kept until after it expires so that it cannot be used again.
 *
 * @property id Base64URL-encoded challenge. Serves as the document `_id`.
 * @property deleteAt When the document can be deleted: some time after the
 *   challenge expires, so that a difference between the clocks of the
 *   backend and of MongoDB does not delete it too early.
 */
@Document(collection = "used_passkey_challenges")
data class UsedPasskeyChallenge(
  @Id val id: String,
  val deleteAt: Instant,
)
