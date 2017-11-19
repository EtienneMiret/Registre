package fr.elimerl.registre.search.grammar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import fr.elimerl.registre.entities.*;
import fr.elimerl.registre.entities.Actor;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.MotClé;

/**
 * Kind of {@link Expression} that models a query targeting a specific field.
 */
public final class FieldQuery extends Expression {

    /** A prime number. Used to compute the hash. */
    private static final int PRIME = 31;

    /** Field this query is made on. */
    private final Field field;

    /** Keyword list to find in the specified field. */
    private final List<MotClé> keywords;

    /**
     * Build a field query from a field and a set of keywords.
     * @param field
     *          field on which to make this query.
     * @param keywords
     *          keywords to find in the specified field.
     */
    public FieldQuery(final Field field, final MotClé... keywords) {
	this.field = field;
	this.keywords = Arrays.asList(keywords);
    }

    @Override
    public Predicate createPredicate(final CriteriaBuilder builder,
	    final CriteriaQuery<Record> query, final Root<Record> root) {
	final Predicate result;
	if (field == Field.TITLE) {
	    result = referencePredicate(Reference.Field.TITLE,
		    builder, query, root);
	} else if (field == Field.COMMENT) {
	    result = referencePredicate(Reference.Field.COMMENT,
		    builder, query, root);
	} else if (field == Field.ACTOR) {
	    result = actorPredicate(builder, query, root);
	} else {
	    result = namedPredicate(builder, query, root);
	}
	return result;
    }

    /**
     * Create a predicate for a referenced field.
     *
     * @param field
     *          the referenced field matching {@link #field}.
     * @param builder
     *          query builder.
     * @param query
     *          main query whose where clause is being built.
     * @param root
     *          root of the main query.
     * @return a predicate, linked to the specified query, that test whether a
     *          record matches the {@link #keywords} keywords in its
     *          {@link #field} field.
     */
    private Predicate referencePredicate(final Reference.Field field,
	    final CriteriaBuilder builder, final CriteriaQuery<Record> query,
            final Root<Record> root) {
	final Predicate[] predicates = new Predicate[keywords.size()];
	final Subquery<Long> subquery = query.subquery(Long.class);
	final Root<Reference> reference = subquery.from(Reference.class);
	final Path<String> mot = reference.get("word").get("value");
	subquery.select(reference.<Record>get("record").get("id"));
	for (int i = 0; i < keywords.size(); i++) {
	    predicates[i] = builder.equal(mot, keywords.get(i).getValeur());
	}
	subquery.where(builder.and(builder.and(predicates),
		builder.equal(reference.get("field"), field)));
	return builder.in(root.get("id")).value(subquery);
    }

    /**
     * Crée un prédicat pour le champ « acteur ».
     *
     * @param builder
     *            constructeur de requêtes.
     * @param query
     *            la requête principale dont on construit la clause where.
     * @param root
     *            la racine de la requête.
     * @return un prédicat, lié à la requête passée en paramètre, qui teste si
     *           une fiche de film contient les mot clés {@link #keywords} dans
     *           un de ses acteurs.
     */
    /**
     * Create a predicate for the “actor” field. This field is special because
     * it is multi-valued.
     *
     * @param builder
     *          query builder.
     * @param query
     *          main query whose where clause is being built.
     * @param root
     *          root of the main query.
     * @return a predicate, linked to the specified query, that test whether a
     *          movie record has the {@link #keywords} keywords in its actor
     *          set.
     */
    private Predicate actorPredicate(final CriteriaBuilder builder,
	    final CriteriaQuery<Record> query, final Root<Record> root) {
	final Predicate[] predicates = new Predicate[keywords.size()];
	final Subquery<Actor> subquery = query.subquery(Actor.class);
	final Root<Actor> actor = subquery.from(Actor.class);
	final Path<String> nom = actor.get("name");
	subquery.select(actor);
	for (int i = 0; i < keywords.size(); i++) {
	    predicates[i] = builder.like(builder.lower(nom),
		    "%" + keywords.get(i).getValeur() + "%");
	}
	subquery.where(builder.and(predicates));
	final Path<Set<Actor>> actors =
		builder.treat(root, Movie.class).get("actors");
	return builder.isMember(subquery, actors);
    }

    /**
     * Crée un prédicat pour un champ qui contient un {@link Named}.
     *
     * @param constructeur
     *            constructeur de requêtes.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête.
     * @return un prédicat, lié à la requête passée en paramètre, qui teste si
     *         une fiche contient les mot clés {@link #keywords} dans son champ
     *         {@link #field}.
     */
    /**
     * Create a predicate for a field containing a single {@link Named}.
     * @param builder
     *          query builder.
     * @param query
     *          main query whose where clause is being built.
     * @param root
     *          root of the main query.
     * @return a predicate, linked to the specified query, that test whether a
     *          record contains the {@link #keywords} keywords in its
     *          {@link #field} field.
     */
    private Predicate namedPredicate(final CriteriaBuilder builder,
	    final CriteriaQuery<Record> query, final Root<Record> root) {
	final Predicate[] predicates = new Predicate[keywords.size()];
	for (int i = 0; i < keywords.size(); i++) {
	    final String mot = keywords.get(i).getValeur();
	    final Root<?> racine = field.getDeclaringClass() == Record.class
		    ? root
		    : builder.treat(root, field.getDeclaringClass());
	    predicates[i] = builder.like(
		    builder.lower(racine
			    .get(field.getName()).<String>get("name")),
			    "%" + mot + "%");
	}
	return builder.and(predicates);
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean result;
	if (objet == this) {
	    result = true;
	} else if (objet instanceof FieldQuery) {
	    final FieldQuery query = (FieldQuery) objet;
	    if (field == null && keywords == null) {
		result = (query.field == null && query.keywords == null);
	    } else if (field == null) {
		result = (query.field == null
			&& keywords.equals(query.keywords));
	    } else if (keywords == null) {
		result = (field.equals(query.field)
			&& query.keywords == null);
	    } else {
		result = (field.equals(query.field)
			&& keywords.equals(query.keywords));
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public int hashCode() {
	return (keywords == null ? 0 : keywords.hashCode())
		+ PRIME * (field == null ? 0 : field.hashCode());
    }

    @Override
    public String toString() {
	final Iterator<MotClé> iterator = keywords.iterator();
	final StringBuilder buffer = new StringBuilder();
	buffer.append(field);
	buffer.append('(');
	while (iterator.hasNext()) {
	    buffer.append(iterator.next());
	    if (iterator.hasNext()) {
		buffer.append(' ');
	    }
	}
	buffer.append(')');
	return buffer.toString();
    }

}
