package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import fr.elimerl.registre.entities.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.entities.Composer;
import fr.elimerl.registre.entities.Film.Support;

/**
 * Classe de test de la classe {@link Film}.
 */
public class TestFilm {

    /**
     * Temps d’attente entre deux opération pour s’assurer qu’elles ont lieu
     * à des dates différentes.
     */
    private static final long SLEEP_TIME = 100L;

    /** Titre du film qui est testé. */
    private static final String TITRE = "À la belle étoile";

    /** Un premier exemple utilisateur. */
    private static final Utilisateur CRÉATEUR =
	    new Utilisateur("Etienne", "etienne@email");

    /** Un deuxième exemple d’utilisateur. */
    private static final Utilisateur MODIFIEUR =
	    new Utilisateur("Grégoire", "gregoire@email");

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestFilm.class);

    /** Film qui va être tester. */
    private Film film;

    /**
     * Vérifie qu’une date est bien dans un intervalle de test.
     *
     * @param message
     *            message à afficher si {@code effective} n’est pas dans
     *            l’intervalle de test.
     * @param avant
     *            date de début de l’intervalle de test (incluse).
     * @param après
     *            date de fin de l’intervalle de test (incluse).
     * @param effective
     *            date à tester.
     */
    private static void assertBetween(final String message, final Date avant,
	    final Date après, final Date effective) {
	assertTrue(message, avant.compareTo(effective) <= 0);
	assertTrue(message, effective.compareTo(après) <= 0);
    }

    /**
     * Définit le {@link #film} qui va être testé.
     */
    @Before
    public void setUp() {
	film = new Film(TITRE, CRÉATEUR, Support.BRD);
    }

    /**
     * Teste la création d’un DVD, puis sa méthode
     * {@link Film#toucher(Utilisateur) toucher(Utilisateur)}.
     *
     * @throws Exception
     *             normalement non, mais on ne sait jamais.
     */
    @Test
    public void testCréerToucher() throws Exception {
	/*  Création du DVD. */
	logger.info("Création du DVD.");
	Date avant = new Date();
	final Film dvd = new Film(TITRE, CRÉATEUR, Support.DVD);
	Date après = new Date();

	/* Vérification de la création. */
	final Date création = dvd.getCreation();
	assertEquals("Le titre du DVD n’est pas le bon.",
		TITRE, dvd.getTitle());
	assertEquals("Le DVD n’est pas un DVD.",
		Support.DVD, dvd.getSupport());
	assertBetween("La date de création du DVD est fausse.",
		avant, après, création);
	assertEquals("À la création, la date de dernière modification doit "
		+ "être la date de création.",
		dvd.getCreation(), dvd.getLastModification());
	assertEquals("Le créateur du DVD est faux.",
		CRÉATEUR, dvd.getCreator());
	assertEquals("À la création, le dernier éditeur doit être le créateur.",
		dvd.getCreator(), dvd.getLastModifier());

	/* Un peu d’attente. */
	logger.info("Un peu d’attente.");
	Thread.sleep(SLEEP_TIME);

	/* Touchage du DVD. */
	logger.info("Touchage du DVD.");
	avant = new Date();
	dvd.toucher(MODIFIEUR);
	après = new Date();

	/* Vérification du touchage. */
	assertBetween("La date de dernière modification du DVD est fausse.",
		avant, après, dvd.getLastModification());
	assertEquals("La date de création a été modifiée.",
		création, dvd.getCreation());
	assertEquals("Le créateur a été modifié.",
		CRÉATEUR, dvd.getCreator());
	assertEquals("Le dernier éditeur est faux.",
		MODIFIEUR, dvd.getLastModifier());
    }

    /**
     * Teste les méthodes {@link Record#getTitle() getTitle()} et
     * {@link Record#setTitle(String) setTitle(String)}.
     */
    @Test
    public void testTitre() {
	final String titre1 = "Bonjour Madame !";
	final String titre2 = "Au revoir Monsieur !";
	logger.info("Test du titre.");

	film.setTitle(titre1);
	assertEquals("Le titre est faux.", titre1, film.getTitle());

	film.setTitle(titre2);
	assertEquals("Le titre est faux.", titre2, film.getTitle());
    }

    /**
     * Teste les méthodes {@link Record#getSeries() getSeries()} et
     * {@link Record#setSeries(Série) setSeries(Série)}.
     */
    @Test
    public void testSérie() {
	final Série série1 = new Série("Robin des bois");
	final Série série2 = new Série("Thierry la fronde");
	logger.info("Test de la série.");

	film.setSeries(série1);
	assertEquals("La série est fausse.", série1, film.getSeries());

	film.setSeries(série2);
	assertEquals("La série est fausse.", série2, film.getSeries());

	film.setSeries(null);
	assertNull("La série n’a pas été effacée.", film.getSeries());
    }

    /**
     * Teste les méthodes {@link Record#getComment() getComment()} et
     * {@link Record#setComment(String) setComment(String)}.
     */
    @Test
    public void tesCommentaire() {
	final String commentaire1 = "Très bon film.";
	final String commentaire2 = "Très mauvais film.";
	logger.info("Test du commentaire.");

	film.setComment(commentaire1);
	assertEquals("Le commentaire n’a pas été défini correctement.",
		commentaire1, film.getComment());

	film.setComment(commentaire2);
	assertEquals("Le commentaire n’a pas été modifié correctement.",
		commentaire2, film.getComment());
    }

    /**
     * Teste les méthodes {@link Record#getPicture() getPicture()} et
     * {@link Record#setPicture(String) setPicture(String)}.
     */
    @Test
    public void testImage() {
	final String uuid1 = UUID.randomUUID().toString();
	final String uuid2 = UUID.randomUUID().toString();
	logger.info("Test de l’image.");

	film.setPicture(uuid1);
	assertEquals("L’image n’a pas été définie correctement.",
		uuid1, film.getPicture());

	film.setPicture(uuid2);
	assertEquals("L’image n’a pas été modifiée correctement.",
		uuid2, film.getPicture());

	film.setPicture(null);
	assertNull("L’image n’a pas été effacée correctement.",
		film.getPicture());
    }

    /**
     * Teste les méthodes {@link Record#getOwner() getOwner()} et
     * {@link Record#setOwner(Propriétaire)
     * setOwner(Propriétaire)}.
     */
    @Test
    public void testPropriétaire() {
	final Propriétaire propriétaire1 = new Propriétaire("Etienne");
	final Propriétaire propriétaire2 = new Propriétaire("Grégoire");
	logger.info("Test du propriétaire.");

	film.setOwner(propriétaire1);
	assertEquals("Le propriétaire n’a pas été défini correctement.",
		propriétaire1, film.getOwner());

	film.setOwner(propriétaire2);
	assertEquals("Le propriétaire n’a pas été modifié correctement.",
		propriétaire2, film.getOwner());

	film.setOwner(null);
	assertNull("Le propriétaire n’a pas été effacé correctement.",
		film.getOwner());
    }

    /**
     * Teste les méthodes {@link Record#getLocation() getLocation()} et
     * {@link Record#setLocation(Location) setLocation(Location)}.
     */
    @Test
    public void testEmplacement() {
	final Location emplacement1 = new Location("Chambre Etienne");
	final Location emplacement2 = new Location("Singapour");
	logger.info("Test de l’emplacement.");

	film.setLocation(emplacement1);
	assertEquals("L’emplacement n’a pas été défini correctement.",
		emplacement1, film.getLocation());

	film.setLocation(emplacement2);
	assertEquals("L’emplacement n’a pas été modifié correctement.",
		emplacement2, film.getLocation());

	film.setLocation(null);
	assertNull("L’emplacement n’a pas été effacé correctement.",
		film.getLocation());
    }

    /**
     * Teste les méthodes {@link Film#getSupport() getSupport()} et
     * {@link Film#setSupport(Support)}.
     */
    @Test
    public void testSupport() {
	final Support support1 = Support.DVD;
	final Support support2 = Support.BRD;
	logger.info("Test du support.");

	film.setSupport(support1);
	assertEquals("Le support n’a pas été correctement défini.",
		support1, film.getSupport());

	film.setSupport(support2);
	assertEquals("Le support n’a pas été correctement modifié.",
		support2, film.getSupport());
    }

    /**
     * Teste les méthodes {@link Film#getRéalisateur() getRéalisateur()} et
     * {@link Film#setRéalisateur(Réalisateur) setRéalisateur(Réalisateur)}.
     */
    @Test
    public void testRéalisateur() {
	final Réalisateur réalisateur1 = new Réalisateur("Spielberg");
	final Réalisateur réalisateur2 = new Réalisateur("Marie Devautour");
	logger.info("Test du réalisateur.");

	film.setRéalisateur(réalisateur1);
	assertEquals("Le réalisateur n’a pas été correctement défini.",
		réalisateur1, film.getRéalisateur());

	film.setRéalisateur(réalisateur2);
	assertEquals("Le réalisateur n’a pas été correctement modifié.",
		réalisateur2, film.getRéalisateur());

	film.setRéalisateur(null);
	assertNull("Le réalisateur n’a pas été correctement effacé.",
		film.getRéalisateur());
    }

    /**
     * Teste les méthodes {@link Film#getCompositeur() getCompositeur()} et
     * {@link Film#setCompositeur(Composer) setCompositeur(Composer)}.
     */
    @Test
    public void testCompositeur() {
	final Composer compositeur1 = new Composer("Mozart");
	final Composer compositeur2 = new Composer("Bethoven");
	logger.info("Test du compositeur.");

	film.setCompositeur(compositeur1);
	assertEquals("Le compositeur n’a pas été correctement défini.",
		compositeur1, film.getCompositeur());

	film.setCompositeur(compositeur2);
	assertEquals("Le compositeur n’a pas été correctement modifié.",
		compositeur2, film.getCompositeur());

	film.setCompositeur(null);
	assertNull("Le compositeur n’a pas été correctement effacé.",
		film.getCompositeur());
    }

}
