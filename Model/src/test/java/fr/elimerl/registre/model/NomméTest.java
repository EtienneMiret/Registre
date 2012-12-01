package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Teste les méthodes de {@link Nommé} sur ses différentes implémentations.
 */
public class NomméTest {

    /** Loggeur de cette classe de test. */
    private static final Logger logger =
	    LoggerFactory.getLogger(NomméTest.class);

    /** Nom des objets à leur création. */
    private static final String NOM_CRÉATION = "Etienne";

    /** Noms des objets après modification. */
    private static final String[] NOMS =
	{"Grégoire", "Claire", "Quentin", "Eloi", "Blanche", "Thibault" };

    /**
     * Tests sur la classe {@link Propriétaire}.
     */
    @Test
    public void propriétaire() {
	final Propriétaire propriétaire = new Propriétaire(NOM_CRÉATION);
	testerTout(propriétaire);
    }

    /**
     * Teste les méthodes {@link Nommé#getNom() getNom()} et
     * {@link Nommé#setNom(String) setNom(String)} sur le {@link Nommé} passé
     * en paramètre.
     *
     * @param nommé
     *            le nommé à tester. Le nom initial doit être
     *            {@link #NOM_CRÉATION}.
     */
    private void testerTout(final Nommé nommé) {
	logger.info("Test de l’implémentation {}.",
		nommé.getClass().getSimpleName());
	assertEquals("Problème de récupération d’un nom initial.",
		NOM_CRÉATION, nommé.getNom());
	for (final String nom : NOMS) {
	    nommé.setNom(nom);
	    assertEquals("Le nom n’a pas été correctement défini/récupéré.",
		    nom, nommé.getNom());
	}
    }

}
