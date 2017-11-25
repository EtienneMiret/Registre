package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.mitre.openid.connect.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
   * MITREid user info.
   *
   * @param userInfo
   *          information to identified a user, certified by an identity
   *          provider.
   * @return the Registre user associated with the given user info, or
   *          {@code null} if there is no such user in the database.
   */
  public User loadUser (UserInfo userInfo) {
    if (userInfo == null
        || userInfo.getEmail () == null
        || !userInfo.getEmailVerified ()) {
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
