package io.miret.etienne.registre.back.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "passkey_credentials")
data class PasskeyCredential(
  @Id val id: String,
  val userId: String,
  val attestedCredentialDataCbor: ByteArray,
  val signCount: Long,
  val transports: Set<String>,
  val backupEligible: Boolean,
  val backupState: Boolean,
)
