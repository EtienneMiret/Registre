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
 * Cas de test JUnit pour la classe {@link SearchQuery}.
 */
public class TestRequête {

    /**
     * Teste qu’une conjonction vide est bien égale à une disjonction vide.
     */
    @Test
    public void conjonctionVideÉgalDisjonctionVide() {
	final SearchQuery conjonctionVide = new SearchQuery(true);
	final SearchQuery disjonctionVide = new SearchQuery(false);
	assertTrue(conjonctionVide.equals(disjonctionVide));
	assertTrue(disjonctionVide.equals(conjonctionVide));
    }

    /**
     * Teste qu’une conjonction d’un élément est bien égale à une disjonction
     * du même élément.
     */
    @Test
    public void conjonctionTailleUnÉgalDisjonctionTailleUn() {
	final Expression expr = mock(Expression.class);
	final SearchQuery conjonction = new SearchQuery(true, expr);
	final SearchQuery disjonction = new SearchQuery(false, expr);
	assertTrue(conjonction.equals(disjonction));
	assertTrue(disjonction.equals(conjonction));
    }

    /**
     * Test qu’une conjonction d’un élément est bien différente d’une
     * disjonction d’un autre élément.
     */
    @Test
    public void conjonctionTailleUnDifférentDisjonctionTailleUn() {
	final Expression expr1 = mock(Expression.class);
	final Expression expr2 = mock(Expression.class);
	assumeFalse(expr1.equals(expr2));
	final SearchQuery conjonction = new SearchQuery(true, expr1);
	final SearchQuery disjonction = new SearchQuery(false, expr2);
	assertFalse(conjonction.equals(disjonction));
	assertFalse(disjonction.equals(conjonction));
    }

    /**
     * Teste qu’une conjonction de deux éléments est bien différente d’une
     * disjonction de deux éléments identiques.
     */
    @Test
    public void conjonctionTailleDeuxDifférentDisjonctionTailleDeux() {
	final Expression expr1 = mock(Expression.class);
	final Expression expr2 = mock(Expression.class);
	final SearchQuery conjonction = new SearchQuery(true, expr1, expr2);
	final SearchQuery disjonction = new SearchQuery(false, expr1, expr2);
	assertFalse(conjonction.equals(disjonction));
	assertFalse(disjonction.equals(conjonction));
    }

    /**
     * Teste que le contrat equals() et hashCode() est bien rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	final EqualsVerifier<SearchQuery> vérifieur =
		EqualsVerifier.forClass(SearchQuery.class);
	vérifieur.verify();
    }

}
