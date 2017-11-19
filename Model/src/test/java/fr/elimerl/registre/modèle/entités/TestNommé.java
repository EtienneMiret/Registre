package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.entities.Actor;

/**
 * Teste les méthodes de {@link Named} sur ses différentes implémentations.
 */
public class TestNommé {

    /** Loggeur de cette classe de test. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestNommé.class);

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
     * Tests sur la classe {@link Série}.
     */
    @Test
    public void série() {
	final Série série = new Série(NOM_CRÉATION);
	testerTout(série);
    }

    /**
     * Tests sur la classe {@link Location}.
     */
    @Test
    public void emplacement() {
	final Location emplacement = new Location(NOM_CRÉATION);
	testerTout(emplacement);
    }

    /**
     * Tests sur la classe {@link Actor}.
     */
    @Test
    public void acteur() {
	final Actor acteur = new Actor(NOM_CRÉATION);
	testerTout(acteur);
    }

    /**
     * Tests sur la classe {@link Réalisateur}.
     */
    @Test
    public void réalisateur() {
	final Réalisateur réalisateur = new Réalisateur(NOM_CRÉATION);
	testerTout(réalisateur);
    }

    /**
     * Tests sur la classe {@link Composer}.
     */
    @Test
    public void compositeur() {
	final Composer compositeur = new Composer(NOM_CRÉATION);
	testerTout(compositeur);
    }

    /**
     * Tests sur la classe {@link Cartoonist}.
     */
    @Test
    public void dessinateur() {
	final Cartoonist dessinateur = new Cartoonist(NOM_CRÉATION);
	testerTout(dessinateur);
    }

    /**
     * Tests sur la classe {@link Scénariste}.
     */
    @Test
    public void scénariste() {
	final Scénariste scénariste = new Scénariste(NOM_CRÉATION);
	testerTout(scénariste);
    }

    /**
     * Tests sur la classe {@link Author}.
     */
    @Test
    public void auteurs() {
	final Author auteur = new Author(NOM_CRÉATION);
	testerTout(auteur);
    }

    /**
     * Teste les méthodes {@link Named#getName() getName()} et
     * {@link Named#setName(String) setName(String)} sur le {@link Named} passé
     * en paramètre.
     *
     * @param nommé
     *            le nommé à tester. Le nom initial doit être
     *            {@link #NOM_CRÉATION}.
     */
    private static void testerTout(final Named nommé) {
	logger.info("Test de l’implémentation {}.",
		nommé.getClass().getSimpleName());
	assertEquals("Problème de récupération d’un nom initial.",
		NOM_CRÉATION, nommé.getName());
	for (final String nom : NOMS) {
	    nommé.setName(nom);
	    assertEquals("Le nom n’a pas été correctement défini/récupéré.",
		    nom, nommé.getName());
	}
    }

}
