package fr.elimerl.registre.controllers;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.grammar.SearchQuery;
import fr.elimerl.registre.services.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller responsible for handling user made search queries.
 */
@Controller
public class Search {

  /**
   * JPA entity manager, provided by Spring.
   */
  @PersistenceContext(name = "Registre")
  private EntityManager em;

  /**
   * Query parser, provided by Spring.
   */
  @Autowired
  private QueryParser parser;

  private CriteriaBuilder builder;

  @PostConstruct
  public void setUp () {
    builder = em.getCriteriaBuilder ();
  }

  @RequestMapping("/Rechercher")
  @Transactional(readOnly = true)
  public ModelAndView search (@RequestParam final String q) {
    final SearchQuery searchQuery = parser.parse (q);
    final CriteriaQuery<Record> query =
        builder.createQuery (Record.class);
    final Root<Record> record = query.from (Record.class);
    final Predicate predicate =
        searchQuery.createPredicate (builder, query, record);
    query.select (record);
    query.where (predicate);
    query.orderBy (
        builder.asc (record.get ("series")),
        builder.asc (record.get ("number")),
        builder.asc (record.get ("title")),
        builder.asc (record.get ("id"))
    );
    final TypedQuery<Record> jpaQuery = em.createQuery (query);
    final Map<String, Object> model = new HashMap<> ();
    model.put ("records", jpaQuery.getResultList ());
    return new ModelAndView ("search", model);
  }

}
