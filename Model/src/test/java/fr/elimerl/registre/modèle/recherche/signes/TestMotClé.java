package fr.elimerl.registre.modèle.recherche.signes;

import fr.elimerl.registre.search.tokens.Keyword;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * Classe de test de la classe {@link Keyword}.
 */
public class TestMotClé {

    /**
     * Teste les méthodes {@link Keyword#equals(Object)} et
     * {@link Keyword#hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	EqualsVerifier.forClass(Keyword.class).verify();
    }

}
