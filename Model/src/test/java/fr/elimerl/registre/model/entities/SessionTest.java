package fr.elimerl.registre.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fr.elimerl.registre.entities.User;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import fr.elimerl.registre.entities.Session;

/**
 * Test for the {@link Session} class.
 */
public class SessionTest {

    /** {@link #user}’s name. */
    private static final String NOM_UTILISATEUR = "Etienne";

    /** {@link #user}’s email address. */
    private static final String EMAIL_UTILISATEUR = "azerty";

    /** User of the session under test. */
    private final User user;

    /**
     * Single constructor.
     */
    public SessionTest() {
	user = new User(NOM_UTILISATEUR, EMAIL_UTILISATEUR);
    }

    /**
     * Test the {@link Session#getUser() getUser()} method.
     */
    @Test
    public void getUser() {
	final Session session = new Session(user, 0);
	assertEquals("Error while getting the user.",
                user, session.getUser());
    }

    /**
     * Test that a session that expired long ago is not valid.
     */
    @Test
    public void expireBefore() {
	final Session session = new Session(user, -1000);
	assertFalse("An expired session is considered valid.",
		session.estValide());
    }

    /**
     * Test that a session that expires now is not valid.
     */
    @Test
    public void expireNow() {
	final Session session = new Session(user, 0);
	assertFalse("An expired session is considered valid.",
		session.estValide());
    }

    /**
     * Test that a session that expires in the future is valid.
     */
    @Test
    public void expireAfter() {
	final Session session = new Session(user, 1000);
	assertTrue("A session that expires in the future is invalid.",
		session.estValide());
    }

    /**
     * Test the {@link Session#equals(Object)} and {@link Session#hashCode()}
     * methods.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Session> equalsVerifier =
		EqualsVerifier.forClass(Session.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
