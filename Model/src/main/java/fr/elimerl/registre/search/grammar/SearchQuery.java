package fr.elimerl.registre.search.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;

/**
 * Model a full search query. According to this application’s query language,
 * a search query is either a conjunction or a disjunction of
 * {@link Expression}s.
 */
public final class SearchQuery {

    /**
     * Whether this query is a conjunction. It is otherwise a disjunction.
     */
    private final boolean conjunction;

    /**
     * The expression list this query is made of.
     */
    private final List<Expression> expressions;

    /**
     * Create a new search query from the given expressions.
     *
     * @param conjunction
     *          {@code true} if this query is a conjunction, {@code false}
     *          otherwise.
     * @param expressions
     *          expressions this query is made of.
     */
    public SearchQuery(final boolean conjunction, final Expression... expressions) {
	this.conjunction = conjunction;
	this.expressions = Arrays.asList(expressions);
    }

    /**
     * Create a predicate that checks whether record matches this search query.
     *
     * @param builder
     *          query builder.
     * @param query
     *          the main query whose where clause is being built.
     * @param root
     *          the root of the main query.
     * @return a predicate linked to the given query that checks whether a
     *          record matches this search query.
     */
    public Predicate createPredicate(final CriteriaBuilder builder,
	    final CriteriaQuery<Record> query, final Root<Record> root) {
	final Predicate result;
	final List<Predicate> subPredicates =
		new ArrayList<Predicate>(expressions.size());
	final Predicate[] tableau = new Predicate[expressions.size()];
	for (final Expression e : expressions) {
	    subPredicates.add(e.createPredicate(builder, query, root));
	}
	if (conjunction) {
	    result = builder.and(subPredicates.toArray(tableau));
	} else {
	    result = builder.or(subPredicates.toArray(tableau));
	}
	return result;
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean result;
	if (this == objet) {
	    result = true;
	} else if (objet == null) {
	    result = false;
	} else if (objet instanceof SearchQuery) {
	    final SearchQuery query = (SearchQuery) objet;
	    if (expressions == null) {
		result = (query.expressions == null);
	    } else if (expressions.size() < 2) {
		result = expressions.equals(query.expressions);
	    } else {
		result = (conjunction == query.conjunction
			&& expressions.equals(query.expressions));
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public int hashCode() {
	return (expressions == null ? 0 : expressions.hashCode());
    }

    @Override
    public String toString() {
	final StringBuilder buffer = new StringBuilder();
	final Iterator<Expression> iterator = expressions.iterator();
	while (iterator.hasNext()) {
	    buffer.append(iterator.next());
	    if (iterator.hasNext()) {
		if (conjunction) {
		    buffer.append(' ');
		} else {
		    buffer.append(" ou ");
		}
	    }
	}
	return buffer.toString();
    }

}
