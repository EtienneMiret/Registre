package fr.elimerl.registre.controllers;

import fr.elimerl.registre.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Controller for the home page.
 */
@Controller
public class Home {

  /**
   * JPA entity manager, provided by Spring.
   */
  @PersistenceContext (name = "Registre")
  private EntityManager em;

  /**
   * JPA criteria builder linked to {@link #em}.
   */
  private CriteriaBuilder builder;

  private Random random;

  @GetMapping ("/")
  @Transactional (readOnly = true)
  public ModelAndView home () {
    Map<String, Object> model = new HashMap<> ();
    model.put ("title", fetchReference (Reference.Field.TITLE));
    model.put ("comment", fetchReference (Reference.Field.COMMENT));
    model.put ("series", fetchRandomNamed (Series.class));
    model.put ("owner", fetchRandomNamed (Owner.class));
    model.put ("location", fetchRandomNamed (Location.class));
    model.put ("director", fetchRandomNamed (Director.class));
    model.put ("actor", fetchRandomNamed (Actor.class));
    model.put ("composer", fetchRandomNamed (Composer.class));
    model.put ("cartoonist", fetchRandomNamed (Cartoonist.class));
    model.put ("scriptWriter", fetchRandomNamed (ScriptWriter.class));
    model.put ("author", fetchRandomNamed (Author.class));
    return new ModelAndView ("home", model);
  }

  @PostConstruct
  public void setUp () {
    builder = em.getCriteriaBuilder ();
    random = ThreadLocalRandom.current ();
  }

  private String fetchReference (Reference.Field field) {
    Query countQuery = em.createQuery ("select count(distinct r.word.value) from Reference r where r.field = :field");
    countQuery.setParameter ("field", field);
    int count = ((Long) countQuery.getResultList ().get (0)).intValue ();
    if (count == 0) {
      return "la vie est belle";
    }
    int index = random.nextInt (count);
    Query fetchQuery = em.createQuery ("select distinct r.word.value from Reference r where r.field = :field");
    fetchQuery.setParameter ("field", field);
    fetchQuery.setMaxResults (1);
    fetchQuery.setFirstResult (index);
    List results = fetchQuery.getResultList ();
    return (String) results.get (0);
  }

  private String fetchRandomNamed (Class<? extends Named> clazz) {
    int count = countNamed (clazz);
    if (count == 0) {
      return "bonjour";
    }
    int index = random.nextInt (count);
    return fetchOneNamed (clazz, index);
  }

  private int countNamed (Class<? extends Named> clazz) {
    CriteriaQuery<Long> criteriaQuery = builder.createQuery (Long.class);
    Root<? extends Named> root = criteriaQuery.from (clazz);
    criteriaQuery.select (builder.count (root));
    TypedQuery<Long> query = em.createQuery (criteriaQuery);
    return query.getSingleResult ().intValue ();
  }

  private String fetchOneNamed (Class<? extends Named> clazz, int index) {
    CriteriaQuery<String> query = builder.createQuery (String.class);
    Root<? extends Named> root = query.from (clazz);
    query.select (root.get ("name"));
    TypedQuery<String> fetchQuery = em.createQuery (query);
    fetchQuery.setMaxResults (1);
    fetchQuery.setFirstResult (index);
    return fetchQuery.getSingleResult ();
  }

}
