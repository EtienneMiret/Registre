package fr.elimerl.registre.search.grammar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import fr.elimerl.registre.search.tokens.Keyword;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * JUnit test case for the {@link SimpleKeyword} class.
 */
public class SimpleKeywordTest {

    /**
     * Check that two keyword expressions containing the same keyword token are
     * equal.
     */
    @Test
    public void equal() {
	final Keyword token = new Keyword("qqch");
	final SimpleKeyword expr1 = new SimpleKeyword(token);
	final SimpleKeyword expr2 = new SimpleKeyword(token);
	assertTrue(expr1.equals(expr2));
	assertTrue(expr2.equals(expr1));
    }

    /**
     * Check that two keyword expressions containing different keyword tokens
     * are not equal.
     */
    @Test
    public void notEqual() {
	final Keyword token1 = new Keyword("quelque chose");
	final Keyword token2 = new Keyword("autre chose");
	assumeFalse(token1.equals(token2));
	final SimpleKeyword expr1 = new SimpleKeyword(token1);
	final SimpleKeyword expr2 = new SimpleKeyword(token2);
	assertFalse(expr1.equals(expr2));
	assertFalse(expr2.equals(expr1));
    }

    /**
     * Check the contract for equals and hashCode.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(SimpleKeyword.class).verify();
    }

}
