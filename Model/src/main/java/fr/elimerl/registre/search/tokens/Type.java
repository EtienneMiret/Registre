package fr.elimerl.registre.search.tokens;

import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Record;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * A type is a special keyword that can also be used with the “type:” operator.
 */
public class Type extends Keyword {

  /** Type for {@link Movie}s. */
  public static final Type MOVIE = new Type ("movie", Movie.class);

  /** Type for {@link Comic}s. */
  public static final Type COMIC = new Type ("comic", Comic.class);

  /** Type for {@link Book}s. */
  public static final Type BOOK = new Type ("book", Book.class);

  /** All types. Read-only. */
  public static final List<Type> all;

  static {
    List<Type> types = new ArrayList<> ();
    types.add (MOVIE);
    types.add (COMIC);
    types.add (BOOK);
    all = unmodifiableList (types);
  }

  /** The subtype of {@link Record} this type matches. */
  private final Class<? extends Record> clazz;

  /**
   * Create a new type from its string value.
   *
   * @param value
   *          this new pattern’s value as a string.
   * @param clazz
   *          the subtype class this type matches.
   */
  private Type (String value, Class<? extends Record> clazz) {
    super (value);
    this.clazz = clazz;
  }

  /**
   * Returns the class this type matches.
   *
   * @return the class this type matches.
   */
  public Class<? extends Record> getClazz () {
    return clazz;
  }

}
