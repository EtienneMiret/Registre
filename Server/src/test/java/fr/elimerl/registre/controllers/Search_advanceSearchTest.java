package fr.elimerl.registre.controllers;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for the {@link Search#advancedSearch} method. Only checks the
 * building of the {@code q} redirect parameter.
 */
public class Search_advanceSearchTest {

  private Search search;

  private RedirectAttributesModelMap redirectAttributes;

  @Before
  public void setUp() {
    search = new Search();
    redirectAttributes = new RedirectAttributesModelMap();
  }

  @Test
  public void no_criteria() {
    String q = advancedSearch(
        null, null, null, null, null, null, null, null, null, null
    );

    assertThat(q).isEmpty();
  }

  @Test
  public void empty_lists() {
    String q = advancedSearch(
        emptyList(), emptyList(), emptyList(), emptyList(), emptyList(),
        emptyList(), emptyList(), emptyList(), emptyList(), emptyList()
    );

    assertThat(q).isEmpty();
  }

  @Test
  public void blank_values_only() {
    String q = advancedSearch(
        List.of("", "  "), null, null, null, null, null, null, null, null, null
    );

    assertThat(q).isEmpty();
  }

  @Test
  public void single_value() {
    String q = advancedSearch(
        List.of("Orlando Bloom"), null, null, null, null,
        null, null, null, null, null);

    assertThat(q).isEqualTo("(actor:(Orlando Bloom))");
  }

  @Test
  public void several_values_of_same_field() {
    String q = advancedSearch(
        List.of("Orlando Bloom", "Ewan McGregor"),
        null, null, null, null, null, null, null, null, null
    );

    assertThat(q).isEqualTo(
        "(actor:(Orlando Bloom) or actor:(Ewan McGregor))"
    );
  }

  @Test
  public void blank_values_are_ignored() {
    String q = advancedSearch(
        List.of("", "Orlando Bloom", " "),
        null, null, null, null, null, null, null, null, null
    );

    assertThat(q).isEqualTo("(actor:(Orlando Bloom))");
  }

  @Test
  public void several_fields() {
    String q = advancedSearch(
        null, null, List.of("Goscinny"), List.of("Uderzo", "Morris"),
        null, null, null, null, null, null
    );

    assertThat(q).isEqualTo(
        "(author:(Goscinny)) (cartoonist:(Uderzo) or cartoonist:(Morris))"
    );
  }

  @Test
  public void all_fields() {
    String q = advancedSearch(
        List.of("actor"), List.of("title"), List.of("author"), List.of("cartoonist"),
        List.of("composer"), List.of("director"), List.of("location"),
        List.of("owner"), List.of("script writer"), List.of("series")
    );

    assertThat(q).isEqualTo(
        "(title:(title)) (actor:(actor)) (author:(author)) "
            + "(cartoonist:(cartoonist)) "
            + "(composer:(composer)) (director:(director)) "
            + "(location:(location)) (owner:(owner)) "
            + "(scriptWriter:(script writer)) (series:(series))"
    );
  }

  @Test
  public void type_does_not_affect_query() {
    search.advancedSearch(
        redirectAttributes, "comic", null, null, null, null, null, null, null,
        null, null, List.of("Astérix")
    );

    assertThat(redirectAttributes.get("q")).isEqualTo("(series:(Astérix))");
  }

  private String advancedSearch(
      List<String> actors,
      List<String> titles,
      List<String> authors,
      List<String> cartoonists,
      List<String> composers,
      List<String> directors,
      List<String> locations,
      List<String> owners,
      List<String> scriptWriters,
      List<String> series
  ) {
    search.advancedSearch(
        redirectAttributes, null, titles, actors, authors, cartoonists,
        composers, directors, locations, owners, scriptWriters, series);
    return (String) redirectAttributes.get("q");
  }

}
