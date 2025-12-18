plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js {
    browser()
  }
}

dependencies {
  commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}")
}
