package fr.elimerl.registre.controllers;

import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Record;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
   * @param response
   *          the HTTP response to send to the UA.
   * @return the name of the view to display.
   */
  @RequestMapping(value = "/{id}", method = GET)
  @Transactional(readOnly = true)
  public ModelAndView display (@PathVariable final Long id,
      final HttpServletResponse response) {
    final Record record = em.find (Record.class, id);
    final Map<String, Object> model = new HashMap<> ();
    model.put ("record", record);
    final String view;
    if (record == null) {
      view = "records/notFound";
      response.setStatus (SC_NOT_FOUND);
    } else if (record instanceof Movie) {
      view = "records/movie";
    } else if (record instanceof Book) {
      view = "records/book";
    } else if (record instanceof Comic) {
      view = "records/comic";
    } else {
      view = "records/unknownType";
      response.setStatus (SC_NOT_IMPLEMENTED);
    }
    return new ModelAndView (view, model);
  }

  /**
   * Get the record editor, empty.
   *
   * @return the name of the view to display.
   */
  @RequestMapping("/Nouvelle")
  public ModelAndView getEditor () {
    return new ModelAndView ("records/editor");
  }

}
