plugins {
  kotlin("multiplatform")
}

kotlin {
  js {
    browser {
      commonWebpackConfig {
        outputFileName = "main.js"
      }
    }
    binaries.executable()
  }
}

dependencies {
  commonMainImplementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:2025.9.8"))
  commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react")
  commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
