package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * A registered passkey credential stored in the `passkeys`
 * MongoDB collection.
 *
 * One document is created per successful registration ceremony and looked up
 * by [id] during each authentication ceremony.
 *
 * @property id Base64URL-encoded credential ID, as returned by the
 *   authenticator and used as the MongoDB document key.
 * @property userId ID of the [User] who owns this credential.
 * @property name Human-readable label chosen by the user at registration time
 *   to identify this credential (e.g. `"My iPhone"`, `"YubiKey"`).
 * @property attestedCredentialDataCbor CBOR-serialized
 *   `AttestedCredentialData` from the registration ceremony, containing the
 *   public key used to verify authentication signatures.
 * @property signCount Monotonically increasing signature counter maintained
 *   by the authenticator. Updated on every successful authentication to
 *   detect cloned authenticators.
 * @property backupEligible Whether the credential is eligible for backup
 *   (WebAuthn flag BE). `true` means the private key may be synced across
 *   devices.
 * @property createdAt When the credential was registered.
 * @property lastUsedAt When the credential was last used to authenticate,
 *   or `null` if it has never been used since registration.
 */
@Document(collection = "passkeys")
data class Passkey(
  @Id val id: String,
  val userId: String,
  val name: String,
  val attestedCredentialDataCbor: ByteArray,
  val signCount: Long,
  val backupEligible: Boolean,
  val createdAt: Instant,
  val lastUsedAt: Instant?,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Passkey

    if (signCount != other.signCount) return false
    if (backupEligible != other.backupEligible) return false
    if (id != other.id) return false
    if (userId != other.userId) return false
    if (name != other.name) return false
    if (!attestedCredentialDataCbor.contentEquals(other.attestedCredentialDataCbor)) return false
    if (createdAt != other.createdAt) return false
    if (lastUsedAt != other.lastUsedAt) return false

    return true
  }

  override fun hashCode(): Int {
    var result = signCount.hashCode()
    result = 31 * result + backupEligible.hashCode()
    result = 31 * result + id.hashCode()
    result = 31 * result + userId.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + attestedCredentialDataCbor.contentHashCode()
    result = 31 * result + createdAt.hashCode()
    result = 31 * result + (lastUsedAt?.hashCode() ?: 0)
    return result
  }
}
