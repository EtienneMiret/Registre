package fr.elimerl.registre.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.recherche.grammaire.Expression;
import fr.elimerl.registre.recherche.grammaire.MotCléSimple;
import fr.elimerl.registre.recherche.grammaire.Requête;
import fr.elimerl.registre.recherche.grammaire.RequêteEntreParenthèse;
import fr.elimerl.registre.recherche.grammaire.RequêteSurChamp;
import fr.elimerl.registre.recherche.signes.Champ;
import fr.elimerl.registre.recherche.signes.MotClé;
import fr.elimerl.registre.recherche.signes.Opérateur;
import fr.elimerl.registre.recherche.signes.Parenthèse;
import fr.elimerl.registre.recherche.signes.Signe;

/**
 * Classe singleton chargée de parser les recherches des utilisateurs.
 */
public class ParseurDeRecherches {

    /** Journal SLF4J de cette classe. */
    private static final Logger journal =
	    LoggerFactory.getLogger(ParseurDeRecherches.class);

    /** Un tableau d’{@code Expression}s de taille zéro. */
    private static final Expression[] EXPRESSIONS = new Expression[0];

    /** Un tableau de {@code MotClé}s de taille zéro. */
    private static final MotClé[] MOTS_CLÉS = new MotClé[0];

    /**
     * Réalise l’analyse lexicale puis l’analyse grammaticale de la requête
     * utilisateur passée en paramètre. Renvoie quelque chose quel que soit le
     * contenu de la requête utilisateur.
     *
     * @param requête
     *            requête fournie par l’utilisateur. Peut contenir n’importe
     *            quoi.
     * @return la requête obtenue suite à l’analyse du texte passé en paramètre.
     */
    public Requête analyser(final String requête) {
	return analyserGrammaticalement(analyserLexicalement(requête));
    }

    /**
     * Réalise l’analyse lexicale de la requête utilisateur passée en paramètre.
     *
     * @param requête
     *            requête fournie par l’utilisateur. Peut contenir n’importe
     *            quoi.
     * @return la file de symboles contenue dans la requête.
     */
    public Queue<Signe> analyserLexicalement(final String requête) {
	final Queue<Signe> résultat = new LinkedList<Signe>();
	int i = 0;
	while (i < requête.length()) {
	    Signe signe = null;
	    {
		final Matcher comparateur =
			Parenthèse.OUVRANTE.getMotif().matcher(requête);
		if (comparateur.find(i)) {
		    signe = Parenthèse.OUVRANTE;
		    i = comparateur.end();
		}
	    }
	    if (signe == null) {
		final Matcher comparateur =
			Parenthèse.FERMANTE.getMotif().matcher(requête);
		if (comparateur.find(i)) {
		    signe = Parenthèse.FERMANTE;
		    i = comparateur.end();
		}
	    }
	    final Iterator<Opérateur> opérateurs = Opérateur.tous().iterator();
	    while (signe == null && opérateurs.hasNext()) {
		final Opérateur opérateur = opérateurs.next();
		final Matcher comparateur =
			opérateur.getMotif().matcher(requête);
		if (comparateur.find(i)) {
		    signe = opérateur;
		    i = comparateur.end();
		}
	    }
	    final Iterator<Champ<?>> champs = Champ.tous().iterator();
	    while (signe == null && champs.hasNext()) {
		final Champ<?> champ = champs.next();
		final Matcher comparateur = champ.getMotif().matcher(requête);
		if (comparateur.find(i)) {
		    signe = champ;
		    i = comparateur.end();
		}
	    }
	    if (signe == null) {
		final Matcher comparateur = MotClé.MOTIF.matcher(requête);
		if (comparateur.find(i)) {
		    signe = new MotClé(comparateur.group(1).toLowerCase());
		    i = comparateur.end();
		}
	    }
	    if (signe == null) {
		i++;
	    } else {
		résultat.add(signe);
	    }
	}
	return résultat;
    }

    /**
     * Construit une requête à partir des signes résultants de l’analyse
     * lexicale. Consomme les signes de la file qui ont un sens, et s’arrête dès
     * qu’il n’est plus possible de construire la requête.
     *
     * @param signes
     *            la file de signes obtenue par l’analyse lexicale du texte
     *            entré par l’utilisateur.
     * @return la requête résultant de l’analyse grammaticale des signes
     *         données.
     */
    public Requête analyserGrammaticalement(final Queue<Signe> signes) {
	final List<Expression> expressions = new ArrayList<Expression>();
	boolean conjonction;
	if (!signes.isEmpty() && signes.peek() != Parenthèse.FERMANTE) {
	    try {
		expressions.add(analyserExpression(signes));
		conjonction = signes.peek() != Opérateur.OU;
		if (!conjonction) {
		    signes.poll(); // Il faut consommer l’opérateur.
		}
	    } catch (final ParseException e) {
		journal.warn("Grammaire de la requête incorrecte :", e);
		conjonction = true; // Peu importe.
		signes.clear(); // Force l’arrêt de l’analyse.
	    }
	} else {
	    conjonction = true;
	}
	while (!signes.isEmpty() && signes.peek() != Parenthèse.FERMANTE) {
	    try {
		expressions.add(analyserExpression(signes));
		/*
		 * Si on est une disjonction et qu’on est pas à la fin, on doit
		 * trouver un opérateur « ou ».
		 */
		if (!conjonction
			&& !signes.isEmpty()
			&& signes.peek() != Parenthèse.FERMANTE) {
		    final Signe signeSuivant = signes.poll();
		    if (signeSuivant != Opérateur.OU) {
			journal.warn("Grammaire de la requête incorrecte,"
				+ " « ou » attendu, {} trouvé.", signeSuivant);
			signes.clear(); // Force l’arrêt de l’analyse.
		    }
		}
	    } catch (final ParseException e) {
		journal.warn("Grammaire de la requête incorrecte :", e);
		signes.clear(); // Force l’arrêt de l’analyse.
	    }
	}
	return new Requête(conjonction, expressions.toArray(EXPRESSIONS));
    }

    /**
     * Lit la suite de signes donnée et tente de faire une expression à partir
     * des premier signes de la suite. Les signes utilisés sont consommés.
     *
     * @param signes
     *            suite de signes à analyser.
     * @return l’expression représentée par le début de la suite de signes.
     * @throws ParseException
     *             si le début de la suite de signes fournie ne peut pas
     *             représenter une expression.
     */
    private Expression analyserExpression(final Queue<Signe> signes)
	    throws ParseException {
	final Expression résultat;
	final Signe premierSigne = signes.poll();
	if (premierSigne == Parenthèse.OUVRANTE) {
	    final Requête sousRequête = analyserGrammaticalement(signes);
	    résultat = new RequêteEntreParenthèse(sousRequête);
	    final Signe signeSuivant = signes.poll();
	    if (signeSuivant != Parenthèse.FERMANTE) {
		throw new ParseException("« ) » attendu, « " + signeSuivant
			+ " » trouvé.", -1);
	    }
	} else if (premierSigne instanceof MotClé) {
	    résultat = new MotCléSimple((MotClé) premierSigne);
	} else if (premierSigne instanceof Champ) {
	    résultat = analyserChamp((Champ<?>) premierSigne, signes);
	} else {
	    throw new ParseException("Début d’expression attendu, « "
		    + premierSigne + " » trouvé.", -1);
	}
	return résultat;
    }

    /**
     * Lit la suite de signes donnée et tente d’y lire une requête sur un champ.
     * En cas de réussite, les signes utilisés sont consommés.
     *
     * @param champ
     *            le champ sur lequel on fait potentiellement une requête.
     * @param signes
     *            la suite de signes à analyser. Doit commencer par un mot-clé
     *            ou par une suite de mots-clés entre parenthèse.
     * @param <T>
     *            le type du champ sur lequel on fait potentiellement une
     *            requête.
     * @return une requête sur le champs donné, avec comme mots-clés ceux
     *         trouvés au début de la suite de signes.
     * @throws ParseException
     *             si la suite de signes ne commence ni par un mot-clé, ni par
     *             une suite de mots-clés entre parenthèse.
     */
    private static <T> RequêteSurChamp<T> analyserChamp(final Champ<T> champ,
	    final Queue<Signe> signes) throws ParseException {
	final RequêteSurChamp<T> résultat;
	final Signe premierSigne = signes.poll();
	if (premierSigne == Parenthèse.OUVRANTE) {
	    final List<MotClé> motsClés = new ArrayList<MotClé>();
	    while (signes.peek() instanceof MotClé) {
		motsClés.add((MotClé) signes.poll());
	    }
	    final Signe signeSuivant = signes.poll();
	    if (signeSuivant != Parenthèse.FERMANTE) {
		throw new ParseException("« ) » attendue, « "
			+ signeSuivant + " » trouvé.", -1);
	    }
	    résultat = new RequêteSurChamp<T>(champ,
		    motsClés.toArray(MOTS_CLÉS));
	} else if (premierSigne instanceof MotClé) {
	    résultat = new RequêteSurChamp<T>(champ, (MotClé) premierSigne);
	} else {
	    throw new ParseException("Mot-clé ou « ( » attendu, « "
		    + premierSigne + " » trouvé.", -1);
	}
	return résultat;
    }

}
