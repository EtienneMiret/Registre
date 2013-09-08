package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.model.Film.Support;

/**
 * Classe de test de la classe {@link Film}.
 */
public class FilmTest {

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
	    LoggerFactory.getLogger(FilmTest.class);

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
	final Date création = dvd.getCréation();
	assertEquals("Le titre du DVD n’est pas le bon.",
		TITRE, dvd.getTitre());
	assertEquals("Le DVD n’est pas un DVD.",
		Support.DVD, dvd.getSupport());
	assertBetween("La date de création du DVD est fausse.",
		avant, après, création);
	assertEquals("À la création, la date de dernière modification doit "
		+ "être la date de création.",
		dvd.getCréation(), dvd.getDernièreÉdition());
	assertEquals("Le créateur du DVD est faux.",
		CRÉATEUR, dvd.getCréateur());
	assertEquals("À la création, le dernier éditeur doit être le créateur.",
		dvd.getCréateur(), dvd.getDernierÉditeur());

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
		avant, après, dvd.getDernièreÉdition());
	assertEquals("La date de création a été modifiée.",
		création, dvd.getCréation());
	assertEquals("Le créateur a été modifié.",
		CRÉATEUR, dvd.getCréateur());
	assertEquals("Le dernier éditeur est faux.",
		MODIFIEUR, dvd.getDernierÉditeur());
    }

    /**
     * Teste les méthodes {@link Fiche#getTitre() getTitre()} et
     * {@link Fiche#setTitre(String) setTitre(String)}.
     */
    @Test
    public void testTitre() {
	final String titre1 = "Bonjour Madame !";
	final String titre2 = "Au revoir Monsieur !";
	logger.info("Test du titre.");

	film.setTitre(titre1);
	assertEquals("Le titre est faux.", titre1, film.getTitre());

	film.setTitre(titre2);
	assertEquals("Le titre est faux.", titre2, film.getTitre());
    }

    /**
     * Teste les méthodes {@link Fiche#getSérie() getSérie()} et
     * {@link Fiche#setSérie(Série) setSérie(Série)}.
     */
    @Test
    public void testSérie() {
	final Série série1 = new Série("Robin des bois");
	final Série série2 = new Série("Thierry la fronde");
	logger.info("Test de la série.");

	film.setSérie(série1);
	assertEquals("La série est fausse.", série1, film.getSérie());

	film.setSérie(série2);
	assertEquals("La série est fausse.", série2, film.getSérie());

	film.setSérie(null);
	assertNull("La série n’a pas été effacée.", film.getSérie());
    }

    /**
     * Teste les méthodes {@link Fiche#getCommentaire() getCommentaire()} et
     * {@link Fiche#setCommentaire(String) setCommentaire(String)}.
     */
    @Test
    public void tesCommentaire() {
	final String commentaire1 = "Très bon film.";
	final String commentaire2 = "Très mauvais film.";
	logger.info("Test du commentaire.");

	film.setCommentaire(commentaire1);
	assertEquals("Le commentaire n’a pas été défini correctement.",
		commentaire1, film.getCommentaire());

	film.setCommentaire(commentaire2);
	assertEquals("Le commentaire n’a pas été modifié correctement.",
		commentaire2, film.getCommentaire());
    }

    /**
     * Teste les méthodes {@link Fiche#getImage() getImage()} et
     * {@link Fiche#setImage(String) setImage(String)}.
     */
    @Test
    public void testImage() {
	final String uuid1 = UUID.randomUUID().toString();
	final String uuid2 = UUID.randomUUID().toString();
	logger.info("Test de l’image.");

	film.setImage(uuid1);
	assertEquals("L’image n’a pas été définie correctement.",
		uuid1, film.getImage());

	film.setImage(uuid2);
	assertEquals("L’image n’a pas été modifiée correctement.",
		uuid2, film.getImage());

	film.setImage(null);
	assertNull("L’image n’a pas été effacée correctement.",
		film.getImage());
    }

    /**
     * Teste les méthodes {@link Fiche#getPropriétaire() getPropriétaire()} et
     * {@link Fiche#setPropriétaire(Propriétaire)
     * setPropriétaire(Propriétaire)}.
     */
    @Test
    public void testPropriétaire() {
	final Propriétaire propriétaire1 = new Propriétaire("Etienne");
	final Propriétaire propriétaire2 = new Propriétaire("Grégoire");
	logger.info("Test du propriétaire.");

	film.setPropriétaire(propriétaire1);
	assertEquals("Le propriétaire n’a pas été défini correctement.",
		propriétaire1, film.getPropriétaire());

	film.setPropriétaire(propriétaire2);
	assertEquals("Le propriétaire n’a pas été modifié correctement.",
		propriétaire2, film.getPropriétaire());

	film.setPropriétaire(null);
	assertNull("Le propriétaire n’a pas été effacé correctement.",
		film.getPropriétaire());
    }

    /**
     * Teste les méthodes {@link Fiche#getEmplacement() getEmplacement()} et
     * {@link Fiche#setEmplacement(Emplacement) setEmplacement(Emplacement)}.
     */
    @Test
    public void testEmplacement() {
	final Emplacement emplacement1 = new Emplacement("Chambre Etienne");
	final Emplacement emplacement2 = new Emplacement("Singapour");
	logger.info("Test de l’emplacement.");

	film.setEmplacement(emplacement1);
	assertEquals("L’emplacement n’a pas été défini correctement.",
		emplacement1, film.getEmplacement());

	film.setEmplacement(emplacement2);
	assertEquals("L’emplacement n’a pas été modifié correctement.",
		emplacement2, film.getEmplacement());

	film.setEmplacement(null);
	assertNull("L’emplacement n’a pas été effacé correctement.",
		film.getEmplacement());
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
     * {@link Film#setCompositeur(Compositeur) setCompositeur(Compositeur)}.
     */
    @Test
    public void testCompositeur() {
	final Compositeur compositeur1 = new Compositeur("Mozart");
	final Compositeur compositeur2 = new Compositeur("Bethoven");
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
