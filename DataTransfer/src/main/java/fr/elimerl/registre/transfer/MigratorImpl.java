package fr.elimerl.registre.transfer;

import static fr.elimerl.registre.entities.Film.Support.BRD;
import static fr.elimerl.registre.entities.Film.Support.DVD;
import static fr.elimerl.registre.entities.Film.Support.K7;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.elimerl.registre.entities.BandeDessinée;
import fr.elimerl.registre.entities.Fiche;
import fr.elimerl.registre.entities.Film;
import fr.elimerl.registre.entities.Film.Support;
import fr.elimerl.registre.entities.Livre;
import fr.elimerl.registre.entities.Nommé;
import fr.elimerl.registre.entities.Utilisateur;
import fr.elimerl.registre.services.GestionnaireEntités;
import fr.elimerl.registre.services.Indexeur;

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
     * {@link Fiche} class (and not a sub-class).
     *
     * @param record
     * 		record whose field is to be modified.
     * @param name
     * 		name of the field to set.
     * @param value
     * 		value to set in the specified field.
     */
    private static void defineField(final Fiche record, final String name,
	    final Object value) {
	try {
	    final Field champ = Fiche.class.getDeclaredField(name);
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
    @Resource(name = "ancienneBase")
    private DataSource formerDatabase;

    /**
     * Registre entity manager. Will be used to create all {@link Nommé}s.
     */
    @Resource(name = "gestionnaireEntités")
    private GestionnaireEntités registreEntityManager;

    /**
     * Service used to index records.
     */
    @Resource(name = "indexeur")
    private Indexeur index;

    /**
     * JPA entity manager. Will be used to create all {@link Utilisateur}s.
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
	final ResultSet result = recordsQuery.executeQuery();
	while (result.next()) {
	    final String type = result.getString("type");
	    final Fiche record;
	    if (type.equals("livre")) {
		record = createBook(result);
	    } else if (type.equals("BD")) {
		record = createComic(result);
	    } else { // Movie. Type Blu-ray, DVD or "cassette".
		record = createMovie(result);
	    }
	    fillInCommonFields(record, result);
	    final Fiche savedRecord = jpaEntityManager.merge(record);
	    index.réindexer(savedRecord);
	    logger.debug("{} migrated.", savedRecord);
	    migrated++;
	}
	result.close();
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
    private Fiche createBook(final ResultSet result) throws SQLException {
	final Utilisateur creator =
		provideUser(result.getString("createur"));
	final String title = result.getString("titre");
	final String author = result.getString("auteur");
	final String styles = result.getString("genres");
	final Livre book = new Livre(title, creator);
	if (author != null && !author.isEmpty()) {
	    book.setAuteur(registreEntityManager.fournirAuteur(author));
	}
	if (styles != null && !styles.isEmpty()) {
	    book.setGenreFantastique(Boolean.valueOf(styles
		    .contains("fantastique")));
	    book.setGenreHistoireVraie(Boolean.valueOf(styles
		    .contains("histoire vraie")));
	    book.setGenreHistorique(Boolean.valueOf(styles
		    .concat("historique")));
	    book.setGenreHumour(Boolean.valueOf(styles
		    .contains("humour")));
	    book.setGenrePolicier(Boolean.valueOf(styles
		    .contains("policier")));
	    book.setGenreRomantique(Boolean.valueOf(styles
		    .contains("romantique")));
	    book.setGenreSf(Boolean.valueOf(styles
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
    private Fiche createComic(final ResultSet result) throws SQLException {
	final Utilisateur creator =
		provideUser(result.getString("createur"));
	final String title = result.getString("titre");
	final String cartoonist = result.getString("dessinateur");
	final String scriptWriter = result.getString("scenariste");
	final Long number = (Long) result.getObject("numero");
	final BandeDessinée comic = new BandeDessinée(title, creator);
	if (cartoonist != null && !cartoonist.isEmpty()) {
	    comic.setDessinateur(
	    	registreEntityManager.fournirDessinateur(cartoonist)
	    );
	}
	if (scriptWriter != null && !scriptWriter.isEmpty()) {
	    comic.setScénariste(
	    	registreEntityManager.fournirScénariste(scriptWriter)
	    );
	}
	if (number != null) {
	    comic.setNuméro(Integer.valueOf(number.intValue()));
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
    private Fiche createMovie(final ResultSet result) throws SQLException {
	final Utilisateur creator =
		provideUser(result.getString("createur"));
	final String title = result.getString("titre");
	final String director = result.getString("realisateur");
	final String composer = result.getString("compositeur");
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
	final Film movie = new Film(title, creator, support);
	if (director != null && !director.isEmpty()) {
	    movie.setRéalisateur(
	    	registreEntityManager.fournirRéalisateur(director)
	    );
	}
	if (composer != null && !composer.isEmpty()) {
	    movie.setCompositeur(
	    	registreEntityManager.fournirCompositeur(composer)
	    );
	}
	actorsQuery.setInt(1, id);
	final ResultSet acteurs = actorsQuery.executeQuery();
	while (acteurs.next()) {
	    final String nom = acteurs.getString("acteur");
	    movie.getActeurs().add(registreEntityManager.fournirActeur(nom));
	}
	acteurs.close();
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
    private void fillInCommonFields(final Fiche record, final ResultSet result)
	    throws SQLException {
	final int id = result.getInt("tout.id");
	final String series = result.getString("serie");
	final String owner = result.getString("proprietaire");
	final String location = result.getString("emplacement");
	final String comment = result.getString("commentaire");
	final Date creation = result.getTimestamp("creation");
	final String lastModifier =
		result.getString("dernier_editeur");
	final Date lastModification =
		result.getTimestamp("derniere_edition");
	if (series != null && !series.isEmpty()) {
	    record.setSérie(registreEntityManager.fournirSérie(series));
	}
	if (owner != null && !owner.isEmpty()) {
	    record.setPropriétaire(registreEntityManager
		    .fournirPropriétaire(owner));
	}
	if (location != null && !location.isEmpty()) {
	    record.setEmplacement(
	    	registreEntityManager.fournirEmplacement(location)
	    );
	}
	record.setCommentaire(comment);
	if (lastModifier != null && !lastModifier.isEmpty()) {
	    record.toucher(provideUser(lastModifier));
	}
	defineField(record, "id", new Long(id));
	defineField(record, "création", creation);
	if (lastModification == null) {
	    defineField(record, "dernièreÉdition", creation);
	} else {
	    defineField(record, "dernièreÉdition", lastModification);
	}
    }

    /**
     * Fetch the user with the given name from the database. Will create it and
     * save it to database if it doesn’t exist.
     * @param name
     * 		name of the user to create/fetch.
     * @return the user called {@code name}.
     */
    private Utilisateur provideUser(final String name) {
	final String newName = (name == null ? "Système" : name);
	final CriteriaBuilder builder = jpaEntityManager.getCriteriaBuilder();
	final CriteriaQuery<Utilisateur> query =
		builder.createQuery(Utilisateur.class);
	final Root<Utilisateur> root = query.from(Utilisateur.class);
	query.where(root.get("nom").in(newName));
	Utilisateur result;
	try {
	    result = jpaEntityManager.createQuery(query).getSingleResult();
	} catch (final NoResultException e) {
	    result = jpaEntityManager.merge(new Utilisateur(newName, newName));
	}
	return result;
    }

}