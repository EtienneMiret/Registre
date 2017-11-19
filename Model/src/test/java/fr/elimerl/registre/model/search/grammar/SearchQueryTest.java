package fr.elimerl.registre.model.search.grammar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.mock;

import fr.elimerl.registre.search.grammar.SearchQuery;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.grammar.Expression;

/**
 * JUnit test case for the {@link SearchQuery} class.
 */
public class SearchQueryTest {

    /**
     * Check that an empty conjunction is equal to an empty disjunction.
     */
    @Test
    public void emptyConjunctionEqualEmptyDisjunction() {
	final SearchQuery emptyConjunction = new SearchQuery(true);
	final SearchQuery emptyDisjunction = new SearchQuery(false);
	assertTrue(emptyConjunction.equals(emptyDisjunction));
	assertTrue(emptyDisjunction.equals(emptyConjunction));
    }

    /**
     * Check that a conjunction with a single element is equal to a disjunction
     * of the same element.
     */
    @Test
    public void oneSizedConjunctionEqualOneSizedDisjunction() {
	final Expression expr = mock(Expression.class);
	final SearchQuery conjunction = new SearchQuery(true, expr);
	final SearchQuery disjunction = new SearchQuery(false, expr);
	assertTrue(conjunction.equals(disjunction));
	assertTrue(disjunction.equals(conjunction));
    }

    /**
     * Check that a conjunction with a single element is not equal to a
     * disjunction with another element.
     */
    @Test
    public void oneSizedConjunctionNotEqualOneSizedDisjunction() {
	final Expression expr1 = mock(Expression.class);
	final Expression expr2 = mock(Expression.class);
	assumeFalse(expr1.equals(expr2));
	final SearchQuery conjunction = new SearchQuery(true, expr1);
	final SearchQuery disjunction = new SearchQuery(false, expr2);
	assertFalse(conjunction.equals(disjunction));
	assertFalse(disjunction.equals(conjunction));
    }

    /**
     * Check a conjunction of two elements is not equal to a disjunction of the
     * two same elements.
     */
    @Test
    public void twoSizedConjunctionNotEqualTwoSizedDisjunction() {
	final Expression expr1 = mock(Expression.class);
	final Expression expr2 = mock(Expression.class);
	final SearchQuery conjunction = new SearchQuery(true, expr1, expr2);
	final SearchQuery disjunction = new SearchQuery(false, expr1, expr2);
	assertFalse(conjunction.equals(disjunction));
	assertFalse(disjunction.equals(conjunction));
    }

    /**
     * Test the equals() and hashCode() contract.
     */
    @Test
    public void contratEqualsEtHashCode() {
	final EqualsVerifier<SearchQuery> verifier =
		EqualsVerifier.forClass(SearchQuery.class);
	verifier.verify();
    }

}
