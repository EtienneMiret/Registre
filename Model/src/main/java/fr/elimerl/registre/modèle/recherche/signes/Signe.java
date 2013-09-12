package fr.elimerl.registre.modèle.recherche.signes;

/**
 * Classe racine de la hierachie des signes du langage de requête.
 */
public abstract class Signe {

    /** Ce signe sous forme de chaîne de caractères. */
    private final String représentation;

    /**
     * Construit un nouveau signe à partir de la chaîne de caractères le
     * représentant.
     *
     * @param représentation
     *            chaîne de charactère représentant le signe à créer.
     */
    public Signe(final String représentation) {
	this.représentation = représentation;
    }

    @Override
    public String toString() {
	return représentation;
    }

}
