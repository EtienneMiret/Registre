package fr.elimerl.registre.model;

import static fr.elimerl.registre.model.Film.Support.BRD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Dans ce test JUnit, on vérifie le mapping Hibernate, le schéma de la base
 * de données, et qu’ils correspondent entre eux.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class TestPersistence {

    /** Nom qui ne doit appartenir à aucun {@link Nommé} en base. */
    private static final String NOM = "Test de persistence JUnit";

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestPersistence.class);

    /**
     * Usine à {@link EntityManager} fournie par Spring.
     */
    @Resource(name = "embeddedEmf")
    private EntityManagerFactory emf;

    /**
     * Gestionnaire d’entité utilisé en guise de DAO.
     */
    private EntityManager em;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	em = emf.createEntityManager();
	em.getTransaction().begin();
    }

    /**
     * Nettoye l’environnement de test. En particulier, fait un retour arrière
     * sur la transaction en cours.
     */
    @After
    public void tearDown() {
	em.getTransaction().rollback();
    }

    /**
     * Teste qu’on peut enregistrer en base le {@code Nommé} donné en argument.
     * Cette méthode est appelée pour tester l’enregistrement en base de chaque
     * implémentation de {@code Nommé}.
     *
     * @param nommé nommé pas encore en base.
     */
    private void enregistrementNommé(final Nommé nommé) {
	final String nom = nommé.getNom();
	assertNull(nommé.getId());
	final Nommé nomméEnregistré = em.merge(nommé);
	assertNotNull(nomméEnregistré.getId());
	assertEquals(nom, nomméEnregistré.getNom());
    }

    /**
     * Teste qu’on peut enregistrer un acteur en base.
     */
    @Test
    public void enregistrementActeur() {
	logger.info("Test d’enregistrement d’un acteur.");
	enregistrementNommé(new Acteur(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un compositeur en base.
     */
    @Test
    public void enregistrementCompositeur() {
	logger.info("Test d’enregistrement d’un compositeur.");
	enregistrementNommé(new Compositeur(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un dessinateur en base.
     */
    @Test
    public void enregistrementDessinateur() {
	logger.info("Test d’enregistrement d’un dessinateur.");
	enregistrementNommé(new Dessinateur(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un emplacement en base.
     */
    @Test
    public void enregistrementEmplacement() {
	logger.info("Test d’enregistrement d’un emplacement.");
	enregistrementNommé(new Emplacement(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un propriétaire en base.
     */
    @Test
    public void enregistrementPropriétaire() {
	logger.info("Test d’enregistrement d’un propriétaire.");
	enregistrementNommé(new Propriétaire(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un réalisateur en base.
     */
    @Test
    public void enregistrementRéalisateur() {
	logger.info("Test d’enregistrement d’un réalisateur.");
	enregistrementNommé(new Réalisateur(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un scénariste en base.
     */
    @Test
    public void enregistrementScénariste() {
	logger.info("Test d’enregistrement d’un scénariste.");
	enregistrementNommé(new Scénariste(NOM));
    }

    /**
     * Teste qu’on peut enregistrer une série en base.
     */
    @Test
    public void enregistrementSérie() {
	logger.info("Test d’enregistrement d’une série.");
	enregistrementNommé(new Série(NOM));
    }

    /**
     * Teste qu’on peut enregistrer un utilisateur en base.
     */
    @Test
    public void enregistrementUtilisateur() {
	logger.info("Test d’enregistrement d’un utilisateur.");
	final String mdp = "somePasswørd";
	Utilisateur utilisateur = new Utilisateur(NOM, mdp);
	assertNull(utilisateur.getId());
	utilisateur = em.merge(utilisateur);
	assertNotNull(utilisateur.getId());
	assertEquals(NOM, utilisateur.getNom());
	assertTrue(utilisateur.vérifierMdp(mdp));
    }

    /**
     * Teste qu’on peut enregistrer une session en base.
     */
    @Test
    public void enregistrementSession() {
	logger.info("Test d’enregistrement d’une session.");
	Utilisateur utilisateur = new Utilisateur(NOM, "***");
	utilisateur = em.merge(utilisateur);
	Session session = new Session(utilisateur, 0L);
	final String clef = session.getClef();
	final Date expiration = session.getExpiration();
	session = em.merge(session);
	assertEquals(utilisateur, session.getUtilisateur());
	assertEquals(clef, session.getClef());
	assertEquals(expiration, session.getExpiration());
    }

    /**
     * Teste qu’on peut enregistrer une bande-dessinée en base. On vérifie tous
     * les champs d’un objet {@code BandeDessinée}, y compris ceux hérités de
     * {@code Fiche}.
     */
    @Test
    public void enregistrementBandeDessinée() {
	logger.info("Test d’enregistrement d’une bande dessinée");

	final String titre = "Une super BD";
	Utilisateur créateur = new Utilisateur("Créateur", "***");
	Utilisateur éditeur = new Utilisateur("Éditeur", "***");
	créateur = em.merge(créateur);
	éditeur = em.merge(éditeur);
	BandeDessinée bandeDessinée = new BandeDessinée(titre, créateur);
	final Date création = bandeDessinée.getCréation();
	assertNull(bandeDessinée.getId());

	bandeDessinée = em.merge(bandeDessinée);

	assertNotNull(bandeDessinée.getId());
	assertEquals(titre, bandeDessinée.getTitre());
	assertNull(bandeDessinée.getSérie());
	assertNull(bandeDessinée.getCommentaire());
	assertNull(bandeDessinée.getImage());
	assertNull(bandeDessinée.getPropriétaire());
	assertNull(bandeDessinée.getEmplacement());
	assertEquals(créateur, bandeDessinée.getCréateur());
	assertEquals(création, bandeDessinée.getCréation());
	assertEquals(créateur, bandeDessinée.getDernierÉditeur());
	assertEquals(création, bandeDessinée.getDernièreÉdition());
	assertNull(bandeDessinée.getDessinateur());
	assertNull(bandeDessinée.getScénariste());
	assertNull(bandeDessinée.getNuméro());

	final Long id = bandeDessinée.getId();
	Série série = new Série(NOM);
	série = em.merge(série);
	final String commentaire = "Bonjour les gens !";
	final String image = UUID.randomUUID().toString();
	Propriétaire propriétaire = new Propriétaire(NOM);
	propriétaire = em.merge(propriétaire);
	Emplacement emplacement = new Emplacement(NOM);
	emplacement = em.merge(emplacement);
	Dessinateur dessinateur = new Dessinateur(NOM);
	dessinateur = em.merge(dessinateur);
	Scénariste scénariste = new Scénariste(NOM);
	scénariste = em.merge(scénariste);
	final Integer numéro = Integer.valueOf(12);

	bandeDessinée.setSérie(série);
	bandeDessinée.setCommentaire(commentaire);
	bandeDessinée.setImage(image);
	bandeDessinée.setPropriétaire(propriétaire);
	bandeDessinée.setEmplacement(emplacement);
	bandeDessinée.setDessinateur(dessinateur);
	bandeDessinée.setScénariste(scénariste);
	bandeDessinée.setNuméro(numéro);
	bandeDessinée.toucher(éditeur);
	final Date édition = bandeDessinée.getDernièreÉdition();

	bandeDessinée = em.merge(bandeDessinée);

	assertEquals(id, bandeDessinée.getId());
	assertEquals(titre, bandeDessinée.getTitre());
	assertEquals(série, bandeDessinée.getSérie());
	assertEquals(commentaire, bandeDessinée.getCommentaire());
	assertEquals(image, bandeDessinée.getImage());
	assertEquals(propriétaire, bandeDessinée.getPropriétaire());
	assertEquals(emplacement, bandeDessinée.getEmplacement());
	assertEquals(créateur, bandeDessinée.getCréateur());
	assertEquals(création, bandeDessinée.getCréation());
	assertEquals(éditeur, bandeDessinée.getDernierÉditeur());
	assertEquals(édition, bandeDessinée.getDernièreÉdition());
	assertEquals(dessinateur, bandeDessinée.getDessinateur());
	assertEquals(scénariste, bandeDessinée.getScénariste());
	assertEquals(numéro, bandeDessinée.getNuméro());
    }

    /**
     * Teste qu’on peut enregistrer un film en base. On ne vérifie pas les
     * champs hérités de {@code Fiche}, car l’enregistrement de ceux-ci est
     * vérifié dans {@link #enregistrementBandeDessinée()}.
     */
    @Test
    public void enregistrementFilm() {
	logger.info("Test d’enregistrement d’un film.");

	final String titre = "Un super film";
	Utilisateur créateur = new Utilisateur("Créateur", "***");
	Utilisateur éditeur = new Utilisateur("Éditeur", "***");
	créateur = em.merge(créateur);
	éditeur = em.merge(éditeur);
	Film film = new Film(titre, créateur, BRD);

	film = em.merge(film);

	assertEquals(BRD, film.getSupport());
	assertNull(film.getRéalisateur());
	assertNotNull(film.getActeurs());
	assertTrue(film.getActeurs().isEmpty());
	assertNull(film.getCompositeur());
	assertNull(film.getGenreAction());
	assertNull(film.getGenreDocumentaire());
	assertNull(film.getGenreFantastique());
	assertNull(film.getGenreGuerre());
	assertNull(film.getGenreHistoireVraie());
	assertNull(film.getGenreHistorique());
	assertNull(film.getGenreHumour());
	assertNull(film.getGenrePolicier());
	assertNull(film.getGenreRomantique());
	assertNull(film.getGenreSf());

	Réalisateur réalisateur = new Réalisateur(NOM);
	réalisateur = em.merge(réalisateur);
	Acteur acteur1 = new Acteur("Très bon acteur");
	acteur1 = em.merge(acteur1);
	Acteur acteur2 = new Acteur("Acteur moyen");
	acteur2 = em.merge(acteur2);
	Acteur acteur3 = new Acteur("Mauvais acteur");
	acteur3 = em.merge(acteur3);
	Compositeur compositeur = new Compositeur(NOM);
	compositeur = em.merge(compositeur);

	film.setRéalisateur(réalisateur);
	film.getActeurs().add(acteur1);
	film.getActeurs().add(acteur2);
	film.getActeurs().add(acteur3);
	film.setCompositeur(compositeur);
	film.setGenreAction(Boolean.TRUE);
	film.setGenreDocumentaire(Boolean.FALSE);
	film.setGenreFantastique(Boolean.TRUE);
	film.setGenreGuerre(Boolean.FALSE);
	film.setGenreHistoireVraie(Boolean.TRUE);
	film.setGenreHistorique(Boolean.FALSE);
	film.setGenreHumour(Boolean.TRUE);
	film.setGenrePolicier(Boolean.FALSE);
	film.setGenreRomantique(Boolean.TRUE);
	film.setGenreSf(Boolean.FALSE);
	film.toucher(éditeur);

	film = em.merge(film);

	assertEquals(réalisateur, film.getRéalisateur());
	assertNotNull(film.getActeurs());
	assertEquals(1 + 1 + 1, film.getActeurs().size());
	assertTrue(film.getActeurs().contains(acteur1));
	assertTrue(film.getActeurs().contains(acteur2));
	assertTrue(film.getActeurs().contains(acteur3));
	assertEquals(compositeur, film.getCompositeur());
	assertEquals(Boolean.TRUE, film.getGenreAction());
	assertEquals(Boolean.FALSE, film.getGenreDocumentaire());
	assertEquals(Boolean.TRUE, film.getGenreFantastique());
	assertEquals(Boolean.FALSE, film.getGenreGuerre());
	assertEquals(Boolean.TRUE, film.getGenreHistoireVraie());
	assertEquals(Boolean.FALSE, film.getGenreHistorique());
	assertEquals(Boolean.TRUE, film.getGenreHumour());
	assertEquals(Boolean.FALSE, film.getGenrePolicier());
	assertEquals(Boolean.TRUE, film.getGenreRomantique());
	assertEquals(Boolean.FALSE, film.getGenreSf());
    }

}
