package fr.elimerl.registre.search.tokens;

import nl.jqno.equalsverifier.EqualsVerifier;

import nl.jqno.equalsverifier.Warning;
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
	EqualsVerifier.forClass(Keyword.class)
            .suppress (Warning.ALL_FIELDS_SHOULD_BE_USED)
            .verify();
    }

}
