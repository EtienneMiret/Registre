package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * Test de la classe {@link Session}.
 */
public class SessionTest {

    /** Nom de l’{@link #utilisateur}. */
    private static final String NOM_UTILISATEUR = "Etienne";

    /** Mot de passe de l’{@link #utilisateur}. */
    private static final String MDP_UTILISATEUR = "azerty";

    /** Utilisateur de la session testée. */
    private final Utilisateur utilisateur;

    /**
     * Unique constructeur.
     */
    public SessionTest() {
	utilisateur = new Utilisateur(NOM_UTILISATEUR, MDP_UTILISATEUR);
    }

    /**
     * Teste la méthode {@link Session#getUtilisateur() getUtilisateur()}.
     */
    @Test
    public void testGetUtilisateur() {
	final Session session = new Session(utilisateur, 0);
	assertEquals("Erreur à la récupération de l’utilisateur.",
		utilisateur, session.getUtilisateur());
    }

    /**
     * Teste qu’une session expirée depuis longtemps n’est pas valide.
     */
    @Test
    public void expireAvant() {
	final Session session = new Session(utilisateur, -1000);
	assertFalse("Une session expirée depuis longtemps est valide.",
		session.estValide());
    }

    /**
     * Teste qu’une session qui expire maintenant n’est pas valide.
     */
    @Test
    public void expireMaintenant() {
	final Session session = new Session(utilisateur, 0);
	assertFalse("Une session qui expire maintenant est valide.",
		session.estValide());
    }

    /**
     * Teste qu’une session qui expire dans longtemps est valide.
     */
    @Test
    public void expireAprès() {
	final Session session = new Session(utilisateur, 1000);
	assertTrue("Une session qui expire plus tard n’est pas valide.",
		session.estValide());
    }

    /**
     * Teste les méthodes {@link Session#equals(Object) equals()} et
     * {@link Session#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Session> equalsVerifier =
		EqualsVerifier.forClass(Session.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
