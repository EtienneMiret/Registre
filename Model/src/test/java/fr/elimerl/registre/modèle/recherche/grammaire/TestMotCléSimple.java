package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import fr.elimerl.registre.search.grammar.SimpleKeyword;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.signes.MotClé;

/**
 * Cas de test JUnit pour la classe {@link SimpleKeyword}.
 */
public class TestMotCléSimple {

    /**
     * Vérifie que deux mots-clés (expressions) contenant le même mot-clé
     * (signe) sont égaux.
     */
    @Test
    public void egaux() {
	final MotClé signe = new MotClé("qqch");
	final SimpleKeyword expr1 = new SimpleKeyword(signe);
	final SimpleKeyword expr2 = new SimpleKeyword(signe);
	assertTrue(expr1.equals(expr2));
	assertTrue(expr2.equals(expr1));
    }

    /**
     * Vérifie que deux mots-clés (expressions) contenant deux mots-clés
     * (signes) différents sont différents.
     */
    @Test
    public void différents() {
	final MotClé signe1 = new MotClé("quelque chose");
	final MotClé signe2 = new MotClé("autre chose");
	assumeFalse(signe1.equals(signe2));
	final SimpleKeyword expr1 = new SimpleKeyword(signe1);
	final SimpleKeyword expr2 = new SimpleKeyword(signe2);
	assertFalse(expr1.equals(expr2));
	assertFalse(expr2.equals(expr1));
    }

    /**
     * Vérifie que le contrat equals() et hashCode() est bien rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(SimpleKeyword.class).verify();
    }

}
