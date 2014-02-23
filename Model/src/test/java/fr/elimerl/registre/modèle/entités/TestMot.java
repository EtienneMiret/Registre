package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import fr.elimerl.registre.entités.Mot;

/**
 * Test JUnit pour la classe {@link Mot}.
 */
public class TestMot {

    /** Un exemple de mot. */
    private static final String MOT = "Bonjour";

    /**
     * Teste le constructeur et les deux getteurs.
     */
    @Test
    public void test() {
	final Mot mot = new Mot(MOT);
	assertNull(mot.getId());
	assertEquals(MOT, mot.getValeur());
    }

    /**
     * Teste les méthodes {@link Mot#equals(Object) equals(Object)} et
     * {@link Mot#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Mot> equalsVerifier =
		EqualsVerifier.forClass(Mot.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
