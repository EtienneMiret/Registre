package io.miret.registre.back.repositories

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoTestConfig {

  @Bean
  fun mongoSettings(): MongoClientSettings = MongoClientSettings.builder()
    .applyConnectionString(ConnectionString("mongodb://localhost:27017/registre-test"))
    .build()

}
