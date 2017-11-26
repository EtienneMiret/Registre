package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.model.PendingOIDCAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * This service is responsible for authenticating user have just been through
 * OpenID Connect. It processes {@link PendingOIDCAuthenticationToken}s, and
 * provides an appropriate {@link RAuthenticationToken} if the user is
 * successfully authenticated.
 */
@Service("authenticationProvider")
public class RAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserInfoFetcher userInfoFetcher;

  @Autowired
  private UserLoader userLoader;

  @Autowired
  private AuthoritiesMapper authoritiesMapper;

  @Override
  public Authentication authenticate (Authentication authentication)
      throws AuthenticationException {
    if (!(authentication instanceof PendingOIDCAuthenticationToken)) {
      return null;
    }

    PendingOIDCAuthenticationToken token =
        (PendingOIDCAuthenticationToken) authentication;

    UserInfo userInfo = userInfoFetcher.loadUserInfo (token);
    User user = userLoader.loadUser (userInfo);
    Collection<GrantedAuthority> authorities =
        authoritiesMapper.mapAuthorities (user);
    return authorities.isEmpty ()
        ? new NullAuthenticationToken (userInfo, token.getAccessTokenValue ())
        : new RAuthenticationToken (user, authorities, token.getAccessTokenValue ());
  }

  @Override
  public boolean supports (Class<?> authentication) {
    return PendingOIDCAuthenticationToken.class.isAssignableFrom (authentication);
  }

}
