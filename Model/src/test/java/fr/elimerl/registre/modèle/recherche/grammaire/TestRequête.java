package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.mock;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.grammar.Expression;
import fr.elimerl.registre.search.grammar.Requête;

/**
 * Cas de test JUnit pour la classe {@link Requête}.
 */
public class TestRequête {

    /**
     * Teste qu’une conjonction vide est bien égale à une disjonction vide.
     */
    @Test
    public void conjonctionVideÉgalDisjonctionVide() {
	final Requête conjonctionVide = new Requête(true);
	final Requête disjonctionVide = new Requête(false);
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
	final Requête conjonction = new Requête(true, expr);
	final Requête disjonction = new Requête(false, expr);
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
	final Requête conjonction = new Requête(true, expr1);
	final Requête disjonction = new Requête(false, expr2);
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
	final Requête conjonction = new Requête(true, expr1, expr2);
	final Requête disjonction = new Requête(false, expr1, expr2);
	assertFalse(conjonction.equals(disjonction));
	assertFalse(disjonction.equals(conjonction));
    }

    /**
     * Teste que le contrat equals() et hashCode() est bien rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	final EqualsVerifier<Requête> vérifieur =
		EqualsVerifier.forClass(Requête.class);
	vérifieur.verify();
    }

}
