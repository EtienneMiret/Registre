package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.search.grammar.FieldQuery;
import fr.elimerl.registre.search.signes.Champ;
import fr.elimerl.registre.search.signes.MotClé;

/**
 * Cas de test JUnit pour la classe {@link FieldQuery}. Seules les méthodes
 * {@code hashCode()} et {@code equals(Object)} sont testées ici. La méthode
 * principale ({@link FieldQuery#createPredicate(
 * javax.persistence.criteria.CriteriaBuilder,
 * javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.Root)
 * createPredicate(CriteriaBuilder, CriteriaQuery, Root)}) est testée dans
 * {@link TestCréerPrédicat}.
 */
public class TestRequêteSurChamp {

    /**
     * Vérifie que deux requêtes portant sur le même champs et le même mot-clé
     * sont égales.
     */
    @Test
    public void égales() {
	final MotClé motClé = new MotClé("toto");
	final FieldQuery requête1 =
		new FieldQuery(Champ.TITRE, motClé);
	final FieldQuery requête2 =
		new FieldQuery(Champ.TITRE, motClé);
	assertTrue(requête1.equals(requête2));
	assertTrue(requête2.equals(requête1));
    }

    /**
     * Vérifie que deux requêtes portant sur le même mot-clé mais sur deux
     * champs différents sont différentes.
     */
    @Test
    public void champsDifférents() {
	final MotClé motClé = new MotClé("toto");
	final FieldQuery requête1 =
		new FieldQuery(Champ.TITRE, motClé);
	final FieldQuery requête2 =
		new FieldQuery(Champ.AUTEUR, motClé);
	assertFalse(requête1.equals(requête2));
	assertFalse(requête2.equals(requête1));
    }

    /**
     * Vérifie que deux requêtes portant sur le même champ mais sur deux
     * mots-clés différents sont différentes.
     */
    @Test
    public void motsClésDifférents() {
	final MotClé motClé1 = new MotClé("toto");
	final MotClé motClé2 = new MotClé("tata");
	final FieldQuery requête1 =
		new FieldQuery(Champ.TITRE, motClé1);
	final FieldQuery requête2 =
		new FieldQuery(Champ.TITRE, motClé2);
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
