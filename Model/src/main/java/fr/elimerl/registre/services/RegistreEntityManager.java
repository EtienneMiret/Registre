package fr.elimerl.registre.services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.*;
import fr.elimerl.registre.entities.Actor;

/**
 * This class wraps a JPA {@link EntityManager} in order to provide Registre
 * specific features.
 */
public class RegistreEntityManager {

    /**
     * The actual entity manager wrapped by this class. Provided by the
     * container.
     */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /**
     * Lookup a database object by a key.
     *
     * @param <T>
     *          type of the wanted object.
     * @param type
     *          class of the wanted object.
     * @param key
     *          name of the key attribute.
     * @param value
     *          value the key must have.
     * @return the wanted object if it exists, {@code null} otherwise.
     * @throws NonUniqueResultException
     *          if there are several matching objects.
     */
    private <T> T lookup(final Class<T> type, final String key,
	    final Object value) {
	final CriteriaBuilder builder = em.getCriteriaBuilder();
	final CriteriaQuery<T> query = builder.createQuery(type);
	final Root<T> racine = query.from(type);
	query.where(builder.equal(racine.get(key), value));
	T result;
	try {
	    result = em.createQuery(query).getSingleResult();
	} catch (final NoResultException e) {
	    result = null;
	}
	return result;
    }

    /**
     * Supply a word with the given value. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param value
     *          the word’s {@link Word#value value}.
     * @return the requested word.
     */
    public Word supplyWord(final String value) {
	final Word word = lookup(Word.class, "value", value);
	return (word == null ? em.merge(new Word(value)) : word);
    }

    /**
     * Lookup a named in the database.
     *
     * @param type
     *          class of the requested named.
     * @param name
     *          name of the requested named.
     * @param <T>
     *          type of the requested named.
     * @return the requested named if it exists, {@code null} otherwise.
     */
    private <T extends Named> T lookupNamed(final Class<T> type,
	    final String name) {
	return lookup(type, "name", name);
    }

    /**
     * Supply an actor with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested actor.
     * @return the actor with the specified name.
     */
    public Actor supplyActor(final String name) {
	final Actor actor = lookupNamed(Actor.class, name);
	return (actor == null ? em.merge(new Actor(name)) : actor);
    }

    /**
     * Supply an author with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested author.
     * @return the author with the specified name.
     */
    public Author supplyAuthor(final String name) {
	final Author author = lookupNamed(Author.class, name);
	return (author == null ? em.merge(new Author(name)) : author);
    }

    /**
     * Supply a composer with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested composer.
     * @return the composer with the specified name.
     */
    public Composer supplyComposer(final String name) {
	final Composer composer = lookupNamed(Composer.class, name);
	return (composer == null ? em.merge(new Composer(name)) : composer);
    }

    /**
     * Supply a cartoonist with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested cartooonist.
     * @return the cartoonist with the specified name.
     */
    public Cartoonist supplyCartoonist(final String name) {
	final Cartoonist lookedUp = lookupNamed(Cartoonist.class, name);
	return (lookedUp == null ? em.merge(new Cartoonist(name)) : lookedUp);
    }

    /**
     * Supply a location with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested location.
     * @return the location with the specified name.
     */
    public Location supplyLocation(final String name) {
	final Location location = lookupNamed(Location.class, name);
	return (location == null ? em.merge(new Location(name)) : location);
    }

    /**
     * Supply an owner with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested owner.
     * @return the owner with the specified name.
     */
    public Owner supplyOwner(final String name) {
	final Owner owner = lookupNamed(Owner.class, name);
	return (owner == null ? em.merge(new Owner(name)) : owner);
    }

    /**
     * Supply an author with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested author.
     * @return the author with the specified name.
     */
    public Director supplyDirector(final String name) {
	final Director director = lookupNamed(Director.class, name);
	return (director == null ? em.merge(new Director(name)) : director);
    }

    /**
     * Supply an script writer with the given name. Fetch it from the database
     * if it already exists, create it otherwise.
     *
     * @param name
     *          name of the requested script writer.
     * @return the script writer with the specified name.
     */
    public ScriptWriter supplyScriptWriter(final String name) {
	final ScriptWriter writer = lookupNamed(ScriptWriter.class, name);
	return (writer == null ? em.merge(new ScriptWriter(name)) : writer);
    }

    /**
     * Supply a series with the given name. Fetch it from the database if it
     * already exists, create it otherwise.
     *
     * @param name
     *          name of the requested series.
     * @return the series with the specified name.
     */
    public Series supplySeries(final String name) {
	final Series series = lookupNamed(Series.class, name);
	return (series == null ? em.merge(new Series(name)) : series);
    }

    /**
     * Returns the JPA entity manager this service wraps.
     *
     * @return the JPA entity manager this service wraps.
     */
    public EntityManager getJpaEntityManager() {
	return em;
    }

}
