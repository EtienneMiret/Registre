package fr.elimerl.registre.services;

import fr.elimerl.registre.entities.Actor;
import fr.elimerl.registre.entities.Author;
import fr.elimerl.registre.entities.Composer;
import fr.elimerl.registre.entities.Location;
import fr.elimerl.registre.entities.Named;
import fr.elimerl.registre.entities.Series;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for {@link AutoCompleter}.
 */
@RunWith (Parameterized.class)
@ContextConfiguration ("/applicationContext.xml")
public class AutoCompleterTest {

  @Parameters (name = "{0} in {1}")
  public static Iterable<Object[]> parameters () {
    return Arrays.asList (
        new Object[] {"Wil", Actor.class, new String[] {"Will Smith"}},
        new Object[] {"smi", Actor.class, new String[] {"Will Smith"}},
        new Object[] {"S", Actor.class, new String[] {"Scarlett Johansson", "Will Smith"}},
        new Object[] {"Tom", Author.class, new String[] {"Tom Clancy"}},
        new Object[] {"h", Composer.class, new String[] {"Hans Zimmer", "Howard Shore"}},
        new Object[] {"Mer", Series.class, new String[] {"Merlin"}},
        new Object[] {"l", Location.class, new String[] {"La Roche sur Yon", "Lyon"}},
        new Object[] {"%e%", Series.class, new String[] {}}
    );
  }

  @ClassRule
  public static final SpringClassRule springClassRule = new SpringClassRule ();

  @Rule
  public SpringMethodRule springMethodRule = new SpringMethodRule ();

  @Parameter (0)
  public String query;

  @Parameter (1)
  public Class<? extends Named> clazz;

  @Parameter (2)
  public String[] expected;

  @Resource
  private AutoCompleter autoCompleter;

  @Test
  public void shouldAutoComplete () {
    List<String> actual = autoCompleter.autoComplete (query, clazz);

    assertThat (actual).containsExactly (expected);
  }

}
