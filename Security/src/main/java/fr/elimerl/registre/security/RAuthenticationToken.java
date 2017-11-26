package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.ContextLoader;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;

public class RAuthenticationToken extends AbstractAuthenticationToken {

  private static final Logger logger =
      LoggerFactory.getLogger (RAuthenticationToken.class);

  private final Long userId;

  private transient volatile User user;

  private final String accessToken;

  /**
   * Creates a token with the supplied array of authorities.
   *
   * @param user
   *          user this token is for.
   * @param authorities
   *          the collection of {@code GrantedAuthority}s for the principal
   *          represented by this authentication object.
   */
  public RAuthenticationToken (
      User user,
      Collection<? extends GrantedAuthority> authorities,
      String accessToken
  ) {
    super (authorities);
    this.userId = user.getId ();
    this.accessToken = accessToken;
    this.user = user;
    setAuthenticated (true);
  }

  @Override
  public Object getCredentials () {
    return accessToken;
  }

  @Override
  public Object getPrincipal () {
    if (user == null) {
      synchronized (this) {
        if (user == null) {
          final EntityManagerFactory emf = ContextLoader
              .getCurrentWebApplicationContext ()
              .getBean (EntityManagerFactory.class);
          user = emf.createEntityManager ().find (User.class, userId);
          logger.debug ("{} was loaded from the database.", user);
        }
      }
    }
    return user;
  }

}
