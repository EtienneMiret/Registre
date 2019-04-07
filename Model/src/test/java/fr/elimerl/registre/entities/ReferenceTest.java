package fr.elimerl.registre.entities;

import static org.junit.Assert.assertEquals;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import fr.elimerl.registre.entities.Movie.Support;
import fr.elimerl.registre.entities.Reference.Field;

/**
 * JUnit test for the {@link Reference} class.
 */
public class ReferenceTest {

    /**
     * Test constructor and getters.
     */
    @Test
    public void test() {
	final Word word = new Word("Demain");
	final Field field = Field.TITLE;
	final Record record = new Movie("Demain ne meurt jamais",
		new User("Etienne", "etienne@email"), Support.DVD);
	final Reference reference = new Reference(word, field, record);
	assertEquals(word, reference.getWord());
	assertEquals(field, reference.getField());
	assertEquals(record, reference.getRecord());
    }

    /**
     * Test the {@link Reference#equals(Object)} and
     * {@link Reference#hashCode()} methods.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Reference> equalsVerifier =
		EqualsVerifier.forClass(Reference.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.withIgnoredFields ("id");
	equalsVerifier.verify();
    }

}
