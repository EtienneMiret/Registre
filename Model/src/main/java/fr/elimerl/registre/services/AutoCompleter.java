package fr.elimerl.registre.services;

import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import fr.elimerl.registre.entities.Actor;
import fr.elimerl.registre.entities.Author;
import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Cartoonist;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Composer;
import fr.elimerl.registre.entities.Director;
import fr.elimerl.registre.entities.Location;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Named;
import fr.elimerl.registre.entities.Owner;
import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.ScriptWriter;
import fr.elimerl.registre.entities.Series;

/**
 * Service that provides auto-completion results.
 */
public class AutoCompleter {

  /**
   * JPA entity manager.
   */
  @PersistenceContext (unitName = "Registre")
  private EntityManager em;

  /**
   * Criteria builder linked to {@link #em}.
   */
  private CriteriaBuilder builder;

  /**
   * List all referenced actors.
   *
   * @return all referenced actors.
   * @see #list(Class, Class, String)
   */
  public List<Actor> listActors() {
    return list(Actor.class, Movie.class, "actors");
  }

  /**
   * List all referenced authors.
   *
   * @return all referenced authors.
   * @see #list(Class, Class, String)
   */
  public List<Author> listAuthors() {
    return list(Author.class, Book.class, "author");
  }

  /**
   * List all referenced cartoonists.
   *
   * @return all referenced cartoonists.
   * @see #list(Class, Class, String)
   */
  public List<Cartoonist> listCartoonists() {
    return list(Cartoonist.class, Comic.class, "cartoonist");
  }

  /**
   * List all referenced composers.
   *
   * @return all referenced composers.
   * @see #list(Class, Class, String)
   */
  public List<Composer> listComposers() {
    return list(Composer.class, Movie.class, "composer");
  }

  /**
   * List all referenced directors.
   *
   * @return all referenced directors.
   * @see #list(Class, Class, String)
   */
  public List<Director> listDirectors() {
    return list(Director.class, Movie.class, "director");
  }

  /**
   * List all referenced locations.
   *
   * @return all referenced locations.
   * @see #list(Class, Class, String)
   */
  public List<Location> listLocations() {
    return list(Location.class, Record.class, "location");
  }

  /**
   * List all referenced owners.
   *
   * @return all referenced owners.
   * @see #list(Class, Class, String)
   */
  public List<Owner> listOwners() {
    return list(Owner.class, Record.class, "owner");
  }

  /**
   * List all referenced scriptwriters.
   *
   * @return all referenced scriptwriters.
   * @see #list(Class, Class, String)
   */
  public List<ScriptWriter> listScriptWriters() {
    return list(ScriptWriter.class, Comic.class, "scriptWriter");
  }

  /**
   * List all referenced series.
   *
   * @return all referenced series.
   * @see #list(Class, Class, String)
   */
  public List<Series> listSeries() {
    return list(Series.class, Record.class, "series");
  }

  /**
   * List all {@link Named} of a given type. Items with no reference are omitted.
   *
   * @param resultClass
   *     class of {@link Named} to list.
   * @param recordClass
   *     class of {@link Record} where the result are referenced.
   * @param attribute
   *     attribute of {@code recordClass} used for the reference.
   * @param <T>
   *     type of {@link Named} to list.
   * @param <R>
   *     type of {@link Record} where the result are referenced.
   * @return the requested list, ordered by decreasing reference
   * number, then by ascending name.
   */
  private <T extends Named, R extends Record> List<T> list(
      Class<T> resultClass,
      Class<R> recordClass,
      String attribute
  ) {
    CriteriaQuery<T> query = builder.createQuery(null);
    Root<R> record = query.from(recordClass);
    Root<T> result = query.from(resultClass);
    Expression<Long> count = builder.count(result);
    query.multiselect(result);
    Predicate equal = builder.equal(result, record.get(attribute));
    Predicate alive = builder.equal(record.get("alive"), true);
    query.where(builder.and(equal, alive));
    query.groupBy(result);
    query.orderBy(builder.desc(count), builder.asc(result.get("name")));
    return em.createQuery(query)
        .getResultList();
  }

  /**
   * Get and set {@link #builder} from {@link #em}.
   */
  @PostConstruct
  public void setBuilder () {
    builder = em.getCriteriaBuilder ();
  }

}
