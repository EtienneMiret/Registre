package fr.elimerl.registre.security;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails
	.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

/**
 * Service responsible for authenticating users. It checks that their email
 * address is registered in the database.
 */
public class Authenticator implements UserDetailsService,
	AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    /** Google OpenID identity provider URL. */
    private static final String GOOGLE_OPENID_ENDPOINT =
	    "https://www.google.com/accounts/o8/id";

    /** URL identifying email attributes in the OpenID protocol. */
    private static final String TYPE_EMAIL_OPENID =
	    "http://axschema.org/contact/email";

    /** This class’ SLF4J logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(Authenticator.class);

    /**
     * JPA entity manager. Used to access the database.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    @Override
    public UserDetails loadUserDetails(final OpenIDAuthenticationToken token) {
	if (!token.getIdentityUrl().startsWith(GOOGLE_OPENID_ENDPOINT)) {
	    logger.info("Access denied for: {}.",
		    token.getIdentityUrl());
	    throw new UsernameNotFoundException(
		    "Google is the only ID provider allowed.");
	}
	final Iterator<OpenIDAttribute> attributes =
		token.getAttributes().iterator();
	User user = null;
	while (user == null && attributes.hasNext()) {
	    final OpenIDAttribute attribute = attributes.next();
	    if (attribute.getType().equals(TYPE_EMAIL_OPENID)) {
		final Iterator<String> emails = attribute.getValues()
                        .iterator();
		while (user == null && emails.hasNext()) {
		    final String email = emails.next();
		    try {
			user = loadUser(email);
		    } catch (final NoResultException e) {
			logger.info("{} is unknown.", email);
		    }
		}
	    }
	}
	if (user == null) {
	    throw new UsernameNotFoundException("Unknown user");
	}
	return new UtilisateurSpring(user);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
	final UserDetails result;
	try {
	    result = new UtilisateurSpring(loadUser(username));
	} catch (final NoResultException e) {
	    logger.info("{} is unknown.", username);
	    throw new UsernameNotFoundException(username, e);
	}
	return result;
    }

    /**
     * Load the user with the given email from the database.
     *
     * @param email
     *          email address of the user to load.
     * @return the user with the given email address.
     * @throws NoResultException
     *          if no user has this email in the database.
     */
    private User loadUser(final String email) {
	final CriteriaBuilder builder = em.getCriteriaBuilder();
	final CriteriaQuery<User> query = builder.createQuery(User.class);
	final Root<User> racine = query.from(User.class);
	query.where(builder.equal(racine.get("email"), email));
	return em.createQuery(query).getSingleResult();
    }

}
