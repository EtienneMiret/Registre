package fr.elimerl.registre.controllers;

import fr.elimerl.registre.commands.RecordCommand;
import fr.elimerl.registre.entities.Actor;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.User;
import fr.elimerl.registre.security.RAuthenticationToken;
import fr.elimerl.registre.services.Index;
import fr.elimerl.registre.services.PictureRegistry;
import fr.elimerl.registre.services.RegistreEntityManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test case for the
 * {@link RecordManager#update(Long, RecordCommand, RAuthenticationToken, HttpServletResponse)}
 * method.
 */
public class RecordManager_updateTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule ();

  @InjectMocks
  private RecordManager recordManager;

  @Mock
  private EntityManager em;

  @Mock
  private RegistreEntityManager rem;

  @Mock
  private Index index;

  @Mock
  private PictureRegistry pictureRegistry;

  @Mock
  private HttpServletResponse servletResponse;

  @Mock
  private MultipartFile picture;

  private RAuthenticationToken token;

  private User user;

  private RecordCommand command;

  @Before
  public void createToken () {
    User user = new User ("me", "me@example.com");
    token = new RAuthenticationToken (user, emptyList (), "");
  }

  @Before
  public void createUser () {
    user = new User ("you", "you@example.com");
  }

  @Before
  public void createCommand () {
    command = new RecordCommand ();
    command.setTitle ("A super record");
    command.setPicture (picture);
  }

  @Test
  public void should_update_a_movie_support () {
    command.setType (RecordCommand.Type.movie);
    command.setSupport (Movie.Support.DVD);
    Movie record = new Movie ("A super cool movie", user, Movie.Support.BRD);
    when (em.find (Record.class, 621L)).thenReturn (record);

    recordManager.update (621L, command, token, servletResponse);

    assertThat (record.getSupport ()).isEqualTo (Movie.Support.DVD);
  }

  @Test
  public void should_remove_actors () {
    List<String> actors = new ArrayList<> ();
    actors.add ("Orlando Bloom");
    actors.add ("Anthony Heads");
    command.setType (RecordCommand.Type.movie);
    command.setSupport (Movie.Support.BRD);
    command.setActors (actors);
    Movie record = new Movie ("A super cool movie", user, Movie.Support.BRD);
    record.getActors ().add (new Actor ("Anthony Heads"));
    record.getActors ().add (new Actor ("Ewan McGregor"));
    when (em.find (Record.class, 749L)).thenReturn (record);
    when (rem.supplyActor ("Orlando Bloom"))
        .thenReturn (new Actor ("Orlando Bloom"));
    when (rem.supplyActor ("Anthony Heads"))
        .thenReturn (new Actor ("Anthony Heads"));

    recordManager.update (749L, command, token, servletResponse);

    assertThat (record.getActors ())
        .extracting (Actor::getName)
        .containsExactlyInAnyOrder ("Orlando Bloom", "Anthony Heads");
  }

}
