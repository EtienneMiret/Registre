package fr.elimerl.registre.transfer;

import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Movie.Support;
import fr.elimerl.registre.entities.Named;
import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.User;
import fr.elimerl.registre.services.Index;
import fr.elimerl.registre.services.RegistreEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static fr.elimerl.registre.entities.Movie.Support.BRD;
import static fr.elimerl.registre.entities.Movie.Support.DVD;
import static fr.elimerl.registre.entities.Movie.Support.K7;

/**
 * Migrator implementation. This service is responsible for migrating a batch
 * of records.
 */
@Service("migrator")
public class MigratorImpl implements Migrator {

    /** SLF4J logger of this class. */
    private static final Logger logger =
	    LoggerFactory.getLogger(MigratorImpl.class);

    /**
     * Assign the specified value to the specified field of the given record.
     * The specified field can be private or final, but it must belong to the
     * {@link Record} class (and not a sub-class).
     *
     * @param record
     * 		record whose field is to be modified.
     * @param name
     * 		name of the field to set.
     * @param value
     * 		value to set in the specified field.
     */
    private static void defineField(final Record record, final String name,
	    final Object value) {
	try {
	    final Field champ = Record.class.getDeclaredField(name);
	    champ.setAccessible(true);
	    champ.set(record, value);
	} catch (final SecurityException e) {
	    logger.error("A security parameter prevents modification of the" +
		    " {} field.", name, e);
	} catch (final NoSuchFieldException e) {
	    logger.error("The {} field does not exist.", name, e);
	} catch (final IllegalArgumentException e) {
	    logger.error("{} is of the wrong type for field {}.", value, name, e);
	} catch (final IllegalAccessException e) {
	    logger.error("The {} field can't be modified.", name, e);
	}
    }

    /**
     * Registre’s former database.
     */
    @Resource(name = "oldDb")
    private DataSource formerDatabase;

    /**
     * Registre entity manager. Will be used to create all {@link Named}s.
     */
    @Resource(name = "registreEntityManager")
    private RegistreEntityManager registreEntityManager;

    /**
     * Service used to index records.
     */
    @Resource(name = "index")
    private Index index;

    /**
     * JPA entity manager. Will be used to create all {@link User}s.
     */
    @PersistenceContext(unitName = "Registre")
    private EntityManager jpaEntityManager;

    /**
     * Connection to Registre’s former database. Requires only read access.
     */
    private Connection oldDatabaseConnection;

    /**
     * Query to fetch records from the former database.
     */
    private PreparedStatement recordsQuery;

    /**
     * Query to fetch a movie’s actors from the former database.
     */
    private PreparedStatement actorsQuery;

    /**
     * Open a connection to the former database.
     *
     * @throws SQLException
     * 		in case of connection error.
     */
    @PostConstruct
    public void connect() throws SQLException {
	oldDatabaseConnection = formerDatabase.getConnection();
	recordsQuery = oldDatabaseConnection.prepareStatement("select *"
		+ " from tout"
		+ " left join films on tout.id = films.id"
		+ " left join bd on tout.id = bd.id"
		+ " left join livres on tout.id = livres.id"
		+ " order by tout.id"
		+ " limit ?, ?",
		ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	actorsQuery = oldDatabaseConnection.prepareStatement("select acteur"
		+ " from acteurs"
		+ " where id = ?",
		ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Close the connection to the former database.
     *
     * @throws SQLException
     * 		in case of SQL error.
     */
    @PreDestroy
    public void disconnect() throws SQLException {
	recordsQuery.close();
	actorsQuery.close();
	oldDatabaseConnection.close();
    }

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public int migrateRecords(final int first, final int number)
	    throws SQLException {
	int migrated = 0;
	recordsQuery.setInt(1, first);
	recordsQuery.setInt(2, number);
	try (ResultSet result = recordsQuery.executeQuery()) {
            while (result.next()) {
                final String type = result.getString("type");
                final Record record;
                if (type.equals("livre")) {
                    record = createBook(result);
                } else if (type.equals("BD")) {
                    record = createComic(result);
                } else { // Movie. Type Blu-ray, DVD or "cassette".
                    record = createMovie(result);
                }
                fillInCommonFields(record, result);
                final Record savedRecord = jpaEntityManager.merge(record);
                index.reindex(savedRecord);
                logger.debug("{} migrated.", savedRecord);
                migrated++;
            }
        }
	return migrated;
    }

    /**
     * Create a book from a result of the {@link #recordsQuery} SQL query.
     *
     * @param result
     * 		result from {@link #recordsQuery}.
     * @return a new record (without any id).
     * @throws SQLException
     * 		in case of SQL error.
     */
    private Record createBook(final ResultSet result) throws SQLException {
	final User creator =
		provideUser(result.getString("createur"));
	final String title = result.getString("titre");
	final String author = result.getString("auteur");
	final String styles = result.getString("livres.genres");
	final Book book = new Book(title, creator);
	if (author != null && !author.isEmpty()) {
	    book.setAuthor(registreEntityManager.supplyAuthor(author));
	}
	if (styles != null && !styles.isEmpty()) {
	    book.setFantasyStyle(Boolean.valueOf(styles
		    .contains("fantastique")));
	    book.setTrueStoryStyle(Boolean.valueOf(styles
		    .contains("histoire vraie")));
	    book.setHistoricalStyle(Boolean.valueOf(styles
		    .concat("historique")));
	    book.setHumorStyle(Boolean.valueOf(styles
		    .contains("humour")));
	    book.setDetectiveStyle(Boolean.valueOf(styles
		    .contains("policier")));
	    book.setRomanticStyle(Boolean.valueOf(styles
		    .contains("romantique")));
	    book.setSfStyle(Boolean.valueOf(styles
		    .contains("science-fiction")));
	}
	return book;
    }

    /**
     * Create a comic from a result of the {@link #recordsQuery} SQL query.
     *
     * @param result
     * 		result from {@link #recordsQuery}.
     * @return a new record (without any id).
     * @throws SQLException
     * 		in case of SQL error.
     */
    private Record createComic(final ResultSet result) throws SQLException {
	final User creator =
		provideUser(result.getString("createur"));
	final String title = result.getString("titre");
	final String cartoonist = result.getString("dessinateur");
	final String scriptWriter = result.getString("scenariste");
	final Long number = (Long) result.getObject("numero");
	final Comic comic = new Comic(title, creator);
	if (cartoonist != null && !cartoonist.isEmpty()) {
	    comic.setCartoonist(
	    	registreEntityManager.supplyCartoonist(cartoonist)
	    );
	}
	if (scriptWriter != null && !scriptWriter.isEmpty()) {
	    comic.setScriptWriter(
	    	registreEntityManager.supplyScriptWriter(scriptWriter)
	    );
	}
	if (number != null) {
	    comic.setNumber(Integer.valueOf(number.intValue()));
	}
	return comic;
    }

    /**
     * Create a movie from a result of the {@link #recordsQuery} SQL query.
     * @param result
     * 		result from {@link #recordsQuery}.
     * @return a new record (without any id).
     * @throws SQLException
     * 		in case of SQL error.
     */
    private Record createMovie(final ResultSet result) throws SQLException {
	final User creator =
		provideUser(result.getString("createur"));
	final String title = result.getString("titre");
	final String director = result.getString("realisateur");
	final String composer = result.getString("compositeur");
        final String styles = result.getString ("films.genres");
	final int id = result.getInt("films.id");
	final Support support;
	final String supportName = result.getString("type");
	if (supportName.equals("DVD")) {
	    support = DVD;
	} else if (supportName.equals("cassette")) {
	    support = K7;
	} else if (supportName.equals("disque Blu-ray")) {
	    support = BRD;
	} else {
	    logger.error("Unknown support: “{}”.", supportName);
	    throw new RuntimeException("Unknown support " + supportName);
	}
	final Movie movie = new Movie(title, creator, support);
	if (director != null && !director.isEmpty()) {
	    movie.setDirector(
	    	registreEntityManager.supplyDirector(director)
	    );
	}
	if (composer != null && !composer.isEmpty()) {
	    movie.setComposer(
	    	registreEntityManager.supplyComposer(composer)
	    );
	}
	if (styles != null && !styles.isEmpty ()) {
	  movie.setActionStyle (styles.contains ("action"));
	  movie.setDocumentaryStyle (styles.contains ("documentaire"));
	  movie.setFantasyStyle (styles.contains ("fantastique"));
	  movie.setWarStyle (styles.contains ("film de guerre"));
	  movie.setTrueStoryStyle (styles.contains ("histoire vraie"));
	  movie.setHistoricalStyle (styles.contains ("historique"));
	  movie.setHumorStyle (styles.contains ("humour"));
	  movie.setDetectiveStyle (styles.contains ("policier"));
	  movie.setRomanticStyle (styles.contains ("romantique"));
	  movie.setSfStyle (styles.contains ("science-fiction"));
        }
	actorsQuery.setInt(1, id);
	try (ResultSet acteurs = actorsQuery.executeQuery()) {
            while (acteurs.next()) {
                final String nom = acteurs.getString("acteur");
                movie.getActors().add(registreEntityManager.supplyActor(nom));
            }
        }
	return movie;
    }

    /**
     * Fill in fields that are common to all kind of records.
     * @param record
     * 		record whose fields to fill in.
     * @param result
     * 		result from {@link #recordsQuery}.
     * @throws SQLException
     * 		in case of SQL error.
     */
    private void fillInCommonFields(final Record record, final ResultSet result)
	    throws SQLException {
	final int id = result.getInt("tout.id");
	final String series = result.getString("serie");
	final String owner = result.getString("proprietaire");
	final String location = result.getString("emplacement");
	final String picture = result.getString ("image");
	final String comment = result.getString("commentaire");
	final Date creation = result.getTimestamp("creation");
	final String lastModifier =
		result.getString("dernier_editeur");
	final Date lastModification =
		result.getTimestamp("derniere_edition");
	if (series != null && !series.isEmpty()) {
	    record.setSeries(registreEntityManager.supplySeries(series));
	}
	if (owner != null && !owner.isEmpty()) {
	    record.setOwner(registreEntityManager
		    .supplyOwner(owner));
	}
	if (location != null && !location.isEmpty()) {
	    record.setLocation(
	    	registreEntityManager.supplyLocation(location)
	    );
	}
	if (picture != null) {
	  record.setPicture (id + ".jpg");
        }
	record.setComment(comment);
	if (lastModifier != null && !lastModifier.isEmpty()) {
	    record.toucher(provideUser(lastModifier));
	}
	defineField(record, "id", new Long(id));
	defineField(record, "creation", creation);
	if (lastModification == null) {
	    defineField(record, "lastModification", creation);
	} else {
	    defineField(record, "lastModification", lastModification);
	}
    }

    /**
     * Fetch the user with the given name from the database. Will create it and
     * save it to database if it doesn’t exist.
     * @param name
     * 		name of the user to create/fetch.
     * @return the user called {@code name}.
     */
    private User provideUser(final String name) {
	final String newName = (name == null ? "Système" : name);
	final CriteriaBuilder builder = jpaEntityManager.getCriteriaBuilder();
	final CriteriaQuery<User> query =
		builder.createQuery(User.class);
	final Root<User> root = query.from(User.class);
	query.where(root.get("name").in(newName));
	User result;
	try {
	    result = jpaEntityManager.createQuery(query).getSingleResult();
	} catch (final NoResultException e) {
	    result = jpaEntityManager.merge(new User(newName, newName));
	}
	return result;
    }

}
