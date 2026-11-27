package fr.elimerl.registre.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test for the {@link User} class.
 */
public class UserTest {

    /** Username in those tests. */
    private static final String NAME = "Etienne";

    /** User email address in those tests. */
    private static final String EMAIL = "etienne.miret@ens-lyon.org";

    /** Logger for this class. */
    private static final Logger logger =
	    LoggerFactory.getLogger(UserTest.class);

    /**
     * Test user creation, and the {@link User#getName()} and
     * {@link User#getEmail()} methods.
     */
    @Test
    public void creationTest() {
	logger.info("Test création.");
	final User utilisateur = new User(NAME, EMAIL);
	assertEquals("The username is wrong.",
                NAME, utilisateur.getName());
	assertEquals("The user email is wrong.",
		EMAIL, utilisateur.getEmail());
    }

    /**
     * Test the {@link User#equals(Object)} and {@link User#hashCode()} methods.
     */
    @Test
    public void equalsAndHashCode() {
      EqualsVerifier.forClass(User.class)
          .suppress(Warning.STRICT_INHERITANCE)
          .withOnlyTheseFields("name")
          .verify();
    }

}
