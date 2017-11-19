package fr.elimerl.registre.search.grammar;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;

/**
 * Kind of {@link Expression} that models a search query within brackets.
 */
public final class BracketedQuery extends Expression {

    /** The search query inside this expression. */
    private final SearchQuery query;

    /**
     * Build a bracketed query from a search query.
     *
     * @param query
     *          the search query to bracket.
     */
    public BracketedQuery(final SearchQuery query) {
	this.query = query;
    }

    @Override
    public Predicate createPredicate(final CriteriaBuilder builder,
	    final CriteriaQuery<Record> query, final Root<Record> root) {
	return this.query.createPredicate(builder, query, root);
    }

    @Override
    public boolean equals(final Object object) {
	final boolean result;
	if (object == this) {
	    result = true;
	} else if (object instanceof BracketedQuery) {
	    final BracketedQuery other = (BracketedQuery) object;
	    if (query == null) {
		result = (other.query == null);
	    } else {
		result = query.equals(other.query);
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public int hashCode() {
	return (query == null ? 0 : query.hashCode());
    }

    @Override
    public String toString() {
	return "(" + query + ")";
    }

}
