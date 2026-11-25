package fr.elimerl.registre.search.grammar;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.tokens.Type;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.Objects;

/**
 * Kind of {@link Expression} that models a query targeting a specific subtype
 * of {@link Record}.
 */
public final class TypeQuery extends Expression {

  /** Which subtype of {@link Record} is targeted. */
  private final Type type;

  /**
   * Create a new expression for targeting a subtype of {@link Record}.
   *
   * @param type
   *          the specific subtype of {@link Record} being targeted.
   */
  public TypeQuery (Type type) {
    this.type = type;
  }

  @Override
  public Predicate createPredicate (
      CriteriaBuilder builder,
      CriteriaQuery<Record> query,
      Root<Record> root
  ) {
    Subquery<Long> subquery = query.subquery (Long.class);
    Root<? extends Record> from = subquery.from (type.getClazz ());
    subquery.select (from.get ("id"));
    return builder.in (root.get ("id")).value (subquery);
  }

  @Override
  public boolean equals (Object other) {
    if (other == this) {
      return true;
    } else if (other == null) {
      return false;
    } else if (other instanceof TypeQuery) {
      TypeQuery otherQuery = (TypeQuery) other;
      return this.type == otherQuery.type;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode () {
    return Objects.hashCode (type);
  }

}
