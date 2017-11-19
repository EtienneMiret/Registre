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
 * Cas de test JUnit pour la classe {@link BracketedQuery}.
 */
public class TestRequêteEntreParenthèse {

    /**
     * Vérifie que mettre deux fois la même requête entre parenthèse donne deux
     * objets égaux.
     */
    @Test
    public void égaux() {
	final SearchQuery requête = new SearchQuery(true);
	final BracketedQuery parenthèse1 =
		new BracketedQuery(requête);
	final BracketedQuery parenthèse2 =
		new BracketedQuery(requête);
	assertTrue(parenthèse1.equals(parenthèse2));
	assertTrue(parenthèse2.equals(parenthèse1));
    }

    /**
     * Vérifie que mettre deux requête différentes entre parenthèse donne deux
     * objets différents.
     */
    @Test
    public void différents() {
	final SearchQuery requête1 = new SearchQuery(true, mock(Expression.class));
	final SearchQuery requête2 = new SearchQuery(true, mock(Expression.class));
	final BracketedQuery parenthèse1 =
		new BracketedQuery(requête1);
	final BracketedQuery parenthèse2 =
		new BracketedQuery(requête2);
	assertFalse(parenthèse1.equals(parenthèse2));
	assertFalse(parenthèse2.equals(parenthèse1));
    }

    /**
     * Vérifie que le contrat equals() et hashCode() est rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(BracketedQuery.class).verify();
    }

}
