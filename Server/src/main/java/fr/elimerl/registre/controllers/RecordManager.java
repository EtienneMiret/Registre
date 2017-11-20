package fr.elimerl.registre.controllers;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Record;

/**
 * Controller responsible for record management: create, edit, display.
 */
@Controller
@RequestMapping("/Fiche")
public class RecordManager {

    /**
     * JPA entity manager, provided by Spring.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    /**
     * Display a record in read-only mode.
     *
     * @param id
     *          id of the record to display.
     * @param model
     *          the Spring model.
     * @param response
     *          the HTTP response to send to the UA.
     * @return the name of the view to display.
     */
    @RequestMapping(value = "/{id}", method = GET)
    @Transactional(readOnly = true)
    public String display(@PathVariable final Long id, final Model model,
	    final HttpServletResponse response) {
	final Record record = em.find(Record.class, id);
	model.addAttribute("fiche", record);
	final String view;
	if (record == null) {
	    view = "ficheInexistante";
	    response.setStatus(SC_NOT_FOUND);
	} else if (record instanceof Movie) {
	    view = "film";
	} else if (record instanceof Book) {
	    view = "livre";
	} else if (record instanceof Comic) {
	    view = "bd";
	} else {
	    view = "typeFicheInconnu";
	    response.setStatus(SC_NOT_IMPLEMENTED);
	}
	return view;
    }

}
