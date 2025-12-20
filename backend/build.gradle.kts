plugins {
  `registre-conventions`

  kotlin("jvm")
  kotlin("plugin.spring")
  id("org.springframework.boot") version Versions.springBoot
  id("io.spring.dependency-management") version Versions.springDependencyManagement
}

dependencies {
  implementation(project(":common"))
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("tools.jackson.dataformat:jackson-dataformat-toml:${Versions.jackson}")
  implementation("tools.jackson.module:jackson-module-kotlin:${Versions.jackson}")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
  useJUnitPlatform()
}
