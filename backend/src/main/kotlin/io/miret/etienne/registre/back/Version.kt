package io.miret.etienne.registre.back

import java.util.*

object Version {
  private val props = javaClass.getResourceAsStream("version.properties")
    .use {
      Properties().apply { load(it) }
    }

  val raw: String = props.getProperty("version")
  val gitHash: String = props.getProperty("gitHash")
  val full: String = raw.replace("SNAPSHOT", gitHash)
}
