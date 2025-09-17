plugins {
  val kotlinVersion = "2.2.20"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("multiplatform") version kotlinVersion apply false
}

allprojects {
  group = "io.miret.registre"
  version = "4.0.0-SNAPSHOT"

  repositories {
    mavenCentral()
  }
}
