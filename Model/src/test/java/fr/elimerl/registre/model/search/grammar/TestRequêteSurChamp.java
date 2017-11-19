package fr.elimerl.registre.model.search.grammar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.grammar.FieldQuery;

/**
 * Cas de test JUnit pour la classe {@link FieldQuery}. Seules les méthodes
 * {@code hashCode()} et {@code equals(Object)} sont testées ici. La méthode
 * principale ({@link FieldQuery#createPredicate(
 * javax.persistence.criteria.CriteriaBuilder,
 * javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.Root)
 * createPredicate(CriteriaBuilder, CriteriaQuery, Root)}) est testée dans
 * {@link CreatePredicateTest}.
 */
public class TestRequêteSurChamp {

    /**
     * Vérifie que deux requêtes portant sur le même champs et le même mot-clé
     * sont égales.
     */
    @Test
    public void égales() {
	final Keyword motClé = new Keyword("toto");
	final FieldQuery requête1 =
		new FieldQuery(Field.TITLE, motClé);
	final FieldQuery requête2 =
		new FieldQuery(Field.TITLE, motClé);
	assertTrue(requête1.equals(requête2));
	assertTrue(requête2.equals(requête1));
    }

    /**
     * Vérifie que deux requêtes portant sur le même mot-clé mais sur deux
     * champs différents sont différentes.
     */
    @Test
    public void champsDifférents() {
	final Keyword motClé = new Keyword("toto");
	final FieldQuery requête1 =
		new FieldQuery(Field.TITLE, motClé);
	final FieldQuery requête2 =
		new FieldQuery(Field.AUTHOR, motClé);
	assertFalse(requête1.equals(requête2));
	assertFalse(requête2.equals(requête1));
    }

    /**
     * Vérifie que deux requêtes portant sur le même champ mais sur deux
     * mots-clés différents sont différentes.
     */
    @Test
    public void motsClésDifférents() {
	final Keyword motClé1 = new Keyword("toto");
	final Keyword motClé2 = new Keyword("tata");
	final FieldQuery requête1 =
		new FieldQuery(Field.TITLE, motClé1);
	final FieldQuery requête2 =
		new FieldQuery(Field.TITLE, motClé2);
	assertFalse(requête1.equals(requête2));
	assertFalse(requête2.equals(requête1));
    }

    /**
     * Vérifie que le contrat des méthodes equals() et hashCode() est rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(FieldQuery.class).verify();
    }

}
