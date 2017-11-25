package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.mitre.openid.connect.client.OIDCAuthenticationProvider;
import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.model.PendingOIDCAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class RAuthenticationProvider implements AuthenticationProvider {

  @PersistenceContext(name = "Registre")
  private EntityManager em;

  @Resource
  private UserInfoFetcher userInfoFetcher;

  @Resource
  private UserLoader userLoader;

  @Resource
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
        ? null
        : new RAuthenticationToken (user, authorities, token.getAccessTokenValue ());
  }

  @Override
  public boolean supports (Class<?> authentication) {
    return PendingOIDCAuthenticationToken.class.isAssignableFrom (authentication);
  }

}
