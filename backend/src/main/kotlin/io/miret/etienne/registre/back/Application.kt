package io.miret.etienne.registre.back

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import tools.jackson.dataformat.toml.TomlMapper
import tools.jackson.module.kotlin.kotlinModule
import java.net.URI
import java.net.URL
import java.time.Clock

@SpringBootApplication(scanBasePackages = ["io.miret.etienne.registre.back"])
class Application {

  @Bean
  fun tomlMapper(): TomlMapper = TomlMapper.builder()
    .addModule(kotlinModule())
    .build()

  @Bean
  fun configUrl(): URL =
    System.getenv("REGISTRE_CONFIG_URL")?.let { URI(it).toURL() }
      ?: Application::class.java.getResource("registre-test.toml")
      ?: Application::class.java.getResource("registre.toml")

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
      .applicationName("Registre/${Version.full}")
      .build()

  @Bean
  fun mongoDbFactory(
    config: RegistreConfig,
    mongoClient: MongoClient,
  ): ReactiveMongoDatabaseFactory {
    val database = ConnectionString(config.mongoConnectionString).database
      ?: "registre"
    return ReactiveMongoDatabaseFactory.create(mongoClient, database)
  }

  @Bean
  fun clock(): Clock = Clock.systemUTC()

}

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
