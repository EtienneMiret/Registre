package fr.elimerl.registre.modèle.recherche.grammaire;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.modèle.recherche.signes.Opérateur;
import fr.elimerl.registre.modèle.recherche.signes.Parenthèse;
import fr.elimerl.registre.modèle.recherche.signes.Signe;

/**
 * Représente une requête de recherche dans son ensemble. Telle que définie dans
 * le langage de recherche, une requête est soit une conjonction soit une
 * disjonction d’{@link Expression}s.
 */
public class Requête {

    /** Journal SLF4J de cette classe. */
    private static final Logger journal =
	    LoggerFactory.getLogger(Requête.class);

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
     * Construit une requête à partir des signes résultants de l’analyse
     * lexicale. Consomme les signes de la file qui ont un sens, et s’arrête dès
     * qu’il n’est plus possible de construire la requête.
     *
     * @param signes
     *            la file de signes obtenue par l’analyse lexicale du texte
     *            entré par l’utilisateur.
     */
    public Requête(final Queue<Signe> signes) {
	expressions = new ArrayList<Expression>();
	boolean estConjonction; // Variable temporaire pour « conjonction ».
	try {
	    final Expression premièreExpression = new Expression(signes);
	    expressions.add(premièreExpression);
	    estConjonction = signes.peek() != Opérateur.OU;
	} catch (final ParseException e) {
	    journal.info("Grammaire de la requête incorrecte :", e);
	    estConjonction = true; // Peu importe.
	    signes.clear(); // Force l’arrêt de l’analyse.
	}
	conjonction = estConjonction;
	while (!signes.isEmpty() && signes.peek() != Parenthèse.FERMANTE) {
	    try {
		final Expression expressionSuivante = new Expression(signes);
		expressions.add(expressionSuivante);
		/*
		 * Si on est une disjonction et qu’on est pas à la fin, on doit
		 * trouver un opérateur « ou ».
		 */
		if (!conjonction
			&& !signes.isEmpty()
			&& signes.peek() != Parenthèse.FERMANTE
			&& signes.peek() != Opérateur.OU) {
		    journal.info("Grammaire de la requête incorrecte,"
			    + " « ou » attendu, {} trouvé.", signes.peek());
		    signes.clear(); // Forece l’arrêt de l’analyse.
		}
	    } catch (final ParseException e) {
		journal.info("Grammaire de la requête incorrecte :", e);
		signes.clear(); // Force l’arrêt de l’analyse.
	    }
	}
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
