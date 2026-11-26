package fr.elimerl.registre.security;

import java.util.Collection;

import fr.elimerl.registre.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptySet;

/**
 * This service is responsible for loading the user who just authenticated
 * through OpenID Connect.
 * <p>
 * It returns a {@link RegistreUser} if the user is registered in Registre and
 * has some authorities, or a plain {@link OidcUser} with no authorities
 * otherwise.
 */
@Service("oidcUserService")
public class RegistreOidcUserService
    implements OAuth2UserService<OidcUserRequest, OidcUser> {

  @Autowired
  private OidcUserService delegate;

  @Autowired
  private UserLoader userLoader;

  @Autowired
  private AuthoritiesMapper authoritiesMapper;

  @Override
  public OidcUser loadUser(OidcUserRequest request)
      throws OAuth2AuthenticationException {
    OidcUser oidcUser = delegate.loadUser(request);
    User user = userLoader.loadUser(oidcUser);
    Collection<GrantedAuthority> authorities =
        authoritiesMapper.mapAuthorities(user);
    if (authorities.isEmpty()) {
      String nameAttribute = oidcUser.getEmail() != null
          ? StandardClaimNames.EMAIL
          : StandardClaimNames.SUB;
      return new DefaultOidcUser(emptySet(), oidcUser.getIdToken(),
          oidcUser.getUserInfo(), nameAttribute);
    }
    return new RegistreUser(user, authorities, oidcUser);
  }

}
