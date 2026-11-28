package fr.elimerl.registre.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.grammar.SearchQuery;
import fr.elimerl.registre.services.AutoCompleter;
import fr.elimerl.registre.services.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

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

  @Autowired
  private AutoCompleter autoCompleter;

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
  public ModelAndView search(
      @RequestParam(required = false) final String type,
      @RequestParam final String q
  ) {
    final SearchQuery searchQuery;
    if (type == null || type.isBlank()) {
      searchQuery = parser.parse(q);
    } else {
      searchQuery = parser.parse("type:%s (%s)".formatted(type, q));
    }
    final CriteriaQuery<Record> query =
        builder.createQuery (Record.class);
    final Root<Record> record = query.from (Record.class);
    final Predicate predicate =
        searchQuery.createPredicate (builder, query, record);
    query.select (record);
    query.where (builder.and (
        predicate,
        builder.equal (record.get ("alive"), true)
    ));
    query.orderBy (
        builder.asc (record.get ("series")),
        builder.asc (record.get ("number")),
        builder.asc (record.get ("title")),
        builder.asc (record.get ("id"))
    );
    final TypedQuery<Record> jpaQuery = em.createQuery (query);
    final Map<String, Object> model = new HashMap<> ();
    model.put ("records", jpaQuery.getResultList ());
    model.put("query", q);
    model.put("type", type);
    return new ModelAndView ("search", model);
  }

  @RequestMapping("/RechercheAvancée")
  public ModelAndView displayAdvancedSearch() {
    return new ModelAndView("advanced-search", autoCompleter.listAll());
  }

  @RequestMapping("/advanced-search")
  public String advancedSearch(
      final RedirectAttributes redirectAttributes,
      @RequestParam(value = "type", required = false) String type,
      @RequestParam(value = "title", required = false) List<String> titles,
      @RequestParam(value = "actor", required = false) List<String> actors,
      @RequestParam(value = "author", required = false) List<String> authors,
      @RequestParam(value = "cartoonist", required = false) List<String> cartoonists,
      @RequestParam(value = "composer", required = false) List<String> composers,
      @RequestParam(value = "director", required = false) List<String> directors,
      @RequestParam(value = "location", required = false) List<String> locations,
      @RequestParam(value = "owner", required = false) List<String> owners,
      @RequestParam(value = "script-writer", required = false) List<String> scriptWriters,
      @RequestParam(value = "series", required = false) List<String> series
  ) {
    var builder = new StringBuilder();
    appendField(builder, "title", titles);
    appendField(builder, "actor", actors);
    appendField(builder, "author", authors);
    appendField(builder, "cartoonist", cartoonists);
    appendField(builder, "composer", composers);
    appendField(builder, "director", directors);
    appendField(builder, "location", locations);
    appendField(builder, "owner", owners);
    appendField(builder, "scriptWriter", scriptWriters);
    appendField(builder, "series", series);
    String q = builder.toString().trim();
    redirectAttributes.addAttribute("type", type);
    redirectAttributes.addAttribute("q", q);
    return "redirect:/Rechercher";
  }

  /**
   * Appends to {@code builder} a disjunction of {@code field:(value)}
   * clauses, one per value, followed by a space. Does nothing if
   * {@code values} is empty.
   */
  private static void appendField(
      final StringBuilder builder,
      final String field,
      final List<String> values
  ) {
    if (values == null || values.isEmpty()) {
      return;
    }
    var fieldQuery = values.stream()
        .filter(not(String::isBlank))
        .map(value -> "%s:(%s)".formatted(field, value))
        .collect(joining(" or "));
    if (fieldQuery.isBlank())
    {
      return;
    }
    builder.append("(");
    builder.append(fieldQuery);
    builder.append(") ");
  }

}
