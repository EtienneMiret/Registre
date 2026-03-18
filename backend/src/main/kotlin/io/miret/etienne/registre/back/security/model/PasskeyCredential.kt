package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * A registered passkey credential stored in the `passkey_credentials`
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
 */
@Document(collection = "passkey_credentials")
data class PasskeyCredential(
  @Id val id: String,
  val userId: String,
  val name: String,
  val attestedCredentialDataCbor: ByteArray,
  val signCount: Long,
  val backupEligible: Boolean,
)
