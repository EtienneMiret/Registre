package fr.elimerl.registre.model.entities;

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
import fr.elimerl.registre.entities.Movie.Support;

/**
 * Test class for {@link Movie}.
 */
public class MovieTest {

    /**
     * Sleep time between two operations to make sure they happen at different
     * time.
     */
    private static final long SLEEP_TIME = 100L;

    /** Title of the movie under test. */
    private static final String TITLE = "À la belle étoile";

    /** A first random user. */
    private static final User CREATOR =
	    new User("Etienne", "etienne@email");

    /** A second random user. */
    private static final User MODIFIER =
	    new User("Grégoire", "gregoire@email");

    /** This class’ logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(MovieTest.class);

    /** Movie under test. */
    private Movie movie;

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
    /**
     * Asserts that a date is within a test interval.
     *
     * @param message
     *          message to display if {@code actual} isn’t in the test
     *          interval.
     * @param before
     *          start date of the test interval (inclusive).
     * @param after
     *          end date of the test interval (inclusive).
     * @param actual
     *          date to test.
     */
    private static void assertBetween(final String message, final Date before,
	    final Date after, final Date actual) {
	assertTrue(message, before.compareTo(actual) <= 0);
	assertTrue(message, actual.compareTo(after) <= 0);
    }

    /**
     * Setup the {@link #movie} that is going to be tested.
     */
    @Before
    public void setUp() {
	movie = new Movie(TITLE, CREATOR, Support.BRD);
    }

    /**
     * Test creation of a DVD, then its method {@link Movie#toucher(User)}.
     *
     * @throws Exception
     *          shouldn’t happen.
     */
    @Test
    public void createAndTouch() throws Exception {
	/*  DVD creation. */
	logger.info("Création du DVD.");
	Date before = new Date();
	final Movie dvd = new Movie(TITLE, CREATOR, Support.DVD);
	Date after = new Date();

	/* Creation checks. */
	final Date creation = dvd.getCreation();
	assertEquals("The DVD title is wrong.",
                TITLE, dvd.getTitle());
	assertEquals("The DVD isn’t a DVD.",
		Support.DVD, dvd.getSupport());
	assertBetween("The DVD creation date is wrong.",
		before, after, creation);
	assertEquals("At creation time, the last modification time" +
                        " must be set to the creation time.",
		dvd.getCreation(), dvd.getLastModification());
	assertEquals("The DVD creator is wrong.",
                CREATOR, dvd.getCreator());
	assertEquals("At creation time, the last modifier must be" +
                        " the creator.",
		dvd.getCreator(), dvd.getLastModifier());

	/* Waiting a bit. */
	logger.info("Waiting a bit.");
	Thread.sleep(SLEEP_TIME);

	/* Touching the DVD.  */
	logger.info("Touching the DVD.");
	before = new Date();
	dvd.toucher(MODIFIER);
	after = new Date();

	/* Touching checks. */
	assertBetween("The DVD last modification date is wrong.",
		before, after, dvd.getLastModification());
	assertEquals("The creation date was modified.",
		creation, dvd.getCreation());
	assertEquals("The creator was modified.",
                CREATOR, dvd.getCreator());
	assertEquals("The last modifier is wrong.",
                MODIFIER, dvd.getLastModifier());
    }

    /**
     * Test the {@link Record#getTitle()} and {@link Record#setTitle(String)}
     * methods.
     */
    @Test
    public void title() {
	final String title1 = "Bonjour Madame !";
	final String title2 = "Au revoir Monsieur !";
	logger.info("Testing title.");

	movie.setTitle(title1);
	assertEquals("The title is wrong.", title1, movie.getTitle());

	movie.setTitle(title2);
	assertEquals("The title is wrong.", title2, movie.getTitle());
    }

    /**
     * Test the {@link Record#getSeries()} and {@link Record#setSeries(Series)}
     * methods.
     */
    @Test
    public void series() {
	final Series series1 = new Series("Robin des bois");
	final Series series2 = new Series("Thierry la fronde");
	logger.info("Testing series.");

	movie.setSeries(series1);
	assertEquals("Series is wrong.", series1, movie.getSeries());

	movie.setSeries(series2);
	assertEquals("Series is wrong.", series2, movie.getSeries());

	movie.setSeries(null);
	assertNull("Series wasn’t cleared.", movie.getSeries());
    }

    /**
     * Test the {@link Record#getComment()} and the
     * {@link Record#setComment(String)} methods.
     */
    @Test
    public void comment() {
	final String comment1 = "Très bon film.";
	final String comment2 = "Très mauvais film.";
	logger.info("Comment test.");

	movie.setComment(comment1);
	assertEquals("Comment wasn’t properly set.",
		comment1, movie.getComment());

	movie.setComment(comment2);
	assertEquals("Comment wasn’t properly updated.",
		comment2, movie.getComment());
    }

    /**
     * Test the {@link Record#getPicture()} and
     * {@link Record#setPicture(String)} methods.
     */
    @Test
    public void picture() {
	final String uuid1 = UUID.randomUUID().toString();
	final String uuid2 = UUID.randomUUID().toString();
	logger.info("Picture test.");

	movie.setPicture(uuid1);
	assertEquals("Picture wasn’t properly set.",
		uuid1, movie.getPicture());

	movie.setPicture(uuid2);
	assertEquals("Picture wasn’t properly updated.",
		uuid2, movie.getPicture());

	movie.setPicture(null);
	assertNull("Picture wasn’t properly cleared.",
		movie.getPicture());
    }

    /**
     * Test the {@link Record#getOwner()} and {@link Record#setOwner(Owner)}
     * methods.
     */
    @Test
    public void owner() {
	final Owner owner1 = new Owner("Etienne");
	final Owner owner2 = new Owner("Grégoire");
	logger.info("Owner test.");

	movie.setOwner(owner1);
	assertEquals("Owner wasn’t properly setup.",
		owner1, movie.getOwner());

	movie.setOwner(owner2);
	assertEquals("Owner wasn’t properly updated.",
		owner2, movie.getOwner());

	movie.setOwner(null);
	assertNull("Owner wasn’t properly cleared.",
		movie.getOwner());
    }

    /**
     * Test the {@link Record#getLocation()} and
     * {@link Record#setLocation(Location)} methods.
     */
    @Test
    public void location() {
	final Location location1 = new Location("Chambre Etienne");
	final Location location2 = new Location("Singapour");
	logger.info("Location test.");

	movie.setLocation(location1);
	assertEquals("The location wasn’t properly set.",
		location1, movie.getLocation());

	movie.setLocation(location2);
	assertEquals("The location wasn’t properly updated.",
		location2, movie.getLocation());

	movie.setLocation(null);
	assertNull("The location wasn’t properly cleared.",
		movie.getLocation());
    }

    /**
     * Test the {@link Movie#getSupport()} and {@link Movie#setSupport(Support)}
     * methods.
     */
    @Test
    public void support() {
	final Support support1 = Support.DVD;
	final Support support2 = Support.BRD;
	logger.info("Support test.");

	movie.setSupport(support1);
	assertEquals("Support wasn’t properly set.",
		support1, movie.getSupport());

	movie.setSupport(support2);
	assertEquals("Support wasn’t properly updated.",
		support2, movie.getSupport());
    }

    /**
     * Test the {@link Movie#getDirector()} and
     * {@link Movie#setDirector(Director)} methods.
     */
    @Test
    public void director() {
	final Director director1 = new Director("Spielberg");
	final Director director2 = new Director("Marie Devautour");
	logger.info("Director test.");

	movie.setDirector(director1);
	assertEquals("The director wasn’t properly set.",
		director1, movie.getDirector());

	movie.setDirector(director2);
	assertEquals("The director wasn’t properly updated.",
		director2, movie.getDirector());

	movie.setDirector(null);
	assertNull("The director wasn’t properly cleared.",
		movie.getDirector());
    }

    /**
     * Test the {@link Movie#getComposer()} and
     * {@link Movie#setComposer(Composer)} methods.
     */
    @Test
    public void composer() {
	final Composer composer1 = new Composer("Mozart");
	final Composer composer2 = new Composer("Bethoven");
	logger.info("Composer test.");

	movie.setComposer(composer1);
	assertEquals("The composer wasn’t properly set.",
		composer1, movie.getComposer());

	movie.setComposer(composer2);
	assertEquals("The composer wasn’t properly updated.",
		composer2, movie.getComposer());

	movie.setComposer(null);
	assertNull("The composer wasn’t properly cleared.",
		movie.getComposer());
    }

}
