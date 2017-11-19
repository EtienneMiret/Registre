package fr.elimerl.registre.model.entités;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import fr.elimerl.registre.entities.Word;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * Test JUnit pour la classe {@link Word}.
 */
public class TestMot {

    /** Un exemple de mot. */
    private static final String MOT = "Bonjour";

    /**
     * Teste le constructeur et les deux getteurs.
     */
    @Test
    public void test() {
	final Word mot = new Word(MOT);
	assertNull(mot.getId());
	assertEquals(MOT, mot.getValue());
    }

    /**
     * Teste les méthodes {@link Word#equals(Object) equals(Object)} et
     * {@link Word#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Word> equalsVerifier =
		EqualsVerifier.forClass(Word.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
