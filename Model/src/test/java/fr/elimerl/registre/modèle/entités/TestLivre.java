package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.Author;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.entities.Livre;

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
     * Teste les méthodes {@link Livre#setAuteur(Author) setAuteur(Author)} et
     * {@link Livre#getAuteur() getAuteur()}.
     */
    @Test
    public void auteur() {
	logger.info("Test de l’auteur.");

	final Author auteur1 = new Author("Robin Hobb");
	final Author auteur2 = new Author("Tom Clancy");

	livre.setAuteur(auteur1);
	assertEquals(auteur1, livre.getAuteur());

	livre.setAuteur(auteur2);
	assertEquals(auteur2, livre.getAuteur());
    }

}
