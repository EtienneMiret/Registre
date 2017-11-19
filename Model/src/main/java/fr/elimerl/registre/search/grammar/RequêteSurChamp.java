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
import fr.elimerl.registre.search.signes.Champ;
import fr.elimerl.registre.search.signes.MotClé;

/**
 * Type d’expression du langage de recherche qui représente une requête sur un
 * champ.
 */
public final class RequêteSurChamp extends Expression {

    /** Un nombre premier. Utiliser pour calculer le hash. */
    private static final int PREMIER = 31;

    /** Champ sur lequel cette requête est faite. */
    private final Champ champ;

    /** Liste de mots-clés à trouver pour cette requête. */
    private final List<MotClé> motsClés;

    /**
     * Construit une requête sur un champ à partir du champ concerné et des
     * mots-clés à y trouver.
     *
     * @param champ
     *            champ sur lequel la requête est faite.
     * @param motsClés
     *            liste de mots-clés à trouver dans le champ.
     */
    public RequêteSurChamp(final Champ champ, final MotClé... motsClés) {
	this.champ = champ;
	this.motsClés = Arrays.asList(motsClés);
    }

    @Override
    public Predicate createPredicate(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Record> requête, final Root<Record> fiche) {
	final Predicate résultat;
	if (champ == Champ.TITRE) {
	    résultat = prédicatPourRéférence(Reference.Field.TITLE,
		    constructeur, requête, fiche);
	} else if (champ == Champ.COMMENTAIRE) {
	    résultat = prédicatPourRéférence(Reference.Field.COMMENT,
		    constructeur, requête, fiche);
	} else if (champ == Champ.ACTEUR) {
	    résultat = prédicatPourActeur(constructeur, requête, fiche);
	} else {
	    résultat = prédicatPourNommé(constructeur, requête, fiche);
	}
	return résultat;
    }

    /**
     * Crée un prédicat pour un champ référencé.
     *
     * @param champ
     *            le champ référencé correspondant au {@link #champ}.
     * @param constructeur
     *            constructeur de requêtes.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête.
     * @return un prédicat, lié à la requête passée en paramètre, qui teste si
     *           une fiche contient les mot clés {@link #motsClés} dans son
     *           champ {@link #champ}.
     */
    private Predicate prédicatPourRéférence(final Reference.Field champ,
	    final CriteriaBuilder constructeur,
	    final CriteriaQuery<Record> requête, final Root<Record> fiche) {
	final Predicate[] prédicats = new Predicate[motsClés.size()];
	final Subquery<Long> sousRequête = requête.subquery(Long.class);
	final Root<Reference> référence = sousRequête.from(Reference.class);
	final Path<String> mot = référence.get("word").get("value");
	sousRequête.select(référence.<Record>get("record").get("id"));
	for (int i = 0; i < motsClés.size(); i++) {
	    prédicats[i] = constructeur.equal(mot, motsClés.get(i).getValeur());
	}
	sousRequête.where(constructeur.and(constructeur.and(prédicats),
		constructeur.equal(référence.get("field"), champ)));
	return constructeur.in(fiche.get("id")).value(sousRequête);
    }

    /**
     * Crée un prédicat pour le champ « acteur ».
     *
     * @param constructeur
     *            constructeur de requêtes.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête.
     * @return un prédicat, lié à la requête passée en paramètre, qui teste si
     *           une fiche de film contient les mot clés {@link #motsClés} dans
     *           un de ses acteurs.
     */
    private Predicate prédicatPourActeur(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Record> requête, final Root<Record> fiche) {
	final Predicate[] prédicats = new Predicate[motsClés.size()];
	final Subquery<Actor> sousRequête = requête.subquery(Actor.class);
	final Root<Actor> acteur = sousRequête.from(Actor.class);
	final Path<String> nom = acteur.get("name");
	sousRequête.select(acteur);
	for (int i = 0; i < motsClés.size(); i++) {
	    prédicats[i] = constructeur.like(constructeur.lower(nom),
		    "%" + motsClés.get(i).getValeur() + "%");
	}
	sousRequête.where(constructeur.and(prédicats));
	final Path<Set<Actor>> acteurs =
		constructeur.treat(fiche, Movie.class).get("actors");
	return constructeur.isMember(sousRequête, acteurs);
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
     *         une fiche contient les mot clés {@link #motsClés} dans son champ
     *         {@link #champ}.
     */
    private Predicate prédicatPourNommé(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Record> requête, final Root<Record> fiche) {
	final Predicate[] prédicats = new Predicate[motsClés.size()];
	for (int i = 0; i < motsClés.size(); i++) {
	    final String mot = motsClés.get(i).getValeur();
	    final Root<?> racine = champ.getClasseDéclarante() == Record.class
		    ? fiche
		    : constructeur.treat(fiche, champ.getClasseDéclarante());
	    prédicats[i] = constructeur.like(
		    constructeur.lower(racine
			    .get(champ.getNom()).<String>get("name")),
			    "%" + mot + "%");
	}
	return constructeur.and(prédicats);
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean résultat;
	if (objet == this) {
	    résultat = true;
	} else if (objet instanceof RequêteSurChamp) {
	    final RequêteSurChamp requête = (RequêteSurChamp) objet;
	    if (champ == null && motsClés == null) {
		résultat = (requête.champ == null && requête.motsClés == null);
	    } else if (champ == null) {
		résultat = (requête.champ == null
			&& motsClés.equals(requête.motsClés));
	    } else if (motsClés == null) {
		résultat = (champ.equals(requête.champ)
			&& requête.motsClés == null);
	    } else {
		résultat = (champ.equals(requête.champ)
			&& motsClés.equals(requête.motsClés));
	    }
	} else {
	    résultat = false;
	}
	return résultat;
    }

    @Override
    public int hashCode() {
	return (motsClés == null ? 0 : motsClés.hashCode())
		+ PREMIER * (champ == null ? 0 : champ.hashCode());
    }

    @Override
    public String toString() {
	final Iterator<MotClé> itérateur = motsClés.iterator();
	final StringBuilder buffer = new StringBuilder();
	buffer.append(champ);
	buffer.append('(');
	while (itérateur.hasNext()) {
	    buffer.append(itérateur.next());
	    if (itérateur.hasNext()) {
		buffer.append(' ');
	    }
	}
	buffer.append(')');
	return buffer.toString();
    }

}
