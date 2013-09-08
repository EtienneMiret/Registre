package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test de la classe {@link Utilisateur}.
 */
public class UtilisateurTest {

    /** Nom de l’utilisateur dans ces tests. */
    private static final String NOM = "Etienne";

    /** Adresse email de l’utilisateur dans ces tests. */
    private static final String EMAIL = "etienne.miret@ens-lyon.org";

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(UtilisateurTest.class);

    /**
     * Teste la création d’un utilisateur, ainsi que les méthodes
     * {@link Utilisateur#getNom() getNom()} et
     * {@link Utilisateur#vérifierMdp(String) vérifierMdp()}.
     */
    @Test
    public void testCréation() {
	logger.info("Test création.");
	final Utilisateur utilisateur = new Utilisateur(NOM, EMAIL);
	assertEquals("Il y a un problème dans le nom de l’utilisateur.",
		NOM, utilisateur.getNom());
	assertEquals("Il y a un problème dans l’adresse email.",
		EMAIL, utilisateur.getEmail());
    }

    /**
     * Teste les méthodes {@link Utilisateur#equals(Object) equals()} et
     * {@link Utilisateur#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Utilisateur> equalsVerifier =
		EqualsVerifier.forClass(Utilisateur.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
