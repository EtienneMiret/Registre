package fr.elimerl.registre.model.entities;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.entities.Actor;

/**
 * Test methods from the {@link Named} class on various implementations.
 */
public class NamedTest {

    /** This test class’ logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(NamedTest.class);

    /** Creation time objects name. */
    private static final String CREATION_NAME = "Etienne";

    /** Objects name after update. */
    private static final String[] NAMES =
	{"Grégoire", "Claire", "Quentin", "Eloi", "Blanche", "Thibault" };

    /**
     * Test of the {@link Owner} class.
     */
    @Test
    public void owner() {
	final Owner owner = new Owner(CREATION_NAME);
	testerTout(owner);
    }

    /**
     * Test of the {@link Series} class.
     */
    @Test
    public void series() {
	final Series series = new Series(CREATION_NAME);
	testerTout(series);
    }

    /**
     * Test of the {@link Location} class.
     */
    @Test
    public void location() {
	final Location location = new Location(CREATION_NAME);
	testerTout(location);
    }

    /**
     * Test of the {@link Actor} class.
     */
    @Test
    public void actor() {
	final Actor actor = new Actor(CREATION_NAME);
	testerTout(actor);
    }

    /**
     * Test of the {@link Director} class.
     */
    @Test
    public void director() {
	final Director director = new Director(CREATION_NAME);
	testerTout(director);
    }

    /**
     * Tests of the {@link Composer} class.
     */
    @Test
    public void composer() {
	final Composer composer = new Composer(CREATION_NAME);
	testerTout(composer);
    }

    /**
     * Test of the {@link Cartoonist} class.
     */
    @Test
    public void cartoonist() {
	final Cartoonist cartoonist = new Cartoonist(CREATION_NAME);
	testerTout(cartoonist);
    }

    /**
     * Test of the {@link ScriptWriter} class.
     */
    @Test
    public void scriptWriter() {
	final ScriptWriter scriptWriter = new ScriptWriter(CREATION_NAME);
	testerTout(scriptWriter);
    }

    /**
     * Test of the {@link Author} class.
     */
    @Test
    public void auteurs() {
	final Author author = new Author(CREATION_NAME);
	testerTout(author);
    }

    /**
     * Test the {@link Named#getName()} and {@link Named#setName(String)}
     * methods on the give {@link Named}.
     *
     * @param named
     *          the named under test. Its initial name must be
     *          {@link #CREATION_NAME}.
     */
    private static void testerTout(final Named named) {
	logger.info("Testing {}",
		named.getClass().getSimpleName());
	assertEquals("The initial name wasn’t properly set or get.",
                CREATION_NAME, named.getName());
	for (final String nom : NAMES) {
	    named.setName(nom);
	    assertEquals("The name wasn’t properly updated or get.",
		    nom, named.getName());
	}
    }

}
