package fr.elimerl.registre.modèle.recherche.signes;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * Classe de test de la classe {@link MotClé}.
 */
public class TestMotClé {

    /**
     * Teste les méthodes {@link MotClé#equals(Object)} et
     * {@link MotClé#hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	EqualsVerifier.forClass(MotClé.class).verify();
    }

}
