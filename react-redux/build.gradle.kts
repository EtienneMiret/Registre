plugins {
  kotlin("multiplatform")
}

kotlin {
  js {
    browser()
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(npm("react-redux", "9.2.0"))
        implementation(npm("@reduxjs/toolkit", "2.11.1"))
      }
    }
  }
}

dependencies {
  commonMainApi(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:2025.9.8"))
  commonMainApi("org.jetbrains.kotlin-wrappers:kotlin-react")
  commonMainApi("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
