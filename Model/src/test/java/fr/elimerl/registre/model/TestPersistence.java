package fr.elimerl.registre.model;

import static fr.elimerl.registre.model.Film.Support.BRD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.elimerl.registre.model.Film.Support;

/**
 * Dans ce test JUnit, on vérifie le mapping Hibernate, le schéma de la base
 * de données, et qu’ils correspondent entre eux.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class TestPersistence {

    /** Nom qui ne doit appartenir à aucun {@link Nommé} en base. */
    private static final String NOM = "Test de persistence JUnit";

    /** L’entier 0, défini comme une constante. */
    private static final int ZÉRO = 0;

    /** L’entier 1, défini comme une constante. */
    private static final int UN = 1;

    /** L’entier 2, défini comme une constante. */
    private static final int DEUX = 2;

    /** L’entier 3, défini comme une constante. */
    private static final int TROIS = 3;

    /** L’entier 12, défini comme une constante. */
    private static final int DOUZE = 12;

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
     * Fabrique de requêtes.
     */
    private CriteriaBuilder builder;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	builder = emf.getCriteriaBuilder();
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
	em.close();
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
	assertEquals(TROIS, film.getActeurs().size());
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

    /**
     * Teste l’enregistrement de deux acteurs ayant le même nom. Comme deux
     * acteurs ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxActeursIdentiques() {
	logger.info("Test de l’enregistrement de deux acteurs identiques.");
	em.merge(new Acteur(NOM));
	em.merge(new Acteur(NOM));
    }

    /**
     * Teste l’enregistrement de deux compositeurs ayant le même nom. Comme deux
     * compositeurs ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxCompositeursIdentiques() {
	logger.info("Test de l’enregistrement de deux compositeurs "
		+ "identiques.");
	em.merge(new Compositeur(NOM));
	em.merge(new Compositeur(NOM));
    }

    /**
     * Teste l’enregistrement de deux dessinateurs ayant le même nom. Comme deux
     * dessinateurs ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxDessinateursIdentiques() {
	logger.info("Test de l’enregistrement de deux dessinateurs "
		+ "identiques");
	em.merge(new Dessinateur(NOM));
	em.merge(new Dessinateur(NOM));
    }

    /**
     * Teste l’enregistrement de deux emplacements ayant le même nom. Comme deux
     * emplacements ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxEmplacementsIdentiques() {
	logger.info("Test de l’enregistrement de deux emplacements "
		+ "identiques");
	em.merge(new Emplacement(NOM));
	em.merge(new Emplacement(NOM));
    }

    /**
     * Teste l’enregistrement de deux propriétaires ayant le même nom. Comme
     * deux propriétaires ne peuvent avoir le même nom, une erreur doit se
     * produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxPropriétairesIdentiques() {
	logger.info("Test de l’enregistrement de deux propriétaires "
		+ "identiques.");
	em.merge(new Propriétaire(NOM));
	em.merge(new Propriétaire(NOM));
    }

    /**
     * Teste l’enregistrement de deux réalisateurs ayant le même nom. Comme deux
     * réalisateurs ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxRéalisateursIdentiques() {
	logger.info("Test de l’enregistrement de deux réalisateurs "
		+ "identiques.");
	em.merge(new Réalisateur(NOM));
	em.merge(new Réalisateur(NOM));
    }

    /**
     * Teste l’enregistrement de deux scénaristes ayant le même nom. Comme deux
     * scénaristes ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxScénaristesIdentiques() {
	logger.info("Test de l’enregistrement de deux scénaristes "
		+ "identiques.");
	em.merge(new Scénariste(NOM));
	em.merge(new Scénariste(NOM));
    }

    /**
     * Teste l’enregistrement de deux séries ayant le même nom. Comme deux
     * séries ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxSériesIdentiques() {
	logger.info("Test de l’enregistrement de deux séries identiques.");
	em.merge(new Série(NOM));
	em.merge(new Série(NOM));
    }

    /**
     * Teste l’enregistrement de deux utilisateurs ayant le même nom. Comme deux
     * utilisateurs ne peuvent avoir le même nom, une erreur doit se produire.
     */
    @Test(expected = PersistenceException.class)
    public void deuxUtilisateursIdentiques() {
	logger.info("Test de l’enregistrement de deux utilisateurs "
		+ "identiques.");
	em.merge(new Utilisateur(NOM, "azerty"));
	em.merge(new Utilisateur(NOM, "qwerty"));
    }

    /**
     * Teste l’enregistrement de nommés ayant le même nom mais venant tous
     * d’implémentations différentes. Dans ce cas, il ne doit y avoir aucune
     * erreur.
     */
    @Test
    public void nommésDifférentsAvecLeMêmeNom() {
	logger.info("Test de l’enregistrement de nommés différents mais avec "
		+ "le même nom.");
	em.merge(new Acteur(NOM));
	em.merge(new Compositeur(NOM));
	em.merge(new Dessinateur(NOM));
	em.merge(new Emplacement(NOM));
	em.merge(new Propriétaire(NOM));
	em.merge(new Réalisateur(NOM));
	em.merge(new Scénariste(NOM));
	em.merge(new Série(NOM));
    }

    /**
     * Charge de la base de données tous les objets dont le type est donné en
     * argument.
     *
     * @param type
     *            classe des objets à charger.
     * @param <T>
     *            type des objets à charger.
     * @param ordre
     *            attribut selon lequel trier les objets à charger.
     * @return tous les objets de la classe {@code type} présents en base, triés
     *         suivant leur attribut {@code ordre} croissant.
     */
    private <T> Iterator<T> charger(final Class<T> type, final String ordre) {
	final CriteriaQuery<T> query = builder.createQuery(type);
	final Root<T> root = query.from(type);
	query.orderBy(builder.asc(root.get(ordre)));
	return em.createQuery(query).getResultList().iterator();
    }

    /**
     * Charge de la base de donnée tous les nommés dont le type est donné en
     * argument.
     *
     * @param type
     *            classe des objets à charger.
     * @param <T>
     *            type des objets à charger.
     * @return tous les objets de la classe {@code type} présents en base, triés
     *         par noms croissants.
     */
    private <T extends Nommé> Iterator<T> chargerNommés(final Class<T> type) {
	return charger(type, "nom");
    }

    /**
     * Teste le chargement des acteurs insérés dans la base de données par le
     * fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerActeurs() {
	logger.info("Chargement des acteurs de test-data.sql.");
	final Iterator<Acteur> acteurs = chargerNommés(Acteur.class);

	final Acteur emmaWatson = acteurs.next();
	assertEquals(TROIS, emmaWatson.getId().intValue());
	assertEquals("Emma Watson", emmaWatson.getNom());

	final Acteur evaMendes = acteurs.next();
	assertEquals(UN, evaMendes.getId().intValue());
	assertEquals("Eva Mendes", evaMendes.getNom());

	final Acteur georgeClooney = acteurs.next();
	assertEquals(DEUX, georgeClooney.getId().intValue());
	assertEquals("George Clooney", georgeClooney.getNom());

	final Acteur willSmith = acteurs.next();
	assertEquals(ZÉRO, willSmith.getId().intValue());
	assertEquals("Will Smith", willSmith.getNom());

	assertFalse(acteurs.hasNext());
    }

    /**
     * Teste le chargement des compositeurs insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerCompositeurs() {
	logger.info("Chargement des compositeurs de test-data.sql.");
	final Iterator<Compositeur> compositeurs =
		chargerNommés(Compositeur.class);

	final Compositeur hansZimmer = compositeurs.next();
	assertEquals(UN, hansZimmer.getId().intValue());
	assertEquals("Hans Zimmer", hansZimmer.getNom());

	final Compositeur howardShore = compositeurs.next();
	assertEquals(ZÉRO, howardShore.getId().intValue());
	assertEquals("Howard Shore", howardShore.getNom());

	final Compositeur lisaGerrard = compositeurs.next();
	assertEquals(DEUX, lisaGerrard.getId().intValue());
	assertEquals("Lisa Gerrard", lisaGerrard.getNom());

	assertFalse(compositeurs.hasNext());
    }

    /**
     * Teste le chargement des dessinateurs insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerDessinateurs() {
	logger.info("Chargement des dessinateurs de test-data.sql.");
	final Iterator<Dessinateur> dessinateurs =
		chargerNommés(Dessinateur.class);

	final Dessinateur alainHenriet = dessinateurs.next();
	assertEquals(UN, alainHenriet.getId().intValue());
	assertEquals("Alain Henriet", alainHenriet.getNom());

	final Dessinateur jigounov = dessinateurs.next();
	assertEquals(ZÉRO, jigounov.getId().intValue());
	assertEquals("Jigounov", jigounov.getNom());

	assertFalse(dessinateurs.hasNext());
    }

    /**
     * Teste le chargement des emplacements insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerEmplacement() {
	logger.info("Chargement des emplacements de test-data.sql.");
	final Iterator<Emplacement> emplacements =
		chargerNommés(Emplacement.class);

	final Emplacement laRocheSurYon = emplacements.next();
	assertEquals(UN, laRocheSurYon.getId().intValue());
	assertEquals("La Roche sur Yon", laRocheSurYon.getNom());

	final Emplacement lyon = emplacements.next();
	assertEquals(DEUX, lyon.getId().intValue());
	assertEquals("Lyon", lyon.getNom());

	final Emplacement singapour = emplacements.next();
	assertEquals(TROIS, singapour.getId().intValue());
	assertEquals("Singapour", singapour.getNom());

	final Emplacement verneuil = emplacements.next();
	assertEquals(ZÉRO, verneuil.getId().intValue());
	assertEquals("Verneuil", verneuil.getNom());

	assertFalse(emplacements.hasNext());
    }

    /**
     * Teste le chargement des propriétaires insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerPropriétaires() {
	logger.info("Chargement des propriétaires de test-data.sql.");
	final Iterator<Propriétaire> propriétaires =
		chargerNommés(Propriétaire.class);

	final Propriétaire claire = propriétaires.next();
	assertEquals(DEUX, claire.getId().intValue());
	assertEquals("Claire", claire.getNom());

	final Propriétaire etienne = propriétaires.next();
	assertEquals(ZÉRO, etienne.getId().intValue());
	assertEquals("Etienne", etienne.getNom());

	final Propriétaire grégoire = propriétaires.next();
	assertEquals(UN, grégoire.getId().intValue());
	assertEquals("Grégoire", grégoire.getNom());

	assertFalse(propriétaires.hasNext());
    }

    /**
     * Teste le chargement des réalisateurs insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerRéalisateurs() {
	logger.info("Chargement des réalisateurs de test-data.sql.");
	final Iterator<Réalisateur> réalisateurs =
		chargerNommés(Réalisateur.class);

	final Réalisateur georgeLucas = réalisateurs.next();
	assertEquals(UN, georgeLucas.getId().intValue());
	assertEquals("George Lucas", georgeLucas.getNom());

	final Réalisateur stevenSpielberg = réalisateurs.next();
	assertEquals(ZÉRO, stevenSpielberg.getId().intValue());
	assertEquals("Steven Spielberg", stevenSpielberg.getNom());

	assertFalse(réalisateurs.hasNext());
    }

    /**
     * Teste le chargement des scénaristes insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerScénaristes() {
	logger.info("Chargement des scénaristes de test-data.sql.");
	final Iterator<Scénariste> scénaristes =
		chargerNommés(Scénariste.class);

	final Scénariste vanHamme = scénaristes.next();
	assertEquals(ZÉRO, vanHamme.getId().intValue());
	assertEquals("Jean Van Hamme", vanHamme.getNom());

	final Scénariste renard = scénaristes.next();
	assertEquals(UN, renard.getId().intValue());
	assertEquals("Renard", renard.getNom());

	assertFalse(scénaristes.hasNext());
    }

    /**
     * Teste le chargement des séries insérées dans la base de données par le
     * fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerSéries() {
	logger.info("Chargement des séries de test-data.sql.");
	final Iterator<Série> séries = chargerNommés(Série.class);

	final Série bouleEtBill = séries.next();
	assertEquals(ZÉRO, bouleEtBill.getId().intValue());
	assertEquals("Boule et Bill", bouleEtBill.getNom());

	final Série merlin = séries.next();
	assertEquals(UN, merlin.getId().intValue());
	assertEquals("Merlin", merlin.getNom());

	assertFalse(séries.hasNext());
    }

    /**
     * Teste le chargement des utilisateurs insérés dans la base de données par
     * le fichier src/test/resources/test-data.sql.
     */
    @Test
    public void chargerUtilisateurs() {
	logger.info("Chargement des utilisateurs de test-data.sql.");
	final Iterator<Utilisateur> utilisateurs =
		charger(Utilisateur.class, "id");

	final Utilisateur etienne = utilisateurs.next();
	assertEquals(ZÉRO, etienne.getId().intValue());
	assertEquals("Etienne", etienne.getNom());
	assertTrue(etienne.vérifierMdp("qwerty"));

	final Utilisateur grégoire = utilisateurs.next();
	assertEquals(UN, grégoire.getId().intValue());
	assertEquals("Grégoire", grégoire.getNom());
	assertTrue(grégoire.vérifierMdp("azerty"));

	final Utilisateur claire = utilisateurs.next();
	assertEquals(DEUX, claire.getId().intValue());
	assertEquals("Claire", claire.getNom());
	assertTrue(claire.vérifierMdp("AZERTY"));

	assertFalse(utilisateurs.hasNext());
    }

    /**
     * Teste le chargement des fiches insérées dans la base de données par le
     * fichier src/test/resources/test-data.sql.
     *
     * @throws ParseException
     *             Jamais.
     */
    @Test
    public void chargerFiches() throws ParseException {
	logger.info("Chargement des fiches de test-data.sql.");
	final Iterator<Fiche> fiches = charger(Fiche.class, "titre");
	logger.debug("Fiches chargées.");

	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	final BandeDessinée bouleEtBill = (BandeDessinée) fiches.next();
	assertEquals(ZÉRO, bouleEtBill.getId().intValue());
	assertEquals("Globe-trotters", bouleEtBill.getTitre());
	assertEquals("Boule et Bill", bouleEtBill.getSérie().getNom());
	assertNull(bouleEtBill.getCommentaire());
	assertNull(bouleEtBill.getImage());
	assertEquals("Claire", bouleEtBill.getPropriétaire().getNom());
	assertEquals("Verneuil", bouleEtBill.getEmplacement().getNom());
	assertEquals("Etienne", bouleEtBill.getCréateur().getNom());
	assertEquals(df.parse("2012-12-25 22:18:30"),
		bouleEtBill.getCréation());
	assertEquals("Grégoire", bouleEtBill.getDernierÉditeur().getNom());
	assertEquals(df.parse("2013-02-16 22:19:58"),
		bouleEtBill.getDernièreÉdition());
	assertEquals("Jigounov", bouleEtBill.getDessinateur().getNom());
	assertEquals("Renard", bouleEtBill.getScénariste().getNom());
	assertEquals(DOUZE, bouleEtBill.getNuméro().intValue());

	final Film merlin = (Film) fiches.next();
	assertEquals(UN, merlin.getId().intValue());
	assertEquals("Merlin, Saison 1", merlin.getTitre());
	assertEquals("Merlin", merlin.getSérie().getNom());
	assertEquals("Une super série !", merlin.getCommentaire());
	assertNull(merlin.getImage());
	assertEquals("Etienne", merlin.getPropriétaire().getNom());
	assertEquals("Verneuil", merlin.getEmplacement().getNom());
	assertEquals("Etienne", merlin.getCréateur().getNom());
	assertEquals(df.parse("2012-12-25 22:21:29"), merlin.getCréation());
	assertEquals("Claire", merlin.getDernierÉditeur().getNom());
	assertEquals(df.parse("2013-02-26 22:22:06"),
		merlin.getDernièreÉdition());
	assertEquals(Support.BRD, merlin.getSupport());
	assertNull(merlin.getRéalisateur());
	assertEquals("Howard Shore", merlin.getCompositeur().getNom());
	assertEquals(DEUX, merlin.getActeurs().size());

	assertFalse(fiches.hasNext());
    }

}
