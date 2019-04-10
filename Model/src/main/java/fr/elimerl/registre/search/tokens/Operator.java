package fr.elimerl.registre.search.tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Collections.unmodifiableList;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * This token models an operator of the query language. For example the boolean
 * OR.
 */
public final class Operator extends Token {

  /** The boolean “OR”. */
  public static final Operator OR = new Operator (
      "or",
      Pattern.compile ("\\G\\s*or\\b", CASE_INSENSITIVE)
  );

  /**
   * The “type:” operator, for selecting a subtype of
   * {@link fr.elimerl.registre.entities.Record}.
   */
  public static final Operator TYPE = new Operator (
      "type:",
      Pattern.compile ("\\G\\s*type:", CASE_INSENSITIVE)
  );

  /** List of all operators. Read-only. */
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
   *     keyword of the query language matching this operator.
   * @param pattern
   *     the pattern matching this keyword.
   */
  private Operator (final String representation, final Pattern pattern) {
    super (representation, pattern);
  }

}
