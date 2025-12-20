package io.miret.registre.back

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import tools.jackson.dataformat.toml.TomlMapper
import tools.jackson.module.kotlin.kotlinModule
import java.net.URI
import java.net.URL
import java.nio.file.Paths

@SpringBootApplication(scanBasePackages = ["io.miret.registre.back"])
class Application {

  @Bean
  fun tomlMapper(): TomlMapper = TomlMapper.builder()
    .addModule(kotlinModule())
    .build()

  @Bean
  fun configUrl(): URL =
    System.getenv("REGISTRE_CONFIG_URL")?.let { URI(it).toURL() }
      ?: Application::class.java.getResource("registre.toml")
      ?: Paths.get("registre.toml").toUri().toURL()

  @Bean
  fun registreConfig(
    tomlMapper: TomlMapper,
    configUrl: URL
  ): RegistreConfig = configUrl.openStream().use {
    tomlMapper.readValue(it, RegistreConfig::class.java)
  }

  @Bean
  fun mongoSettings(config: RegistreConfig): MongoClientSettings =
    MongoClientSettings.builder()
      .applyConnectionString(ConnectionString(config.mongoConnectionString))
      .build()

}

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
