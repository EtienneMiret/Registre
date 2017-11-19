package fr.elimerl.registre.search.grammaire;

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
 * Représente une requête de recherche dans son ensemble. Telle que définie dans
 * le langage de recherche, une requête est soit une conjonction soit une
 * disjonction d’{@link Expression}s.
 */
public final class Requête {

    /**
     * Indique si cette requête est une conjonction. Sinon, c’est une
     * disjonction.
     */
    private final boolean conjonction;

    /**
     * La liste d’expressions qui composent cette requête.
     */
    private final List<Expression> expressions;

    /**
     * Construit une nouvelle requête à partir des expressions données en
     * argument.
     *
     * @param conjonction
     *            {@code true} si cette requête est une conjonction,
     *            {@code false} si c’est une disjonction.
     * @param expressions
     *            liste des expressions composants la requête.
     */
    public Requête(final boolean conjonction, final Expression... expressions) {
	this.conjonction = conjonction;
	this.expressions = Arrays.asList(expressions);
    }

    /**
     * Crée un prédicat qui vérifie qu’une fiche correspond bien aux critères de
     * cette requête.
     *
     * @param constructeur
     *            constructeur de requête.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête principale.
     * @return un prédicat lié à la requête passée en paramètre qui vérifie
     *         qu’une fiche correspond bien aux critères de cette requête.
     */
    public Predicate créerPrédicat(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Record> requête, final Root<Record> fiche) {
	final Predicate résultat;
	final List<Predicate> sousPrédicats =
		new ArrayList<Predicate>(expressions.size());
	final Predicate[] tableau = new Predicate[expressions.size()];
	for (final Expression e : expressions) {
	    sousPrédicats.add(e.créerPrédicat(constructeur, requête, fiche));
	}
	if (conjonction) {
	    résultat = constructeur.and(sousPrédicats.toArray(tableau));
	} else {
	    résultat = constructeur.or(sousPrédicats.toArray(tableau));
	}
	return résultat;
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean résultat;
	if (this == objet) {
	    résultat = true;
	} else if (objet == null) {
	    résultat = false;
	} else if (objet instanceof Requête) {
	    final Requête requête = (Requête) objet;
	    if (expressions == null) {
		résultat = (requête.expressions == null);
	    } else if (expressions.size() < 2) {
		résultat = expressions.equals(requête.expressions);
	    } else {
		résultat = (conjonction == requête.conjonction
			&& expressions.equals(requête.expressions));
	    }
	} else {
	    résultat = false;
	}
	return résultat;
    }

    @Override
    public int hashCode() {
	return (expressions == null ? 0 : expressions.hashCode());
    }

    @Override
    public String toString() {
	final StringBuilder buffer = new StringBuilder();
	final Iterator<Expression> itérateur = expressions.iterator();
	while (itérateur.hasNext()) {
	    buffer.append(itérateur.next());
	    if (itérateur.hasNext()) {
		if (conjonction) {
		    buffer.append(' ');
		} else {
		    buffer.append(" ou ");
		}
	    }
	}
	return buffer.toString();
    }

}
