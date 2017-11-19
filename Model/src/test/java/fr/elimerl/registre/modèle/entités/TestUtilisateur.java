package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.User;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test de la classe {@link User}.
 */
public class TestUtilisateur {

    /** Nom de l’utilisateur dans ces tests. */
    private static final String NOM = "Etienne";

    /** Adresse email de l’utilisateur dans ces tests. */
    private static final String EMAIL = "etienne.miret@ens-lyon.org";

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestUtilisateur.class);

    /**
     * Teste la création d’un utilisateur, ainsi que les méthodes
     * {@link User#getName() getName()} et
     * {@link User#vérifierMdp(String) vérifierMdp()}.
     */
    @Test
    public void testCréation() {
	logger.info("Test création.");
	final User utilisateur = new User(NOM, EMAIL);
	assertEquals("Il y a un problème dans le nom de l’utilisateur.",
		NOM, utilisateur.getName());
	assertEquals("Il y a un problème dans l’adresse email.",
		EMAIL, utilisateur.getEmail());
    }

    /**
     * Teste les méthodes {@link User#equals(Object) equals()} et
     * {@link User#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<User> equalsVerifier =
		EqualsVerifier.forClass(User.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
