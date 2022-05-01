package fr.elimerl.registre.entities;

import fr.elimerl.registre.entities.Movie.Support;
import fr.elimerl.registre.entities.Reference.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import static fr.elimerl.registre.entities.Movie.Support.BRD;
import static org.junit.Assert.*;

/**
 * In this JUnit test, we check the JPA mapping, the database schema, and that
 * they match.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class PersistenceTest {

    /** Username that don’t exist in the database. */
    private static final String USER = "Etienne Miret";

    /** Email address belonging to no registered user. */
    private static final String EMAIL = "etienne.miret@ens-lyon.org";

    /** Name that must not belongs to any {@link Named} in the database. */
    private static final String NAME = "Test de persistence JUnit";

    /** Word that must not exist in the database dictionary. */
    private static final String WORD = "Test JUnit";

    /** The integer 0, as a constant. */
    private static final int ZERO = 0;

    /** The integer 1, as a constant. */
    private static final int ONE = 1;

    /** The integer 2, as a constant. */
    private static final int TWO = 2;

    /** The integer 3, as a constant. */
    private static final int THREE = 3;

    /** The integer 4, as a constant. */
    private static final int FOUR = 4;

    /** The integer 5, as a constant. */
    private static final int FIVE = 5;

    /** The integer 6, as a constant. */
    private static final int SIX = 6;

    /** The integer 7, as a constant. */
    private static final int SEVEN = 7;

    /** The integer 8, as a constant. */
    private static final int EIGHT = 8;

    /** The integer 9, as a constant. */
    private static final int NINE = 9;

    /** The integer 12, as a constant. */
    private static final int TWELVE = 12;

    /** This class’ logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(PersistenceTest.class);

    /**
     * Spring provided {@link EntityManager} factory.
     */
    @Resource(name = "embeddedEmf")
    private EntityManagerFactory emf;

    /**
     * Entity manager.
     */
    private EntityManager em;

    /**
     * Query builder.
     */
    private CriteriaBuilder builder;

    /**
     * Setup the environment for the tests.
     */
    @Before
    public void setUp() {
	builder = emf.getCriteriaBuilder();
	em = emf.createEntityManager();
	em.getTransaction().begin();
    }

    /**
     * Tear down the test environment. Notably, rollback the current
     * transaction.
     */
    @After
    public void tearDown() {
	em.getTransaction().rollback();
	em.close();
    }

    /**
     * Test that the given {@link Named} can be saved to the database. This
     * method is called for each {@code Named} implementation.
     *
     * @param named
     *          named not in the database yet.
     */
    private void save(final Named named) {
	final String name = named.getName();
	assertNull(named.getId());
	final Named savedNamed = em.merge(named);
	em.flush();
	assertNotNull(savedNamed.getId());
	assertEquals(name, savedNamed.getName());
    }

    /**
     * Test that an actor can be stored in the database.
     */
    @Test
    public void actorSaving() {
	logger.info("Test actor saving.");
	save(new Actor(NAME));
    }

    /**
     * Test that an author can be stored in the database.
     */
    @Test
    public void authorSaving() {
	logger.info("Test author saving.");
	save(new Author(NAME));
    }

    /**
     * Test that a composer can be stored in the database.
     */
    @Test
    public void composerSaving() {
	logger.info("Test composer saving.");
	save(new Composer(NAME));
    }

    /**
     * Test that a cartoonist can be stored in the database.
     */
    @Test
    public void cartoonistSaving() {
	logger.info("Test cartoonist saving.");
	save(new Cartoonist(NAME));
    }

    /**
     * Test that a location can be stored in the database.
     */
    @Test
    public void locationSaving() {
	logger.info("Test location saving.");
	save(new Location(NAME));
    }

    /**
     * Test that an owner can be stored in the database.
     */
    @Test
    public void ownerSaving() {
	logger.info("Test owner saving.");
	save(new Owner(NAME));
    }

    /**
     * Test that a director can be store in the database.
     */
    @Test
    public void directorSaving() {
	logger.info("Director saving test.");
	save(new Director(NAME));
    }

    /**
     * Test that a script writer can be stored in the database.
     */
    @Test
    public void scriptWriterSaving() {
	logger.info("Test script writer saving.");
	save(new ScriptWriter(NAME));
    }

    /**
     * Test that a series can be stored in the database.
     */
    @Test
    public void seriesSaving() {
	logger.info("Test series saving.");
	save(new Series(NAME));
    }

    /**
     * Test that a user can be stored in the database.
     */
    @Test
    public void userSaving() {
	logger.info("Test user saving.");
	User utilisateur = new User(USER, EMAIL);
	assertNull(utilisateur.getId());
	utilisateur = em.merge(utilisateur);
	em.flush();
	assertNotNull(utilisateur.getId());
	assertEquals(USER, utilisateur.getName());
	assertEquals(EMAIL, utilisateur.getEmail());
    }

    /**
     * Test that a session can be stored in the database.
     */
    @Test
    public void sessionSaving() {
	logger.info("Test session saving.");
	User user = new User(USER, EMAIL);
	user = em.merge(user);
	Session session = new Session(user, 0L);
	final String key = session.getKey();
	final Date expiration = session.getExpiration();
	session = em.merge(session);
	assertEquals(user, session.getUser());
	assertEquals(key, session.getKey());
	assertEquals(expiration, session.getExpiration());
    }

    /**
     * Test that a comic can be stored in the database. All fields from
     * {@link Comic} are tested, included those inherited from {@link Record}.
     */
    @Test
    public void comicSaving() {
	logger.info("Test comic saving.");

	final String title = "Une super BD";
	User creator = new User("Créateur", "createur@email");
	User modifier = new User("Éditeur", "editeur@email");
	creator = em.merge(creator);
	modifier = em.merge(modifier);
	Comic comic = new Comic(title, creator);
	final Date creation = comic.getCreation();
	assertNull(comic.getId());

	comic = em.merge(comic);
	em.flush();

	assertNotNull(comic.getId());
	assertEquals(title, comic.getTitle());
	assertNull(comic.getSeries());
	assertNull(comic.getComment());
	assertNull(comic.getPicture());
	assertNull(comic.getOwner());
	assertNull(comic.getLocation());
	assertEquals(creator, comic.getCreator());
	assertEquals(creation, comic.getCreation());
	assertEquals(creator, comic.getLastModifier());
	assertEquals(creation, comic.getLastModification());
	assertNull(comic.getCartoonist());
	assertNull(comic.getScriptWriter());
	assertNull(comic.getNumber());

	final Long id = comic.getId();
	Series series = new Series(NAME);
	series = em.merge(series);
	final String comment = "Bonjour les gens !";
	final String picture = UUID.randomUUID().toString();
	Owner owner = new Owner(NAME);
	owner = em.merge(owner);
	Location location = new Location(NAME);
	location = em.merge(location);
	Cartoonist cartoonist = new Cartoonist(NAME);
	cartoonist = em.merge(cartoonist);
	ScriptWriter scriptWriter = new ScriptWriter(NAME);
	scriptWriter = em.merge(scriptWriter);
	final Integer number = Integer.valueOf(12);

	comic.setSeries(series);
	comic.setComment(comment);
	comic.setPicture(picture);
	comic.setOwner(owner);
	comic.setLocation(location);
	comic.setCartoonist(cartoonist);
	comic.setScriptWriter(scriptWriter);
	comic.setNumber(number);
	comic.toucher(modifier);
	final Date lastModification = comic.getLastModification();

	comic = em.merge(comic);

	assertEquals(id, comic.getId());
	assertEquals(title, comic.getTitle());
	assertEquals(series, comic.getSeries());
	assertEquals(comment, comic.getComment());
	assertEquals(picture, comic.getPicture());
	assertEquals(owner, comic.getOwner());
	assertEquals(location, comic.getLocation());
	assertEquals(creator, comic.getCreator());
	assertEquals(creation, comic.getCreation());
	assertEquals(modifier, comic.getLastModifier());
	assertEquals(lastModification, comic.getLastModification());
	assertEquals(cartoonist, comic.getCartoonist());
	assertEquals(scriptWriter, comic.getScriptWriter());
	assertEquals(number, comic.getNumber());
    }

    /**
     * Test that a movie can be stored in the database. Fields inherited from
     * {@link Record} aren’t tested because they were in {@link #comicSaving()}.
     */
    @Test
    public void movieSaving() {
	logger.info("Test movie saving.");

	final String title = "Un super film";
	User creator = new User("Créateur", "createur@email");
	User modifier = new User("Éditeur", "editeur@email");
	creator = em.merge(creator);
	modifier = em.merge(modifier);
	Movie movie = new Movie(title, creator, BRD);

	movie = em.merge(movie);

	assertEquals(BRD, movie.getSupport());
	assertNull(movie.getDirector());
	assertNotNull(movie.getActors());
	assertTrue(movie.getActors().isEmpty());
	assertNull(movie.getComposer());
	assertNull(movie.getActionStyle());
	assertNull(movie.getDocumentaryStyle());
	assertNull(movie.getFantasyStyle());
	assertNull(movie.getWarStyle());
	assertNull(movie.getTrueStoryStyle());
	assertNull(movie.getHistoricalStyle());
	assertNull(movie.getHumorStyle());
	assertNull(movie.getDetectiveStyle());
	assertNull(movie.getRomanticStyle());
	assertNull(movie.getSfStyle());

	Director director = new Director(NAME);
	director = em.merge(director);
	Actor actor1 = new Actor("Très bon acteur");
	actor1 = em.merge(actor1);
	Actor actor2 = new Actor("Acteur moyen");
	actor2 = em.merge(actor2);
	Actor actor3 = new Actor("Mauvais acteur");
	actor3 = em.merge(actor3);
	Composer compositeur = new Composer(NAME);
	compositeur = em.merge(compositeur);

	movie.setDirector(director);
	movie.getActors().add(actor1);
	movie.getActors().add(actor2);
	movie.getActors().add(actor3);
	movie.setComposer(compositeur);
	movie.setActionStyle(Boolean.TRUE);
	movie.setDocumentaryStyle(Boolean.FALSE);
	movie.setFantasyStyle(Boolean.TRUE);
	movie.setWarStyle(Boolean.FALSE);
	movie.setTrueStoryStyle(Boolean.TRUE);
	movie.setHistoricalStyle(Boolean.FALSE);
	movie.setHumorStyle(Boolean.TRUE);
	movie.setDetectiveStyle(Boolean.FALSE);
	movie.setRomanticStyle(Boolean.TRUE);
	movie.setSfStyle(Boolean.FALSE);
	movie.toucher(modifier);

	movie = em.merge(movie);

	assertEquals(director, movie.getDirector());
	assertNotNull(movie.getActors());
	assertEquals(THREE, movie.getActors().size());
	assertTrue(movie.getActors().contains(actor1));
	assertTrue(movie.getActors().contains(actor2));
	assertTrue(movie.getActors().contains(actor3));
	assertEquals(compositeur, movie.getComposer());
	assertEquals(Boolean.TRUE, movie.getActionStyle());
	assertEquals(Boolean.FALSE, movie.getDocumentaryStyle());
	assertEquals(Boolean.TRUE, movie.getFantasyStyle());
	assertEquals(Boolean.FALSE, movie.getWarStyle());
	assertEquals(Boolean.TRUE, movie.getTrueStoryStyle());
	assertEquals(Boolean.FALSE, movie.getHistoricalStyle());
	assertEquals(Boolean.TRUE, movie.getHumorStyle());
	assertEquals(Boolean.FALSE, movie.getDetectiveStyle());
	assertEquals(Boolean.TRUE, movie.getRomanticStyle());
	assertEquals(Boolean.FALSE, movie.getSfStyle());
    }

    /**
     * Test that a book can be stored in the database. Fields inherited from
     * {@link Record} aren’t tested because they were in {@link #comicSaving()}.
     */
    @Test
    public void bookSaving() {
	logger.info("Test book saving.");

	final String titre = "L’Assassin royal";
	User creator = new User("Créateur", "createur@email");
	User modifier = new User("Éditeur", "editeur@email");
	creator = em.merge(creator);
	modifier = em.merge(modifier);
	Book book = new Book(titre, creator);

	book = em.merge(book);

	assertNull(book.getAuthor());
	assertNull(book.getFantasyStyle());
	assertNull(book.getTrueStoryStyle());
	assertNull(book.getHistoricalStyle());
	assertNull(book.getHumorStyle());
	assertNull(book.getDetectiveStyle());
	assertNull(book.getRomanticStyle());
	assertNull(book.getSfStyle());

	Author author = new Author("Robin Hobb");
	author = em.merge(author);

	book.setAuthor(author);
	book.setFantasyStyle(Boolean.TRUE);
	book.setTrueStoryStyle(Boolean.FALSE);
	book.setHistoricalStyle(Boolean.TRUE);
	book.setHumorStyle(Boolean.FALSE);
	book.setDetectiveStyle(Boolean.TRUE);
	book.setRomanticStyle(Boolean.FALSE);
	book.setSfStyle(Boolean.TRUE);

	book = em.merge(book);

	assertEquals(author, book.getAuthor());
	assertEquals(Boolean.TRUE, book.getFantasyStyle());
	assertEquals(Boolean.FALSE, book.getTrueStoryStyle());
	assertEquals(Boolean.TRUE, book.getHistoricalStyle());
	assertEquals(Boolean.FALSE, book.getHumorStyle());
	assertEquals(Boolean.TRUE, book.getDetectiveStyle());
	assertEquals(Boolean.FALSE, book.getRomanticStyle());
	assertEquals(Boolean.TRUE, book.getSfStyle());
    }

    /**
     * Test that a word can be stored in the database.
     */
    @Test
    public void wordSaving() {
	logger.info("Test word saving.");
	Word mot = new Word(WORD);
	assertNull(mot.getId());
	mot = em.merge(mot);
	em.flush();
	assertNotNull(mot.getId());
	assertEquals(WORD, mot.getValue());
    }

    /**
     * Test that a reference can be stored in the database.
     */
    @Test
    public void referenceSaving() {
	logger.info("Test reference saving.");

	final String title = "Titre";
	final Field field = Field.TITLE;
	User creator = new User(USER, EMAIL);
	creator = em.merge(creator);
	Record record = new Movie(title, creator, BRD);
	record = em.merge(record);
	Word word = new Word(WORD);
	word = em.merge(word);
	Reference reference = new Reference(word, field, record);
	assertNull(reference.getId());

	reference = em.merge(reference);
	em.flush();

	assertNotNull(reference.getId());
	assertEquals(word, reference.getWord());
	assertEquals(field, reference.getField());
	assertEquals(record, reference.getRecord());
    }

    /**
     * Test saving two actors with the same name. Since this is not allowed, an
     * error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarActors() {
	logger.info("Test saving two similar actors.");
	em.merge(new Actor(NAME));
	em.merge(new Actor(NAME));
	em.flush();
    }

    /**
     * Test saving two authors with the same name. Since this is not allowed, an
     * error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarAuthors() {
	logger.info("Test saving two similar authors.");
	em.merge(new Author(NAME));
	em.merge(new Author(NAME));
	em.flush();
    }

    /**
     * Test saving two composers with the same name. Since this is not allowed,
     * an error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarComposers() {
	logger.info("Test saving two similar composers.");
	em.merge(new Composer(NAME));
	em.merge(new Composer(NAME));
	em.flush();
    }

    /**
     * Test saving two cartoonists with the same name. Since this is not
     * allowed, an error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarCartoonists() {
	logger.info("Test saving two similar cartoonists.");
	em.merge(new Cartoonist(NAME));
	em.merge(new Cartoonist(NAME));
	em.flush();
    }

    /**
     * Test saving two locations with the same name. Since this is not allowed,
     * an error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarLocations() {
	logger.info("Test saving two similar locations.");
	em.merge(new Location(NAME));
	em.merge(new Location(NAME));
	em.flush();
    }

    /**
     * Test saving two owners with the same name. Since this is not allowed, an
     * error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarOwners() {
	logger.info("Test saving two similar owners.");
	em.merge(new Owner(NAME));
	em.merge(new Owner(NAME));
	em.flush();
    }

    /**
     * Test saving two directors with the same name. Since this is not allowed,
     * an error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarDirectors() {
	logger.info("Test saving two similar directors.");
	em.merge(new Director(NAME));
	em.merge(new Director(NAME));
	em.flush();
    }

    /**
     * Test saving two script writers with the same name. Since this is not
     * allowed, an error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarScriptWriters() {
	logger.info("Test saving two similar script writers.");
	em.merge(new ScriptWriter(NAME));
	em.merge(new ScriptWriter(NAME));
	em.flush();
    }

    /**
     * Test saving two series with the same name. Since this is not allowed, an
     * error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarSeries() {
	logger.info("Test saving two similar series.");
	em.merge(new Series(NAME));
	em.merge(new Series(NAME));
	em.flush();
    }

    /**
     * Test saving two users with the same name. Since this is not allowed, an
     * error is expected.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarUsers() {
	logger.info("Test saving two similar users.");
	em.merge(new User(USER, "email1@email"));
	em.merge(new User(USER, "email2@email"));
	em.flush();
    }

    /**
     * Test saving different {@link Named} with the same name, from different
     * subclasses. In this case, there must be no error.
     */
    @Test
    public void differentNamedWithSameName() {
	logger.info("Test saving different named with same name.");
	em.merge(new Actor(NAME));
	em.merge(new Author(NAME));
	em.merge(new Composer(NAME));
	em.merge(new Cartoonist(NAME));
	em.merge(new Location(NAME));
	em.merge(new Owner(NAME));
	em.merge(new Director(NAME));
	em.merge(new ScriptWriter(NAME));
	em.merge(new Series(NAME));
	em.flush();
    }

    /**
     * Test saving two similar words. An error is expected in this case.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarWords() {
	logger.info("Test saving two similar words.");
	em.merge(new Word(WORD));
	em.merge(new Word(WORD));
	em.flush();
    }

    /**
     * Test saving two similar references. An error is expected in this case.
     */
    @Test(expected = PersistenceException.class)
    public void twoSimilarReferences() {
	logger.info("Test saving two similar references.");

	final String title = "Titre";
	final Field field = Reference.Field.TITLE;
	User creator = new User(USER, EMAIL);
	creator = em.merge(creator);
	Record fiche = new Movie(title, creator, BRD);
	fiche = em.merge(fiche);
	Word word = new Word(WORD);
	word = em.merge(word);

	em.merge(new Reference(word, field, fiche));
	em.merge(new Reference(word, field, fiche));
	em.flush();
    }

    /**
     * Test saving two record with the same title. This is allowed so there must
     * be no errro.
     */
    @Test
    public void twoSimilarRecords() {
	logger.info("Test saving two similar records.");

	final String title = "Lettres d’Iwo Jima";
	final User creator =
		em.merge(new User(USER, EMAIL));
	final Record record1 = new Movie(title, creator, BRD);
	final Record record2 = new Comic(title, creator);

	em.merge(record1);
	em.merge(record2);
	em.flush();
    }

    /**
     * Load all objects of the given type from the database.
     *
     * @param type
     *          class of the objects to load.
     * @param <T>
     *          type of the objects to load.
     * @param order
     *          attribute which to sort the loaded objects on.
     * @return all database objects of the {@code type} class, sorted in
     *          ascending order according to their {@code order} attribute.
     */
    private <T> Iterator<T> load(final Class<T> type, final String order) {
	final CriteriaQuery<T> query = builder.createQuery(type);
	final Root<T> root = query.from(type);
	query.orderBy(builder.asc(root.get(order)));
	return em.createQuery(query).getResultList().iterator();
    }

    /**
     * Load from the database all named of the give type.
     *
     * @param type
     *          class of the objects to load.
     * @param <T>
     *          type of the objects to load.
     * @return all objects from the {@code type} class in the database, sorted
     *          by ascending names.
     */
    private <T extends Named> Iterator<T> loadNamed(final Class<T> type) {
	return load(type, "name");
    }

    /**
     * Test loading actors inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadActors() {
	logger.info("Loading actors from test-data.sql.");
	final Iterator<Actor> actors = loadNamed(Actor.class);

	final Actor antonyHead = actors.next();
	assertEquals(FIVE, antonyHead.getId().intValue());
	assertEquals("Anthony Head", antonyHead.getName());

	final Actor bradleyJames = actors.next();
	assertEquals(SEVEN, bradleyJames.getId().intValue());
	assertEquals("Bradley James", bradleyJames.getName());

	final Actor colinMorgan = actors.next();
	assertEquals(EIGHT, colinMorgan.getId().intValue());
	assertEquals("Colin Morgan", colinMorgan.getName());

	final Actor emmaWatson = actors.next();
	assertEquals(THREE, emmaWatson.getId().intValue());
	assertEquals("Emma Watson", emmaWatson.getName());

	final Actor evaMendes = actors.next();
	assertEquals(ONE, evaMendes.getId().intValue());
	assertEquals("Eva Mendes", evaMendes.getName());

	final Actor georgeClooney = actors.next();
	assertEquals(TWO, georgeClooney.getId().intValue());
	assertEquals("George Clooney", georgeClooney.getName());

	final Actor marilynMonroe = actors.next();
	assertEquals(SIX, marilynMonroe.getId().intValue());
	assertEquals("Marilyn Monroe", marilynMonroe.getName());

	final Actor scarlettJohansson = actors.next();
	assertEquals(FOUR, scarlettJohansson.getId().intValue());
	assertEquals("Scarlett Johansson", scarlettJohansson.getName());

	final Actor willSmith = actors.next();
	assertEquals(ZERO, willSmith.getId().intValue());
	assertEquals("Will Smith", willSmith.getName());

	assertFalse(actors.hasNext());
    }

    /**
     * Test loading authors inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadAuthors() {
	logger.info("Loading authors from test-data.sql.");
	final Iterator<Author> authors = loadNamed(Author.class);

	final Author gavThorpe = authors.next();
	assertEquals(THREE, gavThorpe.getId().intValue());
	assertEquals("Gav Thorpe", gavThorpe.getName());

	final Author asimov = authors.next();
	assertEquals(ONE, asimov.getId().intValue());
	assertEquals("Isaac Asimov", asimov.getName());

	final Author grisham = authors.next();
	assertEquals(TWO, grisham.getId().intValue());
	assertEquals("John Grisham", grisham.getName());

	final Author noickKyme = authors.next();
	assertEquals(FOUR, noickKyme.getId().intValue());
	assertEquals("Noick Kyme", noickKyme.getName());

	final Author clancy = authors.next();
	assertEquals(ZERO, clancy.getId().intValue());
	assertEquals("Tom Clancy", clancy.getName());

	assertFalse(authors.hasNext());
    }

    /**
     * Test loading actors inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadComposers() {
	logger.info("Loading composers from test-data.sql.");
	final Iterator<Composer> composers = loadNamed(Composer.class);

	final Composer hansZimmer = composers.next();
	assertEquals(ONE, hansZimmer.getId().intValue());
	assertEquals("Hans Zimmer", hansZimmer.getName());

	final Composer howardShore = composers.next();
	assertEquals(ZERO, howardShore.getId().intValue());
	assertEquals("Howard Shore", howardShore.getName());

	final Composer lisaGerrard = composers.next();
	assertEquals(TWO, lisaGerrard.getId().intValue());
	assertEquals("Lisa Gerrard", lisaGerrard.getName());

	assertFalse(composers.hasNext());
    }

    /**
     * Test loading cartoonists inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadCartoonists() {
	logger.info("Loading cartoonists from test-data.sql.");
	final Iterator<Cartoonist> cartoonists = loadNamed(Cartoonist.class);

	final Cartoonist alainHenriet = cartoonists.next();
	assertEquals(ONE, alainHenriet.getId().intValue());
	assertEquals("Alain Henriet", alainHenriet.getName());

	final Cartoonist jigounov = cartoonists.next();
	assertEquals(ZERO, jigounov.getId().intValue());
	assertEquals("Jigounov", jigounov.getName());

	final Cartoonist morris = cartoonists.next();
	assertEquals(TWO, morris.getId().intValue());
	assertEquals("Morris", morris.getName());

	assertFalse(cartoonists.hasNext());
    }

    /**
     * Test loading locations inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadLocations() {
	logger.info("Loading locations from test-data.sql.");
	final Iterator<Location> locations = loadNamed(Location.class);

	final Location laRocheSurYon = locations.next();
	assertEquals(ONE, laRocheSurYon.getId().intValue());
	assertEquals("La Roche sur Yon", laRocheSurYon.getName());

	final Location lyon = locations.next();
	assertEquals(TWO, lyon.getId().intValue());
	assertEquals("Lyon", lyon.getName());

	final Location poissy = locations.next();
	assertEquals(FOUR, poissy.getId().intValue());
	assertEquals("Poissy", poissy.getName());

	final Location singapour = locations.next();
	assertEquals(THREE, singapour.getId().intValue());
	assertEquals("Singapour", singapour.getName());

	final Location verneuil = locations.next();
	assertEquals(ZERO, verneuil.getId().intValue());
	assertEquals("Verneuil", verneuil.getName());

	assertFalse(locations.hasNext());
    }

    /**
     * Test loading owners inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadOwners() {
	logger.info("Loading owners from test-data.sql.");
	final Iterator<Owner> owners = loadNamed(Owner.class);

	final Owner claire = owners.next();
	assertEquals(TWO, claire.getId().intValue());
	assertEquals("Claire", claire.getName());

	final Owner etienne = owners.next();
	assertEquals(ZERO, etienne.getId().intValue());
	assertEquals("Etienne", etienne.getName());

	final Owner gregoire = owners.next();
	assertEquals(ONE, gregoire.getId().intValue());
	assertEquals("Grégoire", gregoire.getName());

	assertFalse(owners.hasNext());
    }

    /**
     * Test loading directors inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadingDirectors() {
	logger.info("Loading directors from test-data.sql.");
	final Iterator<Director> directors = loadNamed(Director.class);

	final Director georgeLucas = directors.next();
	assertEquals(ONE, georgeLucas.getId().intValue());
	assertEquals("George Lucas", georgeLucas.getName());

	final Director lucBesson = directors.next();
	assertEquals(TWO, lucBesson.getId().intValue());
	assertEquals("Luc Besson", lucBesson.getName());

	final Director stevenSpielberg = directors.next();
	assertEquals(ZERO, stevenSpielberg.getId().intValue());
	assertEquals("Steven Spielberg", stevenSpielberg.getName());

	assertFalse(directors.hasNext());
    }

    /**
     * Test loading script writers inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadScriptWriters() {
	logger.info("Loading script-writers from test-data.sql.");
	final Iterator<ScriptWriter> scriptWriters =
		loadNamed(ScriptWriter.class);

	final ScriptWriter vanHamme = scriptWriters.next();
	assertEquals(ZERO, vanHamme.getId().intValue());
	assertEquals("Jean Van Hamme", vanHamme.getName());

	final ScriptWriter renard = scriptWriters.next();
	assertEquals(ONE, renard.getId().intValue());
	assertEquals("Renard", renard.getName());

	final ScriptWriter goscinny = scriptWriters.next();
	assertEquals(TWO, goscinny.getId().intValue());
	assertEquals("René Goscinny", goscinny.getName());

	assertFalse(scriptWriters.hasNext());
    }

    /**
     * Test loading series inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadSeries() {
	logger.info("Loading series from test-data.sql.");
	final Iterator<Series> series = loadNamed(Series.class);

	final Series bouleEtBill = series.next();
	assertEquals(ZERO, bouleEtBill.getId().intValue());
	assertEquals("Boule et Bill", bouleEtBill.getName());

	final Series harryPotter = series.next();
	assertEquals(THREE, harryPotter.getId().intValue());
	assertEquals("Harry Potter", harryPotter.getName());

	final Series lukyLuke = series.next();
	assertEquals(FOUR, lukyLuke.getId().intValue());
	assertEquals("Luky Luke", lukyLuke.getName());

	final Series merlin = series.next();
	assertEquals(ONE, merlin.getId().intValue());
	assertEquals("Merlin", merlin.getName());

	final Series warhammer40k = series.next();
	assertEquals(TWO, warhammer40k.getId().intValue());
	assertEquals("Warhammer 40,000", warhammer40k.getName());

	assertFalse(series.hasNext());
    }

    /**
     * Test loading users inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadUsers() {
	logger.info("Loading users from test-data.sql.");
	final Iterator<User> users = load(User.class, "id");

	final User etienne = users.next();
	assertEquals(ZERO, etienne.getId().intValue());
	assertEquals("Etienne", etienne.getName());
	assertEquals("etienne@email", etienne.getEmail());

	final User gregoire = users.next();
	assertEquals(ONE, gregoire.getId().intValue());
	assertEquals("Grégoire", gregoire.getName());
	assertEquals("gregoire@email", gregoire.getEmail());

	final User claire = users.next();
	assertEquals(TWO, claire.getId().intValue());
	assertEquals("Claire", claire.getName());
	assertEquals("claire@email", claire.getEmail());

	assertFalse(users.hasNext());
    }

    /**
     * Test loading records inserted in the database by the
     * src/test/resources/test-data.sql file. All fields of one comic, one movie
     * and one book are tested (each implementation of {@link Record}). For
     * other records, only title and id are tested.
     *
     * @throws ParseException
     *          Never.
     */
    @Test
    public void loadRecords() throws ParseException {
	logger.info("Loading users from test-data.sql.");
	final Iterator<Record> records = load(Record.class, "title");
	logger.debug("Records loaded.");

	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	final Comic alerteAuxPiedsBleus = (Comic) records.next();
	assertEquals(SIX, alerteAuxPiedsBleus.getId().intValue());
	assertEquals("Alerte aux Pieds-Bleus", alerteAuxPiedsBleus.getTitle());

	final Comic bouleEtBill = (Comic) records.next();
	assertEquals(ZERO, bouleEtBill.getId().intValue());
	assertEquals("Globe-trotters", bouleEtBill.getTitle());
	assertEquals("Boule et Bill", bouleEtBill.getSeries().getName());
	assertNull(bouleEtBill.getComment());
	assertNull(bouleEtBill.getPicture());
	assertEquals("Claire", bouleEtBill.getOwner().getName());
	assertEquals("Verneuil", bouleEtBill.getLocation().getName());
	assertEquals("Etienne", bouleEtBill.getCreator().getName());
	assertEquals(df.parse("2012-12-25 22:18:30"),
		bouleEtBill.getCreation());
	assertEquals("Grégoire", bouleEtBill.getLastModifier().getName());
	assertEquals(df.parse("2013-02-16 22:19:58"),
		bouleEtBill.getLastModification());
	assertEquals("Jigounov", bouleEtBill.getCartoonist().getName());
	assertEquals("Renard", bouleEtBill.getScriptWriter().getName());
	assertEquals(TWELVE, bouleEtBill.getNumber().intValue());

	final Book laChuteDeDamnos = (Book) records.next();
	assertEquals(FIVE, laChuteDeDamnos.getId().intValue());
	assertEquals("La Chute de Damnos", laChuteDeDamnos.getTitle());

	final Comic luckyLukeFiancee = (Comic) records.next();
	assertEquals(SEVEN, luckyLukeFiancee.getId().intValue());
	assertEquals("La Fiancée de Luky Luke", luckyLukeFiancee.getTitle());

	final Book laPurgeDeKadillus = (Book) records.next();
	assertEquals(FOUR, laPurgeDeKadillus.getId().intValue());
	assertEquals("La Purge de Kadillus", laPurgeDeKadillus.getTitle());

	final Comic lePonyExpress = (Comic) records.next();
	assertEquals(EIGHT, lePonyExpress.getId().intValue());
	assertEquals("Le Pony Express", lePonyExpress.getTitle());

	final Movie lucy = (Movie) records.next();
	assertEquals(THREE, lucy.getId().intValue());
	assertEquals("Lucy", lucy.getTitle());

	final Movie lukyMarines = (Movie) records.next();
	assertEquals(NINE, lukyMarines.getId().intValue());
	assertEquals("Luky Marines", lukyMarines.getTitle());

	final Movie merlin = (Movie) records.next();
	assertEquals(ONE, merlin.getId().intValue());
	assertEquals("Merlin, Saison 1", merlin.getTitle());
	assertEquals("Merlin", merlin.getSeries().getName());
	assertEquals("Une super série !", merlin.getComment());
	assertNull(merlin.getPicture());
	assertEquals("Etienne", merlin.getOwner().getName());
	assertEquals("Verneuil", merlin.getLocation().getName());
	assertEquals("Etienne", merlin.getCreator().getName());
	assertEquals(df.parse("2012-12-25 22:21:29"), merlin.getCreation());
	assertEquals("Claire", merlin.getLastModifier().getName());
	assertEquals(df.parse("2013-02-26 22:22:06"),
		merlin.getLastModification());
	assertEquals(Support.BRD, merlin.getSupport());
	assertNull(merlin.getDirector());
	assertEquals("Howard Shore", merlin.getComposer().getName());
	assertEquals(THREE, merlin.getActors().size());

	final Book rainbowSix = (Book) records.next();
	assertEquals(TWO, rainbowSix.getId().intValue());
	assertEquals("Rainbow Six", rainbowSix.getTitle());
	assertNull(rainbowSix.getSeries());
	assertNull(rainbowSix.getComment());
	assertNull(rainbowSix.getPicture());
	assertEquals("Etienne", rainbowSix.getOwner().getName());
	assertEquals("Verneuil", rainbowSix.getLocation().getName());
	assertEquals("Etienne", rainbowSix.getCreator().getName());

	assertFalse(records.hasNext());
    }

    /**
     * Test loading words inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadWords() {
	logger.info("Loading words from test-data.sql.");
	final Iterator<Word> mots = load(Word.class, "value");

	final Word serie = mots.next();
	assertEquals(TWO, serie.getId().intValue());
	assertEquals("série", serie.getValue());

	final Word _super = mots.next();
	assertEquals(ONE, _super.getId().intValue());
	assertEquals("super", _super.getValue());

	final Word une = mots.next();
	assertEquals(ZERO, une.getId().intValue());
	assertEquals("une", une.getValue());

	assertFalse(mots.hasNext());
    }

    /**
     * Test loading references inserted in the database by the
     * src/test/resources/test-data.sql file.
     */
    @Test
    public void loadReferences() {
	logger.info("Load references from test-data.sql.");
	final Iterator<Reference> references = load(Reference.class, "id");
	logger.debug("References loaded");

	{
	    final Reference reference = references.next();
	    assertEquals(ZERO, reference.getId().intValue());
	    assertEquals("une", reference.getWord().getValue());
	    assertEquals(Field.COMMENT, reference.getField());
	    assertEquals(ONE, reference.getRecord().getId().intValue());
	}

	{
	    final Reference reference = references.next();
	    assertEquals(ONE, reference.getId().intValue());
	    assertEquals("super", reference.getWord().getValue());
	    assertEquals(Field.COMMENT, reference.getField());
	    assertEquals(ONE, reference.getRecord().getId().intValue());
	}

	{
	    final Reference reference = references.next();
	    assertEquals(TWO, reference.getId().intValue());
	    assertEquals("série", reference.getWord().getValue());
	    assertEquals(Field.COMMENT, reference.getField());
	    assertEquals(ONE, reference.getRecord().getId().intValue());
	}

	assertFalse(references.hasNext());
    }

    /**
     * Test records unindexation through the named query “unindexRecord” defined
     * in the {@link Reference} class.
     */
    @Test
    public void unindexRecord() {
	logger.info("Unindexing records.");

	/* Unindexing record 0, which has no references. */
	final Record record0 = em.getReference(Record.class, Long.valueOf(ZERO));
	final Query unindexRecord0 = em.createNamedQuery("unindexRecord");
	unindexRecord0.setParameter("record", record0);
	assertEquals(ZERO, unindexRecord0.executeUpdate());

	/* Unindexing record 1, which has three references. */
	final Record record1 = em.getReference(Record.class, Long.valueOf(ONE));
	final Query unindexRecord1 = em.createNamedQuery("unindexRecord");
	unindexRecord1.setParameter("record", record1);
	assertEquals(THREE, unindexRecord1.executeUpdate());
    }

}
