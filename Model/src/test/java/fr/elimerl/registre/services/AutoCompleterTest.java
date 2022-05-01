package fr.elimerl.registre.services;

import fr.elimerl.registre.entities.Actor;
import fr.elimerl.registre.entities.Author;
import fr.elimerl.registre.entities.Director;
import fr.elimerl.registre.entities.Named;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for {@link AutoCompleter}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class AutoCompleterTest {

  @Resource
  private AutoCompleter autoCompleter;

  /** Test {@link AutoCompleter#listActors()}. */
  @Test
  public void should_list_actors() {
    List<Actor> actual = autoCompleter.listActors();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly(
            "Scarlett Johansson",
            "Emma Watson",
            "Will Smith"
        );
  }

  /** Test {@link AutoCompleter#listAuthors()}. */
  @Test
  public void should_list_authors() {
    List<Author> actual = autoCompleter.listAuthors();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly(
            "Gav Thorpe",
            "Noick Kyme",
            "Tom Clancy"
        );
  }

  /** Test {@link AutoCompleter#listDirectors()}. */
  @Test
  public void should_list_directors() {
    List<Director> actual = autoCompleter.listDirectors();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly("Luc Besson");
  }

}
