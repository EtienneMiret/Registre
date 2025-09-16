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
  commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react:2025.9.8-19.1.1")
  commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:2025.9.8-19.1.1")
}
