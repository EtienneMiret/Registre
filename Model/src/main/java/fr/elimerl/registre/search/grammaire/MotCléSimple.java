package fr.elimerl.registre.search.grammaire;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.Reference;
import fr.elimerl.registre.search.signes.MotClé;

/**
 * Type d’expression du langage de recherche contenant seulement un mot-clé.
 */
public final class MotCléSimple extends Expression {

    /** Mot-clé contenu dans cette expression. */
    private final MotClé motClé;

    /**
     * Construit une expression {@code MotCléSimple} à partir d’un mot-clé.
     *
     * @param motClé
     *            mot-clé à mettre dans cette expression.
     */
    public MotCléSimple(final MotClé motClé) {
	this.motClé = motClé;
    }

    @Override
    public Predicate créerPrédicat(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Record> requête, final Root<Record> fiche) {
	final Subquery<Long> sousRequête = requête.subquery(Long.class);
	final Root<Reference> référence = sousRequête.from(Reference.class);
	sousRequête.select(référence.<Record>get("record").get("id"));
	sousRequête.where(constructeur.equal(référence.get("word").get("value"),
		motClé.getValeur()));
	return constructeur.in(fiche.get("id")).value(sousRequête);
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean résultat;
	if (objet == this) {
	    résultat = true;
	} else if (objet == null) {
	    résultat = false;
	} else if (objet instanceof MotCléSimple) {
	    final MotCléSimple motClé = (MotCléSimple) objet;
	    if (this.motClé == null) {
		résultat = (motClé.motClé == null);
	    } else {
		résultat = this.motClé.equals(motClé.motClé);
	    }
	} else {
	    résultat = false;
	}
	return résultat;
    }

    @Override
    public int hashCode() {
	return (motClé == null ? 0 : motClé.hashCode());
    }

    @Override
    public String toString() {
	return motClé.toString();
    }

}
