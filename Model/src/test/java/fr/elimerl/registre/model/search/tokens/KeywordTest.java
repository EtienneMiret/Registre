package fr.elimerl.registre.model.search.tokens;

import fr.elimerl.registre.search.tokens.Keyword;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * Test case for the {@link Keyword} class.
 */
public class KeywordTest {

    /**
     * Test the {@link Keyword#equals(Object)} and {@link Keyword#hashCode()}
     * methods.
     */
    @Test
    public void equalsAndHashCode() {
	EqualsVerifier.forClass(Keyword.class).verify();
    }

}
