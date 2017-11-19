package fr.elimerl.registre.model.search.grammar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import fr.elimerl.registre.search.grammar.SearchQuery;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.grammar.Expression;
import fr.elimerl.registre.search.grammar.BracketedQuery;

/**
 * JUnit test case for the {@link BracketedQuery} class.
 */
public class BracketedQueryTest {

    /**
     * Check that putting the same search query twice in brackets produces two
     * equal objects.
     */
    @Test
    public void equal() {
	final SearchQuery searchQuery = new SearchQuery(true);
	final BracketedQuery bracket1 = new BracketedQuery(searchQuery);
	final BracketedQuery bracket2 = new BracketedQuery(searchQuery);
	assertTrue(bracket1.equals(bracket2));
	assertTrue(bracket2.equals(bracket1));
    }

    /**
     * Check that putting two different search query in brackets produces two
     * different objects.
     */
    @Test
    public void different() {
	final SearchQuery query1 = new SearchQuery(true, mock(Expression.class));
	final SearchQuery query2 = new SearchQuery(true, mock(Expression.class));
	final BracketedQuery bracket1 = new BracketedQuery(query1);
	final BracketedQuery bracket2 = new BracketedQuery(query2);
	assertFalse(bracket1.equals(bracket2));
	assertFalse(bracket2.equals(bracket1));
    }

    /**
     * Check the equals() and hashCode() contract.
     */
    @Test
    public void equalsAndHashCode() {
	EqualsVerifier.forClass(BracketedQuery.class).verify();
    }

}
