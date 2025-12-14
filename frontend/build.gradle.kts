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
  commonMainImplementation(project(":react-redux"))
}
