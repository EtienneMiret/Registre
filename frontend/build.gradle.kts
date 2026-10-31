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
      testTask {
        enabled = false
      }
    }
    nodejs()
    binaries.executable()
  }
}

dependencies {
  commonMainImplementation(project(":common"))
  commonMainImplementation(project(":react-redux"))

  commonTestImplementation(kotlin("test"))
}
