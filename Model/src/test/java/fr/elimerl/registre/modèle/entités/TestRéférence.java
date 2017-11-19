package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import fr.elimerl.registre.entities.Word;
import fr.elimerl.registre.entities.Movie.Support;
import fr.elimerl.registre.entities.Reference.Field;

/**
 * Test JUnit pour la classe {@link Reference}.
 */
public class TestRéférence {

    /**
     * Teste le constructeur et les getteurs.
     */
    public void test() {
	final Word mot = new Word("Demain");
	final Field champ = Field.TITLE;
	final Record fiche = new Movie("Demain ne meurt jamais",
		new User("Etienne", "etienne@email"), Support.DVD);
	final Reference référence = new Reference(mot, champ, fiche);
	assertEquals(mot, référence.getWord());
	assertEquals(champ, référence.getField());
	assertEquals(fiche, référence.getRecord());
    }

    /**
     * Teste les méthodes {@link Reference#equals(Object) equals(Object)} et
     * {@link Reference#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Reference> equalsVerifier =
		EqualsVerifier.forClass(Reference.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
