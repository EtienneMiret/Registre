plugins {
  `registre-conventions`

  kotlin("multiplatform")
  kotlin("plugin.js-plain-objects")
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
  commonMainImplementation(project(":common"))
  commonMainImplementation(project(":react-redux"))
}
