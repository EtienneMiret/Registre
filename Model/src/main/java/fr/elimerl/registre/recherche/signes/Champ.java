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

    /** Le titre d’une fiche. */
    public static final Champ<String> TITRE =
	    new Champ<String>("titre", String.class);

    /** Le commentaire associé à une fiche. */
    public static final Champ<String> COMMENTAIRE =
	    new Champ<String>("commentaire", String.class);

    /** La série dont fait partie une fiche. */
    public static final Champ<Série> SÉRIE =
	    new Champ<Série>("série", Série.class);

    /** Le propriétaire d’une fiche. */
    public static final Champ<Propriétaire> PROPRIÉTAIRE =
	    new Champ<Propriétaire>("propriétaire", Propriétaire.class);

    /** L’emplacement où est rangé une fiche. */
    public static final Champ<Emplacement> EMPLACEMENT =
	    new Champ<Emplacement>("emplacement", Emplacement.class);

    /** Le réalisateur d’un film. */
    public static final Champ<Réalisateur> RÉALISATEUR =
	    new Champ<Réalisateur>("réalisateur", Réalisateur.class);

    /** Un acteur qui joue dans un film. */
    public static final Champ<Acteur> ACTEUR =
	    new Champ<Acteur>("acteur", Acteur.class);

    /** Le compositeur de la musique d’un film. */
    public static final Champ<Compositeur> COMPOSITEUR =
	    new Champ<Compositeur>("compositeur", Compositeur.class);

    /** Le dessinateur d’une bande-dessinée. */
    public static final Champ<Dessinateur> DESSINATEUR =
	    new Champ<Dessinateur>("dessinateur", Dessinateur.class);

    /** Le scénariste d’une bande-dessinée. */
    public static final Champ<Scénariste> SCÉNARISTE =
	    new Champ<Scénariste>("scénariste", Scénariste.class);

    /** L’auteur d’un livre. */
    public static final Champ<Auteur> AUTEUR =
	    new Champ<Auteur>("auteur", Auteur.class);

    /**
     * Renvoie la liste de tous les champs existant.
     *
     * @return la liste de tous les champs existant.
     */
    public static List<Champ<?>> tous() {
	final List<Champ<?>> résultat = new ArrayList<Champ<?>>(11);
	résultat.add(TITRE);
	résultat.add(COMMENTAIRE);
	résultat.add(SÉRIE);
	résultat.add(PROPRIÉTAIRE);
	résultat.add(EMPLACEMENT);
	résultat.add(RÉALISATEUR);
	résultat.add(ACTEUR);
	résultat.add(COMPOSITEUR);
	résultat.add(DESSINATEUR);
	résultat.add(SCÉNARISTE);
	résultat.add(AUTEUR);
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
