package fr.elimerl.registre.services;

import fr.elimerl.registre.entities.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * Service that provides auto-completion results.
 */
public class AutoCompleter {

  /**
   * Regex matching any JPA "like" pattern operators.
   */
  private static final Pattern JPA_OPERATORS = Pattern.compile ("[%_^]");

  /**
   * Maximum number of suggestion to make.
   */
  private static final long MAX_SUGGESTIONS = 10;

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
   * Find all {@link Named} of type {@code clazz} that contains {@code query} at
   * the beginning of a word. Case-insensitive.
   * <p>
   * Eg, “mer” will match “Merlin“ and “Vlad Mergal”, but not “Warhammer”.
   *
   * @param query
   *     text to find at the beginning of a word in a named.
   * @param clazz
   *     subtype of {@link Named} to search.
   * @return the list of matching results, in alphabetical order.
   */
  public List<String> autoComplete (
      String query,
      Class<? extends Named> clazz
  ) {
    String pattern = "%" + JPA_OPERATORS.matcher (query.toLowerCase ())
        .replaceAll ("^$0") + "%";
    CriteriaQuery<String> criteriaQuery = builder.createQuery (String.class);
    Path<String> name = criteriaQuery.from (clazz).get ("name");
    criteriaQuery.select (name);
    criteriaQuery.where (builder.like (builder.lower (name), pattern));
    criteriaQuery.orderBy (builder.asc (name));
    Pattern regex = Pattern.compile (
        "\\b" + Pattern.quote (query),
        Pattern.CASE_INSENSITIVE
    );
    return em.createQuery (criteriaQuery)
        .getResultList ()
        .stream ()
        .filter (res -> regex.matcher (res).find ())
        .limit (MAX_SUGGESTIONS)
        .collect (toList ());
  }

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
    query.where(builder.equal(result, record.get(attribute)));
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
