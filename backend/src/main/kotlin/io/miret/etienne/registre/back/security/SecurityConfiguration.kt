package io.miret.etienne.registre.back.security

import com.webauthn4j.async.WebAuthnAsyncManager
import com.webauthn4j.converter.util.ObjectConverter
import io.miret.etienne.registre.back.security.services.DbSecurityContextRepository
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableReactiveMethodSecurity
class SecurityConfiguration {

  @Bean
  fun filterChain(
    http: ServerHttpSecurity,
    dbSecurityContextRepository: DbSecurityContextRepository,
  ): SecurityWebFilterChain =
    http {
      authorizeExchange {
        authorize("/auth/login", permitAll)
        authorize("/auth/passkey/authentication/challenge", permitAll)
        authorize("/auth/passkey/authentication", permitAll)
        authorize(anyExchange, hasRole("USER"))
      }
      securityContextRepository = dbSecurityContextRepository
      csrf { disable() }
      exceptionHandling {
        authenticationEntryPoint = { exchange, _ ->
          exchange.response.statusCode = HttpStatus.FORBIDDEN
          mono { return@mono null }
        }
      }
    }

  @Bean
  fun objectConverter(): ObjectConverter = ObjectConverter()

  @Bean
  fun webAuthnAsyncManager(objectConverter: ObjectConverter): WebAuthnAsyncManager =
    WebAuthnAsyncManager.createNonStrictWebAuthnAsyncManager(objectConverter)

}
