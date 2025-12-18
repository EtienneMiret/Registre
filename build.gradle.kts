plugins {
  `registre-conventions`

  val kotlinVersion = Versions.kotlin
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.js-plain-objects") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  kotlin("multiplatform") version kotlinVersion apply false
}
