package fr.elimerl.registre.services;

import fr.elimerl.registre.entities.*;
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

  /** Test {@link AutoCompleter#listCartoonists()}. */
  @Test
  public void should_list_cartoonists() {
    List<Cartoonist> actual = autoCompleter.listCartoonists();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly(
            "Morris",
            "Jigounov"
        );
  }

  /** Test {@link AutoCompleter#listComposers()}. */
  @Test
  public void should_list_composers() {
    List<Composer> actual = autoCompleter.listComposers();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly(
            "Hans Zimmer",
            "Howard Shore"
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

  /** Test {@link AutoCompleter#listLocations()}. */
  @Test
  public void should_list_locations() {
    List<Location> actual = autoCompleter.listLocations();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly(
            "Poissy",
            "Singapour",
            "Verneuil",
            "La Roche sur Yon"
        );
  }

  /** Test {@link AutoCompleter#listOwners()}. */
  @Test
  public void should_list_owners() {
    List<Owner> actual = autoCompleter.listOwners();

    assertThat(actual)
        .extracting(Named::getName)
        .containsExactly(
            "Etienne",
            "Grégoire",
            "Claire"
        );
  }

}
