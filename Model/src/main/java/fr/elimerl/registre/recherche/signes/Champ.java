package fr.elimerl.registre.recherche.signes;

import java.util.ArrayList;
import java.util.List;

import fr.elimerl.registre.entities.*;
import fr.elimerl.registre.entities.Comic;

/**
 * Ce signe représente un champ dans lequel chercher un mot clé. Par exemple le
 * titre, le commentaire ou l’auteur.
 */
public final class Champ extends Signe {

    /*
     * Pour l’instant, on fait confiance aux utilisateurs de cette bibliothèque
     * pour ne pas modifier ce champ. À terme, il faudra peut-être l’encapsuler
     * pour le protéger des modifications.
     */
    /**
     * Liste de tous les champs existants. <em>En lecture seule.</em>
     */
    public static final List<Champ> tous = new ArrayList<Champ>();

    /** Le titre d’une fiche. */
    public static final Champ TITRE =
	    valeurDe("title", String.class, Record.class);

    /** Le commentaire associé à une fiche. */
    public static final Champ COMMENTAIRE =
	    valeurDe("comment", String.class, Record.class);

    /** La série dont fait partie une fiche. */
    public static final Champ SÉRIE =
	    valeurDe("series", Series.class, Record.class);

    /** Le propriétaire d’une fiche. */
    public static final Champ PROPRIÉTAIRE =
	    valeurDe("owner", Owner.class, Record.class);

    /** L’emplacement où est rangé une fiche. */
    public static final Champ EMPLACEMENT =
	    valeurDe("location", Location.class, Record.class);

    /** Le réalisateur d’un film. */
    public static final Champ RÉALISATEUR =
	    valeurDe("director", Director.class, Movie.class);

    /** Un acteur qui joue dans un film. */
    public static final Champ ACTEUR =
	    valeurDe("actor", Actor.class, Movie.class);

    /** Le compositeur de la musique d’un film. */
    public static final Champ COMPOSITEUR =
	    valeurDe("composer", Composer.class, Movie.class);

    /** Le dessinateur d’une bande-dessinée. */
    public static final Champ DESSINATEUR =
	    valeurDe("cartoonist", Cartoonist.class, Comic.class);

    /** Le scénariste d’une bande-dessinée. */
    public static final Champ SCÉNARISTE =
	    valeurDe("scriptWriter", ScriptWriter.class, Comic.class);

    /** L’auteur d’un livre. */
    public static final Champ AUTEUR =
	    valeurDe("author", Author.class, Book.class);

    /**
     * Crée un nouveau type de champ, et l’enregistre dans {@link #tous}.
     *
     * @param nom
     *            nom du champ à créer.
     * @param classe
     *            classe des objets mis dans le champ à créer.
     * @param classeDéclarante
     *            classe à laquelle appartient ce champ.
     * @return le nouveau champ.
     */
    private static Champ valeurDe(final String nom, final Class<?> classe,
	    final Class<? extends Record> classeDéclarante) {
	final Champ résultat = new Champ(nom, classe, classeDéclarante);
	tous.add(résultat);
	return résultat;
    }

    /**
     * La classe des objets mis dans ce champ.
     */
    private final Class<?> classe;

    /**
     * La classe à laquelle appartient ce champ. Par exemple {@link Movie} pour
     * le champ réalisateur et {@link Comic} pour le champ dessinateur.
     */
    private final Class<? extends Record> classeDéclarante;

    /**
     * Le nom de ce champ. Il s’agit à la fois du nom de l’attribut
     * correspondant à ce champ dans {@link Record} ou une de ses sous-classes et
     * du terme à mettre avec les ‘:’ pour faire une recherche sur ce champ.
     */
    private final String nom;

    /**
     * Constructeur privé, seul un nombre prédéfini d’instances étant
     * autorisées.
     *
     * @param nom
     *            nom de ce champ.Il s’agit à la fois du nom de l’attribut
     *            correspondant à ce champ dans {@link Record} ou une de ses
     *            sous-classes et du terme à mettre avec les ‘:’ pour faire une
     *            recherche sur ce champ.
     * @param classe
     *            classe des objets mis dans ce champ.
     * @param classeDéclarante
     *            classe à laquelle appartient ce champ.
     */
    private Champ(final String nom, final Class<?> classe,
	    final Class<? extends Record> classeDéclarante) {
	super(nom + ':');
	this.classe = classe;
	this.classeDéclarante = classeDéclarante;
	this.nom = nom;
    }

    /**
     * Renvoie la classe à laquelle appartiennent les objets mis dans ce champ.
     *
     * @return la classe à laquelle appartiennent les objets mis dans ce champ.
     */
    public Class<?> getClasse() {
	return classe;
    }

    /**
     * Renvoie la classe à laquelle appartient ce champ.
     *
     * @return la classe à laquelle appartient ce champ.
     */
    public Class<? extends Record> getClasseDéclarante() {
	return classeDéclarante;
    }

    /**
     * Renvoie le nom de ce champ. Il s’agit à la fois du nom de l’attribut
     * correspondant à ce champ dans {@link Record} ou une de ses sous-classes et
     * du terme à mettre avec les ‘:’ pour faire une recherche sur ce champ.
     *
     * @return le nom de ce champ.
     */
    public String getNom() {
	return nom;
    }

}
