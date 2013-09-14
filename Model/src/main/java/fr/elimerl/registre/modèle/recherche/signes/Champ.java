package fr.elimerl.registre.modèle.recherche.signes;

/**
 * Ce signe représente une catégorie dans laquelle chercher un mot clé. Par
 * exemple le titre, le commentaire ou l’auteur.
 */
public final class Champ extends Signe {

    /** Le titre d’une fiche. */
    public static final Champ TITRE = new Champ("titre:");

    /** Le commentaire associé à une fiche. */
    public static final Champ COMMENTAIRE = new Champ("commentaire:");

    /**
     * Constructeur privé, seul un nombre prédéfini d’instance étant autorisées.
     *
     * @param représentation
     *            chaîne de caractères représentant la catégorie.
     */
    private Champ(final String représentation) {
	super(représentation);
    }

}
