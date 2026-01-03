package io.miret.etienne.registre.back.security

import io.miret.etienne.registre.back.security.services.DbSecurityContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfiguration {

  @Bean
  fun filterChain(
    http: ServerHttpSecurity,
    dbSecurityContextRepository: DbSecurityContextRepository,
  ): SecurityWebFilterChain =
    http {
      authorizeExchange {
        authorize(anyExchange, hasRole("USER"))
      }
      securityContextRepository = dbSecurityContextRepository
    }

}
