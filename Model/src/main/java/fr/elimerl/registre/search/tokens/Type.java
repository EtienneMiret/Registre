package fr.elimerl.registre.search.tokens;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Type extends Token {

  /** Type for {@link fr.elimerl.registre.entities.Movie}s. */
  public static final Type MOVIE = new Type ("movie");

  /** Type for {@link fr.elimerl.registre.entities.Comic}s. */
  public static final Type COMIC = new Type ("comic");

  /** Type for {@link fr.elimerl.registre.entities.Book}s. */
  public static final Type BOOK = new Type ("book");

  /** All types. Read-only. */
  public static final List<Type> all;

  static {
    List<Type> types = new ArrayList<> ();
    types.add (MOVIE);
    types.add (COMIC);
    types.add (BOOK);
    all = unmodifiableList (types);
  }

  /**
   * Create a new type from its string value.
   *
   * @param value this new pattern’s value as a string.
   */
  private Type (String value) {
    super (value);
  }

}
