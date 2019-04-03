package fr.elimerl.registre.search.grammar;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.tokens.Type;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Objects;

/**
 * Kind of {@link Expression} that models a query targeting a specific subtype
 * of {@link Record}.
 */
public final class TypeQuery extends Expression {

  private final Type type;

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
