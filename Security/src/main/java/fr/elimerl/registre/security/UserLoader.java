package fr.elimerl.registre.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fr.elimerl.registre.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Service responsible for loading Registre {@link User}s from the database.
 */
@Service
public class UserLoader {

  /**
   * SLF4J logger for this class.
   */
  private static final Logger logger =
      LoggerFactory.getLogger (UserLoader.class);

  /**
   * JPA entity manager.
   */
  @PersistenceContext(name = "Registre")
  private EntityManager em;

  /**
   * Loads from the database the Registre user associated with the given
   * OpenID Connect user.
   *
   * @param userInfo
   *          information to identify a user, certified by an identity
   *          provider.
   * @return the Registre user associated with the given user info, or
   *          {@code null} if there is no such user in the database.
   */
  public User loadUser (OidcUser userInfo) {
    if (userInfo == null
        || userInfo.getEmail() == null
        || !Boolean.TRUE.equals(userInfo.getEmailVerified())) {
      return null;
    }

    final CriteriaBuilder builder = em.getCriteriaBuilder();
    final CriteriaQuery<User> query = builder.createQuery(User.class);
    final Root<User> user = query.from(User.class);
    query.where(builder.equal(user.get("email"), userInfo.getEmail ()));
    try {
      return em.createQuery (query).getSingleResult ();
    } catch (NoResultException e) {
      logger.warn ("{} tried to authenticate, but isn’t registered.",
          userInfo.getEmail (), e);
      return null;
    }
  }

}
