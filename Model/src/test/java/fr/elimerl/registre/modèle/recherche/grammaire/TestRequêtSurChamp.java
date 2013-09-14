package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import fr.elimerl.registre.modèle.recherche.signes.Champ;
import fr.elimerl.registre.modèle.recherche.signes.MotClé;

/**
 * Cas de test JUnit pour la classe {@link RequêteSurChamp}.
 */
public class TestRequêtSurChamp {

    /**
     * Vérifie que deux requêtes portant sur le même champs et le même mot-clé
     * sont égales.
     */
    @Test
    public void égales() {
	final MotClé motClé = new MotClé("toto");
	final RequêteSurChamp requête1 =
		new RequêteSurChamp(Champ.TITRE, motClé);
	final RequêteSurChamp requête2 =
		new RequêteSurChamp(Champ.TITRE, motClé);
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
	final RequêteSurChamp requête1 =
		new RequêteSurChamp(Champ.TITRE, motClé);
	final RequêteSurChamp requête2 =
		new RequêteSurChamp(Champ.AUTEUR, motClé);
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
	final RequêteSurChamp requête1 =
		new RequêteSurChamp(Champ.TITRE, motClé1);
	final RequêteSurChamp requête2 =
		new RequêteSurChamp(Champ.TITRE, motClé2);
	assertFalse(requête1.equals(requête2));
	assertFalse(requête2.equals(requête1));
    }

    /**
     * Vérifie que le contrat des méthodes equals() et hashCode() est rempli.
     */
    @Test
    public void contratEqualsEtHashCode() {
	EqualsVerifier.forClass(RequêteSurChamp.class).verify();
    }

}
