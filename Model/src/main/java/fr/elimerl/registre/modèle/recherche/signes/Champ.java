package fr.elimerl.registre.modèle.recherche.signes;

import java.util.ArrayList;
import java.util.List;

/**
 * Ce signe représente un champ dans lequel chercher un mot clé. Par exemple le
 * titre, le commentaire ou l’auteur.
 */
public final class Champ extends Signe {

    /** Le titre d’une fiche. */
    public static final Champ TITRE = new Champ("titre:");

    /** Le commentaire associé à une fiche. */
    public static final Champ COMMENTAIRE = new Champ("commentaire:");

    /** La série dont fait partie une fiche. */
    public static final Champ SÉRIE = new Champ("série:");

    /** Le propriétaire d’une fiche. */
    public static final Champ PROPRIÉTAIRE = new Champ("propriétaire:");

    /** L’emplacement où est rangé une fiche. */
    public static final Champ EMPLACEMENT = new Champ("emplacement:");

    /** Le réalisateur d’un film. */
    public static final Champ RÉALISATEUR = new Champ("réalisateur:");

    /** Un acteur qui joue dans un film. */
    public static final Champ ACTEUR = new Champ("acteur:");

    /** Le compositeur de la musique d’un film. */
    public static final Champ COMPOSITEUR = new Champ("compositeur:");

    /** Le dessinateur d’une bande-dessinée. */
    public static final Champ DESSINATEUR = new Champ("dessinateur:");

    /** Le scénariste d’une bande-dessinée. */
    public static final Champ SCÉNARISTE = new Champ("scénarise:");

    /** L’auteur d’un livre. */
    public static final Champ AUTEUR = new Champ("auteur:");

    /**
     * Renvoie la liste de tous les champs existant.
     *
     * @return la liste de tous les champs existant.
     */
    public static List<Champ> tous() {
	final List<Champ> résultat = new ArrayList<Champ>(11);
	résultat.add(TITRE);
	résultat.add(COMMENTAIRE);
	résultat.add(SÉRIE);
	résultat.add(PROPRIÉTAIRE);
	résultat.add(EMPLACEMENT);
	résultat.add(EMPLACEMENT);
	résultat.add(ACTEUR);
	résultat.add(COMPOSITEUR);
	résultat.add(DESSINATEUR);
	résultat.add(SCÉNARISTE);
	résultat.add(AUTEUR);
	return résultat;
    }

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
