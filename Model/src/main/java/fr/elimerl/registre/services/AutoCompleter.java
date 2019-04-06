package fr.elimerl.registre.services;

import fr.elimerl.registre.entities.Named;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
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
        .collect (toList ());
  }

  /**
   * Get and set {@link #builder} from {@link #em}.
   */
  @PostConstruct
  public void setBuilder () {
    builder = em.getCriteriaBuilder ();
  }

}
