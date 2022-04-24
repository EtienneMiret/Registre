package fr.elimerl.registre.controllers;

import fr.elimerl.registre.commands.RecordCommand;
import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.User;
import fr.elimerl.registre.security.RAuthenticationToken;
import fr.elimerl.registre.services.Index;
import fr.elimerl.registre.services.PictureRegistry;
import fr.elimerl.registre.services.RegistreEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller responsible for record management: create, edit, display.
 */
@Controller
@RequestMapping("/Fiche")
public class RecordManager {

  private static final Logger logger =
      LoggerFactory.getLogger (RecordManager.class);

  /**
   * JPA entity manager, provided by Spring.
   */
  @PersistenceContext(name = "Registre")
  private EntityManager em;

  @Autowired
  private RegistreEntityManager rem;

  @Autowired
  private Index index;

  @Autowired
  private PictureRegistry pictureRegistry;

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
      return notFound (response);
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
   * Get the record editor, populated with a record for edition.
   *
   * @param id
   *          id of the record to populate the editor with.
   * @param response
   *          the HTTP response to send to the UA.
   * @return the record editor, populated with the requested record.
   */
  @GetMapping ("/{id}/Edition")
  @Transactional (readOnly = true)
  public ModelAndView getEditor (
      @PathVariable Long id,
      HttpServletResponse response
  ) {
    Record record = em.find (Record.class, id);
    if (record == null) {
      return notFound (response);
    }
    RecordCommand command = new RecordCommand (record);
    Map<String, Object> model = singletonMap ("command", command);
    return new ModelAndView ("records/editor", model);
  }

  /**
   * Get the record editor, empty.
   *
   * @return the name of the view to display.
   */
  @GetMapping
  public ModelAndView getEditor () {
    Map<String, Object> model = singletonMap ("command", new RecordCommand ());
    return new ModelAndView ("records/editor", model);
  }

  /**
   * Update a record.
   *
   * @param id
   *          id of the record to update.
   * @param command
   *          specification of the update to make. Must be of the same type than
   *          the specified record.
   * @param token
   *          authenticated principal creating this record.
   * @param response
   *          the HTTP response to send to the UA.
   * @return a record display view, populated with the updated record.
   */
  @PostMapping ("/{id}")
  @Transactional
  public ModelAndView update (
      @PathVariable Long id,
      @Valid RecordCommand command,
      @AuthenticationPrincipal RAuthenticationToken token,
      HttpServletResponse response
  ) {
    User user = token.getPrincipal ();
    String view;
    Record record = em.find (Record.class, id);
    if (record == null) {
      return notFound (response);
    } else if (record instanceof Movie) {
      if (command.getType () != RecordCommand.Type.movie) {
        response.setStatus (SC_BAD_REQUEST);
        return new ModelAndView ("error/bad-request");
      }
      view = "records/movie";
      updateMovie (command, (Movie) record);
    } else if (record instanceof Comic) {
      if (command.getType () != RecordCommand.Type.comic) {
        response.setStatus (SC_BAD_REQUEST);
        return new ModelAndView ("error/bad-request");
      }
      view = "records/comic";
      updateComic (command, (Comic) record);
    } else if (record instanceof Book) {
      if (command.getType () != RecordCommand.Type.book) {
        response.setStatus (SC_BAD_REQUEST);
        return new ModelAndView ("error/bad-request");
      }
      view = "records/book";
      updateBook (command, (Book) record);
    } else {
      view = "records/unknownType";
      response.setStatus (SC_NOT_IMPLEMENTED);
    }
    updateCommonFields (command, record);
    record.toucher (user);
    logger.info ("{} updated {}.", user, record);
    record = em.merge (record);
    index.reindex (record);
    return new ModelAndView (view, singletonMap ("record", record));
  }

  /**
   * Create a new record and save it to the database.
   *
   * @param command
   *          specification of the record to create.
   * @param token
   *          authenticated principal creating this record.
   * @return the record editor, populated with the supplied {@code command} and
   *          the created {@link Record}.
   */
  @PostMapping
  @Transactional
  public ModelAndView create (
      @Valid RecordCommand command,
      @AuthenticationPrincipal RAuthenticationToken token
  ) {
    User user = token.getPrincipal ();
    logger.info ("{} creates a new {}.", user, command.getType ());
    Record record;
    switch (command.getType ()) {
      case movie:
        record = createMovie (command, user);
        break;
      case comic:
        record = createComic (command, user);
        break;
      case book:
        record = createBook (command, user);
        break;
      default:
        throw new RuntimeException ("Unknown type: " + command.getType ());
    }
    updateCommonFields (command, record);
    record = em.merge (record);
    index.reindex (record);
    command.prepareForReuse ();
    Map<String, Object> model = new HashMap<> ();
    model.put ("record", record);
    model.put ("command", command);
    return new ModelAndView ("records/editor", model);
  }

  private void updateCommonFields (RecordCommand command, Record record) {
    record.setTitle (command.getTitle ());
    if (isNotBlank (command.getSeries ())) {
      record.setSeries (rem.supplySeries (command.getSeries ()));
    } else {
      record.setSeries (null);
    }
    if (isNotBlank (command.getOwner ())) {
      record.setOwner (rem.supplyOwner (command.getOwner ()));
    } else {
      record.setOwner (null);
    }
    if (isNotBlank (command.getLocation ())) {
      record.setLocation (rem.supplyLocation (command.getLocation ()));
    } else {
      record.setLocation (null);
    }
    if (!command.getPicture ().isEmpty ()
        && command.getPicture ().getContentType () != null) {
      pictureRegistry.savePicture (
          command.getPicture ().getContentType (),
          command.getPicture ()::getInputStream
      ).ifPresent (record::setPicture);
    }
    record.setComment (command.getComment ());
  }

  /**
   * Create a {@link Movie} record from the provided {@link RecordCommand}. Only
   * mandatory and movie specific fields will be populated.
   *
   * @param command
   *          specification of the movie to create.
   * @param user
   *          user who originated the creation.
   * @return the newly created movie, not yet saved to the database.
   */
  private Movie createMovie (RecordCommand command, User user) {
    Movie movie = new Movie (command.getTitle (), user, command.getSupport ());
    updateMovie (command, movie);
    return movie;
  }

  private void updateMovie (RecordCommand command, Movie movie) {
    movie.setSupport (command.getSupport ());
    if (isNotBlank (command.getDirector ())) {
      movie.setDirector (rem.supplyDirector (command.getDirector ()));
    } else {
      movie.setDirector (null);
    }
    movie.getActors ().clear ();
    if (command.getActors () != null) {
      command.getActors ()
          .stream ()
          .filter (this::isNotBlank)
          .map (rem::supplyActor)
          .forEach (movie.getActors ()::add);
    }
    if (isNotBlank (command.getComposer ())) {
      movie.setComposer (rem.supplyComposer (command.getComposer ()));
    } else {
      movie.setComposer (null);
    }
  }

  /**
   * Create a {@link Comic} record from the provided {@link RecordCommand}. Only
   * mandatory and comic specific fields will be populated.
   *
   * @param command
   *          specification of the comic to create.
   * @param user
   *          user who originated the creation.
   * @return the newly created comic, not yet saved to the database.
   */
  private Comic createComic (RecordCommand command, User user) {
    Comic comic = new Comic (command.getTitle (), user);
    updateComic (command, comic);
    return comic;
  }

  private void updateComic (RecordCommand command, Comic comic) {
    if (isNotBlank (command.getCartoonist ())) {
      comic.setCartoonist (rem.supplyCartoonist (command.getCartoonist ()));
    } else {
      comic.setCartoonist (null);
    }
    if (isNotBlank (command.getScriptWriter ())) {
      comic.setScriptWriter (rem.supplyScriptWriter (command.getScriptWriter ()));
    } else {
      comic.setScriptWriter (null);
    }
  }

  /**
   * Create a {@link Book} record from the provided {@link RecordCommand}. Only
   * mandatory and book specific fields will be populated.
   *
   * @param command
   *          specification of the book to create.
   * @param user
   *          user who originated the creation.
   * @return the newly created book, not yet saved to the database.
   */
  private Book createBook (RecordCommand command, User user) {
    Book book = new Book (command.getTitle (), user);
    updateBook (command, book);
    return book;
  }

  private void updateBook (RecordCommand command, Book book) {
    if (isNotBlank (command.getAuthor ())) {
      book.setAuthor (rem.supplyAuthor (command.getAuthor ()));
    } else {
      book.setAuthor (null);
    }
  }

  private boolean isNotBlank (String string) {
    return string != null && !string.trim ().isEmpty ();
  }

  private ModelAndView notFound (HttpServletResponse response) {
    response.setStatus (SC_NOT_FOUND);
    return new ModelAndView ("records/notFound");
  }

}
