package fr.elimerl.registre.search.grammar;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Test case for the {@link TypeQuery} class.
 */
public class TypeQueryTest {

  /**
   * Test the {@link TypeQuery#equals(Object)} and the
   * {@link TypeQuery#hashCode()} methods.
   */
  @Test
  public void equalsAndHashCode () {
    EqualsVerifier.forClass (TypeQuery.class)
        .verify ();
  }

}
