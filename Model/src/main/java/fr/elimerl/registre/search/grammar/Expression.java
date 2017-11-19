package fr.elimerl.registre.search.grammar;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;

/**
 * An expression in this application’s query language. Used to build a
 * {@link SearchQuery} with boolean operators.
 */
public abstract class Expression {

    /**
     * Create a predicate that checks a record matches this expression.
     *
     * @param builder
     *          query builder.
     * @param query
     *          the main query whose where clause is being built.
     * @param root
     *          the root of the main query.
     * @return a predicate linked to the specified query that checks that a
     *          record matches this expression.
     */
    public abstract Predicate createPredicate(CriteriaBuilder builder,
	    CriteriaQuery<Record> query, Root<Record> root);

}
