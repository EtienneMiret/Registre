package fr.elimerl.registre.modèle.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;

import fr.elimerl.registre.modèle.recherche.signes.Champ;
import fr.elimerl.registre.modèle.recherche.signes.MotClé;
import fr.elimerl.registre.modèle.recherche.signes.Opérateur;
import fr.elimerl.registre.modèle.recherche.signes.Parenthèse;
import fr.elimerl.registre.modèle.recherche.signes.Signe;

/**
 * Classe singleton chargée de parser les recherches des utilisateurs.
 */
public class ParseurDeRecherches {

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
	    final Iterator<Champ> champs = Champ.tous().iterator();
	    while (signe == null && champs.hasNext()) {
		final Champ champ = champs.next();
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

}
