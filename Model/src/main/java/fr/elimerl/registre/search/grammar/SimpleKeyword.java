package fr.elimerl.registre.search.grammar;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.Reference;
import fr.elimerl.registre.search.tokens.Keyword;

/**
 * Kind of expression of this application’s query language that only contains
 * a keyword.
 */
public final class SimpleKeyword extends Expression {

    /** This expression’s keyword. */
    private final Keyword keyword;

    /**
     * Create a {@code {@link SimpleKeyword} expression from a keyword.
     *
     * @param keyword
     *          keyword to put in this expression.
     */
    public SimpleKeyword(final Keyword keyword) {
	this.keyword = keyword;
    }

    @Override
    public Predicate createPredicate(final CriteriaBuilder builder,
	    final CriteriaQuery<Record> query, final Root<Record> root) {
	final Subquery<Long> subquery = query.subquery(Long.class);
	final Root<Reference> reference = subquery.from(Reference.class);
	subquery.select(reference.<Record>get("record").get("id"));
	subquery.where(builder.equal(reference.get("word").get("value"),
		keyword.getValue()));
	return builder.in(root.get("id")).value(subquery);
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean result;
	if (objet == this) {
	    result = true;
	} else if (objet == null) {
	    result = false;
	} else if (objet instanceof SimpleKeyword) {
	    final SimpleKeyword keyword = (SimpleKeyword) objet;
	    if (this.keyword == null) {
		result = (keyword.keyword == null);
	    } else {
		result = this.keyword.equals(keyword.keyword);
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public int hashCode() {
	return (keyword == null ? 0 : keyword.hashCode());
    }

    @Override
    public String toString() {
	return keyword.toString();
    }

}
