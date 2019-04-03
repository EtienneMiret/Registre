package fr.elimerl.registre.search.tokens;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * This token models an operator of the query language. For example the boolean
 * OR.
 */
public final class Operator extends Token {

  /** The boolean “OR”. */
  public static final Operator OR = new Operator ("or");

  /**
   * The “type:” operator, for selecting a subtype of
   * {@link fr.elimerl.registre.entities.Record}.
   */
  public static final Operator TYPE = new Operator ("type:");

  public static final List<Operator> all;

  static {
    List<Operator> operators = new ArrayList<> ();
    operators.add (OR);
    operators.add (TYPE);
    all = unmodifiableList (operators);
  }

  /**
   * Private constructor, since only a limited number of instances is allowed.
   *
   * @param representation
   *          keyword of the query language matching this operator.
   */
  private Operator (final String representation) {
    super (representation);
  }

}
