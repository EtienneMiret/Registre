package fr.elimerl.registre.modèle.recherche.grammaire;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
