package fr.elimerl.registre.security;

import org.mitre.openid.connect.model.UserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Optional;

import static java.util.Collections.emptySet;

/**
 * An {@link org.springframework.security.core.Authentication} for unregistered
 * authenticated users.
 */
public class NullAuthenticationToken extends AbstractAuthenticationToken {

  private final String token;

  private final String principal;

  public NullAuthenticationToken (UserInfo userInfo, String token) {
    super (emptySet ());
    this.token = token;
    this.principal = Optional.ofNullable (userInfo)
        .map (UserInfo::getEmail)
        .orElse (null);
    setAuthenticated (true);
  }

  @Override
  public String getCredentials () {
    return token;
  }

  @Override
  public String getPrincipal () {
    return principal;
  }

}
