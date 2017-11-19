package fr.elimerl.registre.search.tokens;

import java.util.regex.Pattern;

/**
 * Dans une requête, un mot-clé est un mot que l’utilisateur souhaite retrouver
 * dans ses résultats.
 */
public final class MotClé extends Signe {

    /** Motif représentant l’ensemble des mot-clés. */
    public static final Pattern MOTIF =
	    Pattern.compile("\\G\\s*([\\p{L}\\d]+)");

    /** Le mot représenté par ce mot-clé, en tant que chaîne de caractères. */
    private final String valeur;

    /**
     * Construit un mot-clé représentant la chaîne passée en argument.
     *
     * @param valeur
     *            la chaîne que représente ce mot-clé. Ne doit contenir aucune
     *            espace ni ponctuation.
     */
    public MotClé(final String valeur) {
	super(valeur);
	this.valeur = valeur;
    }

    /**
     * Récupère la chaîne que représente ce mot-clé.
     *
     * @return la chaîne que représente ce mot-clé.
     */
    public String getValeur() {
	return valeur;
    }

    @Override
    public boolean equals(final Object autre) {
	final boolean résultat;
	if (this == autre) {
	    résultat = true;
	} else if (autre == null) {
	    résultat = false;
	} else if (autre instanceof MotClé) {
	    final MotClé motClé = (MotClé) autre;
	    if (valeur == null) {
		résultat = (motClé.valeur == null);
	    } else {
		résultat = valeur.equals(motClé.valeur);
	    }
	} else {
	    résultat = false;
	}
	return résultat;
    }

    @Override
    public int hashCode() {
	return (valeur == null ? 0 : valeur.hashCode());
    }

}
