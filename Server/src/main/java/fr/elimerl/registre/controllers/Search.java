package fr.elimerl.registre.controllers;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.grammar.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.elimerl.registre.services.QueryParser;

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
    public void setUp() {
	builder = em.getCriteriaBuilder();
    }

    @RequestMapping("/Rechercher/{texte}")
    @Transactional(readOnly = true)
    public String search(@PathVariable final String texte,
	    final Model model) {
	final SearchQuery searchQuery = parser.parse(texte);
	final CriteriaQuery<Record> query =
		builder.createQuery(Record.class);
	final Root<Record> record = query.from(Record.class);
	final Predicate predicate =
		searchQuery.createPredicate(builder, query, record);
	query.select(record);
	query.where(predicate);
	final TypedQuery<Record> jpaQuery = em.createQuery(query);
	model.addAttribute("records", jpaQuery.getResultList());
	return "search";
    }

}
