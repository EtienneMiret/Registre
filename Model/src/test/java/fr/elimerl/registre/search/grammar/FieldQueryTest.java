package fr.elimerl.registre.search.grammar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * JUnit test case for the {@link FieldQuery} class. Only the {@code hasCode()}
 * and {@code equals(Object} methods are tested here. The main method
 * ({@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery, Root)}
 * is already tested in {@link CreatePredicateTest}.
 */
public class FieldQueryTest {

    /**
     * Check that two queries regarding the same field and same keyword are equal.
     */
    @Test
    public void equal() {
	final Keyword keyword = new Keyword("toto");
	final FieldQuery query1 = new FieldQuery(Field.TITLE, keyword);
	final FieldQuery query2 = new FieldQuery(Field.TITLE, keyword);
	assertTrue(query1.equals(query2));
	assertTrue(query2.equals(query1));
    }

    /**
     * Check that two queries regarding the same keyword but different fields
     * are different.
     */
    @Test
    public void differentFields() {
	final Keyword keyword = new Keyword("toto");
	final FieldQuery query1 = new FieldQuery(Field.TITLE, keyword);
	final FieldQuery query2 = new FieldQuery(Field.AUTHOR, keyword);
	assertFalse(query1.equals(query2));
	assertFalse(query2.equals(query1));
    }

    /**
     * Check that two queries regarding the same field but different keywords
     * are different.
     */
    @Test
    public void differentKeywords() {
	final Keyword keyword1 = new Keyword("toto");
	final Keyword keyword2 = new Keyword("tata");
	final FieldQuery query1 = new FieldQuery(Field.TITLE, keyword1);
	final FieldQuery query2 = new FieldQuery(Field.TITLE, keyword2);
	assertFalse(query1.equals(query2));
	assertFalse(query2.equals(query1));
    }

    /**
     * Check the hashCode() and equals() contract.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(FieldQuery.class).verify();
    }

}
