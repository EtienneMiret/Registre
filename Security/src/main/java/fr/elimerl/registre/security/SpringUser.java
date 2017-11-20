package fr.elimerl.registre.security;

import java.util.Collection;

import javax.persistence.EntityManagerFactory;

import fr.elimerl.registre.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.context.ContextLoader;

/**
 * Models a Registre user in the Spring framework.
 */
public class SpringUser
        extends org.springframework.security.core.userdetails.User {

    /** Version number used for serialisation. */
    private static final long serialVersionUID = 8681631417262947043L;

    /** SLF4J logger for this class. */
    private static final Logger logger =
	    LoggerFactory.getLogger(SpringUser.class);

    /**
     * List of roles granted to all users. In this version, there is only one
     * role and it is granted to all users.
     */
    private static final Collection<GrantedAuthority> ROLES =
	    AuthorityUtils.createAuthorityList("ROLE_UTILISATEUR");

    /**
     * {@link #user}’s id.
     */
    private final Long id;

    /**
     * The database user matching this Spring user.
     */
    private transient User user;

    /**
     * Create a Spring user from a database user.
     * @param user
     *          database user to link to this spring user.
     */
    public SpringUser(final User user) {
	super(user.getEmail(), "****", ROLES);
	this.id = user.getId();
	this.user = user;
    }

    /**
     * Returns the database user linked to this Spring user.
     *
     * @return the database user linked to this Spring user.
     */
    public User getUser() {
	if (user == null) {
	    loadUser();
	}
	return user;
    }

    /**
     * Fetch {@link #user} from the database if it is {@code null}.
     */
    private synchronized void loadUser() {
	if (user == null) {
	    final EntityManagerFactory emf = ContextLoader
		    .getCurrentWebApplicationContext()
		    .getBean("usineGestionnairesEntités",
			    EntityManagerFactory.class);
	    user = emf.createEntityManager().find(User.class, id);
	    logger.debug("{} was loaded from the database.", user);
	}
    }

}
