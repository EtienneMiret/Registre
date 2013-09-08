package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de test de la classe {@link Livre}.
 */
public class TestLivre {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestLivre.class);

    /** Le {@code Livre} qui va être testé. */
    private Livre livre;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	livre = new Livre("L’Assassin royal", null);
    }

    /**
     * Teste les méthodes {@link Livre#setAuteur(Auteur) setAuteur(Auteur)} et
     * {@link Livre#getAuteur() getAuteur()}.
     */
    @Test
    public void auteur() {
	logger.info("Test de l’auteur.");

	final Auteur auteur1 = new Auteur("Robin Hobb");
	final Auteur auteur2 = new Auteur("Tom Clancy");

	livre.setAuteur(auteur1);
	assertEquals(auteur1, livre.getAuteur());

	livre.setAuteur(auteur2);
	assertEquals(auteur2, livre.getAuteur());
    }

}
