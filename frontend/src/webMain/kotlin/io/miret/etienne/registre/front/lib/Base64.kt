package io.miret.etienne.registre.front.lib

import js.buffer.ArrayBuffer
import js.typedarrays.Uint8Array

fun encodeToBase64Url(buffer: ArrayBuffer): String = Uint8Array(buffer)
  .toBase64()
  .replace("+", "-")
  .replace("/", "_")

fun decodeFromBase64Url(base64Url: String): ArrayBuffer {
  val s = base64Url.replace("-", "+")
    .replace("_", "/")
  val pad = s.length % 4
  return Uint8Array.fromBase64(s + "=".repeat(4 - pad))
    .buffer
}
