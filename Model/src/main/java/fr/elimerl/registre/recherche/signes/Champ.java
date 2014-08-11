package fr.elimerl.registre.recherche.signes;

import java.util.ArrayList;
import java.util.List;

import fr.elimerl.registre.entités.Acteur;
import fr.elimerl.registre.entités.Auteur;
import fr.elimerl.registre.entités.Compositeur;
import fr.elimerl.registre.entités.Dessinateur;
import fr.elimerl.registre.entités.Emplacement;
import fr.elimerl.registre.entités.Fiche;
import fr.elimerl.registre.entités.Propriétaire;
import fr.elimerl.registre.entités.Réalisateur;
import fr.elimerl.registre.entités.Scénariste;
import fr.elimerl.registre.entités.Série;

/**
 * Ce signe représente un champ dans lequel chercher un mot clé. Par exemple le
 * titre, le commentaire ou l’auteur.
 *
 * @param <T>
 *            le type d’objet mis dans ce champ.
 */
public final class Champ<T> extends Signe {

    /**
     * Liste de tous les champs existants. <em>En lecture seule.</em>
     */
    /*
     * Pour l’instant, on fait confiance aux utilisateurs de cette bibliothèque
     * pour ne pas modifier ce champ. À terme, il faudra peut-être l’encapsuler
     * pour le protéger des modifications.
     */
    public static final List<Champ<?>> tous = new ArrayList<Champ<?>>();

    /** Le titre d’une fiche. */
    public static final Champ<String> TITRE =
	    valeurDe("titre", String.class);

    /** Le commentaire associé à une fiche. */
    public static final Champ<String> COMMENTAIRE =
	    valeurDe("commentaire", String.class);

    /** La série dont fait partie une fiche. */
    public static final Champ<Série> SÉRIE =
	    valeurDe("série", Série.class);

    /** Le propriétaire d’une fiche. */
    public static final Champ<Propriétaire> PROPRIÉTAIRE =
	    valeurDe("propriétaire", Propriétaire.class);

    /** L’emplacement où est rangé une fiche. */
    public static final Champ<Emplacement> EMPLACEMENT =
	    valeurDe("emplacement", Emplacement.class);

    /** Le réalisateur d’un film. */
    public static final Champ<Réalisateur> RÉALISATEUR =
	    valeurDe("réalisateur", Réalisateur.class);

    /** Un acteur qui joue dans un film. */
    public static final Champ<Acteur> ACTEUR =
	    valeurDe("acteur", Acteur.class);

    /** Le compositeur de la musique d’un film. */
    public static final Champ<Compositeur> COMPOSITEUR =
	    valeurDe("compositeur", Compositeur.class);

    /** Le dessinateur d’une bande-dessinée. */
    public static final Champ<Dessinateur> DESSINATEUR =
	    valeurDe("dessinateur", Dessinateur.class);

    /** Le scénariste d’une bande-dessinée. */
    public static final Champ<Scénariste> SCÉNARISTE =
	    valeurDe("scénariste", Scénariste.class);

    /** L’auteur d’un livre. */
    public static final Champ<Auteur> AUTEUR =
	    valeurDe("auteur", Auteur.class);

    /**
     * Crée un nouveau type de champ, et l’enregistre dans {@link #tous}.
     * @param nom nom du champ à créer.
     * @param classe classe des objets mis dans le champ à créer.
     * @return le nouveau champ.
     */
    private static <T> Champ<T> valeurDe(final String nom,
	    final Class<T> classe) {
	final Champ<T> résultat = new Champ<T>(nom, classe);
	tous.add(résultat);
	return résultat;
    }

    /**
     * La classe des objets mis dans ce champ.
     */
    private final Class<T> classe;

    /**
     * Le nom de ce champ. Il s’agit à la fois du nom de l’attribut
     * correspondant à ce champ dans {@link Fiche} ou une de ses sous-classes et
     * du terme à mettre avec les ‘:’ pour faire une recherche sur ce champ.
     */
    private final String nom;

    /**
     * Constructeur privé, seul un nombre prédéfini d’instances étant
     * autorisées.
     *
     * @param nom
     *            nom de ce champ.Il s’agit à la fois du nom de l’attribut
     *            correspondant à ce champ dans {@link Fiche} ou une de ses
     *            sous-classes et du terme à mettre avec les ‘:’ pour faire une
     *            recherche sur ce champ.
     * @param classe
     *            classe des objets mis dans ce champ.
     */
    private Champ(final String nom, final Class<T> classe) {
	super(nom + ':');
	this.classe = classe;
	this.nom = nom;
    }

    /**
     * Renvoie la classe à laquelle appartiennent les objets mis dans ce champ.
     *
     * @return la classe à laquelle appartiennent les objets mis dans ce champ.
     */
    public Class<T> getClasse() {
	return classe;
    }

    /**
     * Renvoie le nom de ce champ. Il s’agit à la fois du nom de l’attribut
     * correspondant à ce champ dans {@link Fiche} ou une de ses sous-classes et
     * du terme à mettre avec les ‘:’ pour faire une recherche sur ce champ.
     *
     * @return le nom de ce champ.
     */
    public String getNom() {
	return nom;
    }

}
