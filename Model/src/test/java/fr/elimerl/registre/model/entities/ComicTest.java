package fr.elimerl.registre.model.entities;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.Cartoonist;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.ScriptWriter;
import fr.elimerl.registre.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@link Comic}.
 */
public class ComicTest {

    /** Title of the comic under test. */
    private static final String TITLE = "Le démon de minuit";

    /** A random {@code User}. */
    private static final User CREATOR =
	    new User("Etienne", "etienne@email");

    /** This class’ logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(ComicTest.class);

    /** {@code Comic} under test. */
    private Comic comic;

    /**
     * Instantiate the {@code Comic} that is going to be tested.
     */
    @Before
    public void setUp() {
	comic = new Comic(TITLE, CREATOR);
    }

    /**
     * Test the {@link Comic#setCartoonist(Cartoonist)} and
     * {@link Comic#getCartoonist()} methods.
     */
    @Test
    public void cartoonist() {
	logger.info("Cartoonist test.");

	final Cartoonist cartoonist1 = new Cartoonist("Jigounov");
	final Cartoonist cartoonist2 = new Cartoonist("Alain Henriet");

	comic.setCartoonist(cartoonist1);
	assertEquals("Cartoonist wasn’t properly set.",
		cartoonist1, comic.getCartoonist());

	comic.setCartoonist(cartoonist2);
	assertEquals("Cartoonist wasn’t properly updated.",
		cartoonist2, comic.getCartoonist());
    }

    /**
     * Test the {@link Comic#setScriptWriter(ScriptWriter)} and
     * {@link Comic#getScriptWriter()} methods.
     */
    @Test
    public void scriptWriter() {
	logger.info("Script writer test.");

	final ScriptWriter scriptWriter1 = new ScriptWriter("Renard");
	final ScriptWriter scriptWriter2 = new ScriptWriter("Callède");

	comic.setScriptWriter(scriptWriter1);
	assertEquals("Script writer wasn’t properly set.",
		scriptWriter1, comic.getScriptWriter());

	comic.setScriptWriter(scriptWriter2);
	assertEquals("Script writer wasn’t properly updated.",
		scriptWriter2, comic.getScriptWriter());
    }

    /**
     * Test the {@link Comic#setNumber(Integer)} and {@link Comic#getNumber()}
     * methods.
     */
    @Test
    public void number() {
	logger.info("Test du numéro.");

	final Integer number1 = Integer.valueOf(12);
	final Integer number2 = Integer.valueOf(4);

	comic.setNumber(number1);
	assertEquals("Number wasn’t properly set.",
		number1, comic.getNumber());

	comic.setNumber(number2);
	assertEquals("Number wasn’t properly updated.",
		number2, comic.getNumber());
    }

}
