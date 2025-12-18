plugins {
  kotlin("multiplatform")
  kotlin("plugin.js-plain-objects")
}

kotlin {
  js {
    browser()
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(npm("react-redux", Versions.reactRedux))
        implementation(npm("@reduxjs/toolkit", Versions.reduxToolkit))
      }
    }
  }
}

dependencies {
  commonMainApi(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${Versions.kotlinWrappers}"))
  commonMainApi("org.jetbrains.kotlin-wrappers:kotlin-react")
  commonMainApi("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
