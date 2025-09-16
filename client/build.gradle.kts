plugins {
  kotlin("multiplatform") version "2.2.20"
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

repositories {
  mavenCentral()
}

dependencies {
  commonMainImplementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:2025.9.8"))
  commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react")
  commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
}
