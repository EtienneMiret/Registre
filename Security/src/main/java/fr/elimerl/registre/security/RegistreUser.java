package fr.elimerl.registre.security;

import java.io.Serial;
import java.util.Collection;

import jakarta.persistence.EntityManagerFactory;

import fr.elimerl.registre.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.context.ContextLoader;

/**
 * An OpenID Connect user who is also a registered Registre {@link User}.
 * <p>
 * Only the id of the Registre user is serialized along with this principal
 * (e.g. when the HTTP session is persisted). The user itself is reloaded from
 * the database on demand.
 */
public class RegistreUser extends DefaultOidcUser {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final Logger logger =
      LoggerFactory.getLogger(RegistreUser.class);

  private final Long userId;

  private transient volatile User user;

  /**
   * Creates a new principal.
   *
   * @param user
   *     the Registre user.
   * @param authorities
   *     the authorities granted to {@code user}.
   * @param oidcUser
   *     the OpenID Connect user {@code user} authenticated as.
   */
  public RegistreUser(
      User user,
      Collection<? extends GrantedAuthority> authorities,
      OidcUser oidcUser
  ) {
    super(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), IdTokenClaimNames.SUB);
    this.userId = user.getId();
    this.user = user;
  }

  /**
   * Returns the Registre user this principal is for.
   *
   * @return the Registre user this principal is for.
   */
  public User getUser() {
    if (user == null) {
      synchronized (this) {
        if (user == null) {
          final EntityManagerFactory emf = ContextLoader
              .getCurrentWebApplicationContext()
              .getBean(EntityManagerFactory.class);
          try (var em = emf.createEntityManager()) {
            user = em.find(User.class, userId);
          }
          logger.debug("{} was loaded from the database.", user);
        }
      }
    }
    return user;
  }

  @Override
  public String getName() {
    return getUser().getName();
  }

}
