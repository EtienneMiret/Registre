package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import fr.elimerl.registre.search.grammar.SearchQuery;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.grammar.Expression;
import fr.elimerl.registre.search.grammar.RequêteEntreParenthèse;

/**
 * Cas de test JUnit pour la classe {@link RequêteEntreParenthèse}.
 */
public class TestRequêteEntreParenthèse {

    /**
     * Vérifie que mettre deux fois la même requête entre parenthèse donne deux
     * objets égaux.
     */
    @Test
    public void égaux() {
	final SearchQuery requête = new SearchQuery(true);
	final RequêteEntreParenthèse parenthèse1 =
		new RequêteEntreParenthèse(requête);
	final RequêteEntreParenthèse parenthèse2 =
		new RequêteEntreParenthèse(requête);
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
	final RequêteEntreParenthèse parenthèse1 =
		new RequêteEntreParenthèse(requête1);
	final RequêteEntreParenthèse parenthèse2 =
		new RequêteEntreParenthèse(requête2);
	assertFalse(parenthèse1.equals(parenthèse2));
	assertFalse(parenthèse2.equals(parenthèse1));
    }

    /**
     * Vérifie que le contrat equals() et hashCode() est rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(RequêteEntreParenthèse.class).verify();
    }

}
