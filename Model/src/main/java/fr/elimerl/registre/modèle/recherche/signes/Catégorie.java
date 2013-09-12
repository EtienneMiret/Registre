package fr.elimerl.registre.modèle.recherche.signes;

/**
 * Ce signe représente une catégorie dans laquelle chercher un mot clé. Par
 * exemple le titre, le commentaire ou l’auteur.
 */
public final class Catégorie extends Signe {

    /** Le titre d’une fiche. */
    public static final Catégorie TITRE = new Catégorie("titre:");

    /** Le commentaire associé à une fiche. */
    public static final Catégorie COMMENTAIRE = new Catégorie("commentaire:");

    /**
     * Constructeur privé, seul un nombre prédéfini d’instance étant autorisées.
     *
     * @param représentation
     *            chaîne de caractères représentant la catégorie.
     */
    private Catégorie(final String représentation) {
	super(représentation);
    }

}
