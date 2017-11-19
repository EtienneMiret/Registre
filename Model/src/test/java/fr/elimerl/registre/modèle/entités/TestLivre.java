package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.Author;
import fr.elimerl.registre.entities.Book;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de test de la classe {@link Book}.
 */
public class TestLivre {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestLivre.class);

    /** Le {@code Book} qui va être testé. */
    private Book livre;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	livre = new Book("L’Assassin royal", null);
    }

    /**
     * Teste les méthodes {@link Book#setAuthor(Author) setAuthor(Author)} et
     * {@link Book#getAuthor() getAuthor()}.
     */
    @Test
    public void auteur() {
	logger.info("Test de l’auteur.");

	final Author auteur1 = new Author("Robin Hobb");
	final Author auteur2 = new Author("Tom Clancy");

	livre.setAuthor(auteur1);
	assertEquals(auteur1, livre.getAuthor());

	livre.setAuthor(auteur2);
	assertEquals(auteur2, livre.getAuthor());
    }

}
