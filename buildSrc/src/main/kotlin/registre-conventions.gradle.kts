import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure

plugins {
  id("com.palantir.git-version")
}

group = "io.miret.etienne.registre"
version = "4.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val versionDetails: Closure<VersionDetails> by extra
val details = versionDetails()

tasks.withType<ProcessResources>().configureEach(
  ConfigureVersionResourcesAction(
    version.toString(),
    details.gitHashFull,
  )
)
