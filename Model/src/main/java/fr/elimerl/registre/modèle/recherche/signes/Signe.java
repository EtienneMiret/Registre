package fr.elimerl.registre.modèle.recherche.signes;

import java.util.regex.Pattern;

/**
 * Classe racine de la hierachie des signes du langage de requête.
 */
public abstract class Signe {

    /** Ce signe sous forme de chaîne de caractères. */
    private final String représentation;

    /**
     * Le motif qui permet de détecter ce signe dans une chaîne de caractères.
     */
    private final Pattern motif;

    /**
     * Construit un nouveau signe à partir de la chaîne de caractères le
     * représentant.
     *
     * @param représentation
     *            chaîne de charactère représentant le signe à créer.
     */
    public Signe(final String représentation) {
	this.représentation = représentation;
	this.motif = Pattern.compile("\\G\\s*" + Pattern.quote(représentation),
		Pattern.CASE_INSENSITIVE);
    }

    /**
     * Renvoie le motif qui permet de détecter ce signe dans une chaîne de
     * caractères.
     *
     * @return le motif qui permet de détecter ce signe dans une chaîne de
     *         caractères.
     */
    public Pattern getMotif() {
	return motif;
    }

    @Override
    public String toString() {
	return représentation;
    }

}
