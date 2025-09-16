pluginManagement {
  plugins {
    val kotlinVersion = "2.2.20"
    kotlin("multiplatform") version kotlinVersion
  }
}

rootProject.name = "Registre"
include("client")
