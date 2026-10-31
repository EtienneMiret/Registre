package io.miret.etienne.registre.front.lib

import js.buffer.toArrayBuffer
import js.buffer.toByteArray
import kotlin.test.Test
import kotlin.test.assertEquals

class Base64Test {

  private val data = listOf(
    Data("", ""),
    Data("QQ==", "A"),
    Data("MTI=", "12"),
    Data("YWJj", "abc"),
    Data("AA==", byteArrayOf(0)),
    Data("--__", byteArrayOf(
      0xfb.toByte(),
      0xef.toByte(),
      0xff.toByte(),
    )),
  )

  @Test
  fun `should encode to URL base64`() {
    for ((encoded, decoded) in data) {
      val buffer = decoded.toArrayBuffer()

      val actual = encodeToBase64Url(buffer)

      assertEquals(encoded, actual)
    }
  }

  @Test
  fun `should decode from URL base64`() {
    for ((encoded, decoded) in data) {
      val actual = decodeFromBase64Url(encoded)

      val expected = decoded.toList()
      assertEquals(expected, actual.toByteArray().toList())
    }
  }

  data class Data(val encoded: String, val decoded: ByteArray) {
    constructor(encoded: String, decoded: String)
        : this(encoded, decoded.encodeToByteArray())

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || this::class != other::class) return false

      other as Data

      if (encoded != other.encoded) return false
      if (!decoded.contentEquals(other.decoded)) return false

      return true
    }

    override fun hashCode(): Int {
      var result = encoded.hashCode()
      result = 31 * result + decoded.contentHashCode()
      return result
    }
  }

}
