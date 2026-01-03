package io.miret.etienne.registre.back.security

import io.miret.etienne.registre.back.security.model.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class SessionAuthentication(
  val user: User,
  private var authenticated: Boolean = true,
) : Authentication {

  override fun getAuthorities(): Collection<GrantedAuthority> =
    listOfNotNull(
      SimpleGrantedAuthority("ROLE_USER"),
      if (user.admin) SimpleGrantedAuthority("ROLE_ADMIN") else null,
    )

  override fun getCredentials() = null

  override fun getDetails() = null

  override fun getPrincipal() = user

  override fun getName() = user.name

  override fun isAuthenticated() = authenticated

  override fun setAuthenticated(isAuthenticated: Boolean) {
    authenticated = isAuthenticated
  }

}
