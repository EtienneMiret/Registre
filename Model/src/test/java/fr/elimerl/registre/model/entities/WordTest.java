package fr.elimerl.registre.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import fr.elimerl.registre.entities.Word;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * JUnit test for the {@link Word} class.
 */
public class WordTest {

    /** A random word. */
    private static final String WORD = "Bonjour";

    /**
     * Test the constructor and both getters.
     */
    @Test
    public void test() {
	final Word mot = new Word(WORD);
	assertNull(mot.getId());
	assertEquals(WORD, mot.getValue());
    }

    /**
     * Test the {@link Word#equals(Object)} and {@link Word#hashCode()} methods.
     */
    @Test
    public void equalsAndHashcode() {
	final EqualsVerifier<Word> equalsVerifier =
		EqualsVerifier.forClass(Word.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
