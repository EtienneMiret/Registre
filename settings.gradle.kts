pluginManagement {
  plugins {
    val kotlinVersion = "2.2.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
  }
}

rootProject.name = "Registre"
include("client")
include("server")
