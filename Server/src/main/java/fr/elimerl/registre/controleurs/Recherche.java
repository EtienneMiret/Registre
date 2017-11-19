package fr.elimerl.registre.controleurs;

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

import fr.elimerl.registre.services.ParseurDeRecherches;

/**
 * Contrôleur chargé de traiter les recherches faites par un utilisateur.
 */
@Controller
public class Recherche {

    /**
     * Gestionnaire d’entité JPA, injecté par Spring.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    /**
     * Service parseur de recherches, injecté par Spring.
     */
    @Autowired
    private ParseurDeRecherches parseur;

    private CriteriaBuilder constructeur;

    @PostConstruct
    public void setUp() {
	constructeur = em.getCriteriaBuilder();
    }

    @RequestMapping("/Rechercher/{texte}")
    @Transactional(readOnly = true)
    public String rechercher(@PathVariable final String texte,
	    final Model modèle) {
	final SearchQuery requêteRegistre = parseur.analyser(texte);
	final CriteriaQuery<Record> requête =
		constructeur.createQuery(Record.class);
	final Root<Record> fiche = requête.from(Record.class);
	final Predicate prédicat =
		requêteRegistre.createPredicate(constructeur, requête, fiche);
	requête.select(fiche);
	requête.where(prédicat);
	final TypedQuery<Record> requêteJpa = em.createQuery(requête);
	modèle.addAttribute("fiches", requêteJpa.getResultList());
	return "recherche";
    }

}
