package fr.elimerl.registre.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Type de fiche qui représente un film.
 */
@Entity
@Table(name = "films")
public class Film extends Fiche {

    /**
     * Support physique sur lequel est enregistré un film.
     */
    public static enum Support {

	/** Une cassette. */
	K7("Cassette"),
	/** Un DVD. */
	DVD("DVD"),
	/** Dématérialisé. */
	DEM("Dématérialisé"),
	/** Un disque Blu-Ray. */
	BRD("Blu-Ray");

	/** Nom de ce support à afficher dans l’IHM. */
	private final String nom;

	/**
	 * Définit le nom de ce support.
	 *
	 * @param nom
	 *            nom de ce support à afficher dans l’IHM.
	 */
	private Support(final String nom) {
	    this.nom = nom;
	}

	@Override
	public String toString() {
	    return nom;
	}
    }

    /** Support physique sur lequel est enregistré ce film. */
    @Enumerated(EnumType.STRING)
    @Column(name = "support")
    private Support support;

    /** Le réalisateur de ce film. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realisateur")
    private Réalisateur réalisateur;

    /** Les acteurs ayant joué dans ce film. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
	name = "joue_dans",
	joinColumns = @JoinColumn(name = "film"),
	inverseJoinColumns = @JoinColumn(name = "acteur")
    )
    private Set<Actor> acteurs;

    /** Le compositeur de la musique de ce film. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compositeur")
    private Composer compositeur;

    /**
     * Crée une nouvelle référence de film.
     *
     * @param titre
     *            titre de ce film.
     * @param créateur
     *            utilisateur qui référence ce film.
     * @param support
     *            support physique sur lequel est enregistré ce film.
     */
    public Film(final String titre, final Utilisateur créateur,
	    final Support support) {
	super(titre, créateur);
	this.support = support;
	this.acteurs = new HashSet<Actor>();
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Film() {
	super();
    }

    @Override
    public List<String> getAutresChamps() {
	final List<String> résultat = super.getAutresChamps();
	résultat.add("film");
	if (support != null) {
	    résultat.add(support.toString());
	}
	if (réalisateur != null) {
	    résultat.add(réalisateur.getNom());
	}
	if (acteurs != null) {
	    for (final Actor acteur : acteurs) {
		résultat.add(acteur.getNom());
	    }
	}
	if (compositeur != null) {
	    résultat.add(compositeur.getNom());
	}
	return résultat;
    }

    /**
     * Renvoie le support sur lequel est enregistré ce film.
     *
     * @return le support sur lequel est enregistré ce film.
     * @see #support
     */
    public Support getSupport() {
	return support;
    }

    /**
     * Définit le support sur lequel est enregistré ce film.
     *
     * @param support
     *            support sur lequel est enregistré ce film.
     * @see #support
     */
    public void setSupport(final Support support) {
	this.support = support;
    }

    /**
     * Renvoie le réalisateur de ce film.
     *
     * @return le réalisateur de ce film.
     * @see #réalisateur
     */
    public Réalisateur getRéalisateur() {
	return réalisateur;
    }

    /**
     * Définit le réalisateur de ce film.
     *
     * @param réalisateur
     *            réalisateur de ce film.
     * @see #réalisateur
     */
    public void setRéalisateur(final Réalisateur réalisateur) {
	this.réalisateur = réalisateur;
    }

    /**
     * Renvoie l’ensemble des acteurs qui ont joué dans ce film.
     *
     * @return l’ensemble des acteurs qui ont joué dans ce film.
     * @see #acteurs
     */
    public Set<Actor> getActeurs() {
	return acteurs;
    }

    /**
     * Renvoie le compositeur de la musique de ce film.
     *
     * @return le compositeur de la musique de ce film.
     * @see #compositeur
     */
    public Composer getCompositeur() {
	return compositeur;
    }

    /**
     * Définit le compositeur de la musique de ce film.
     *
     * @param compositeur
     *            compositeur de la musique de ce film.
     * @see #compositeur
     */
    public void setCompositeur(final Composer compositeur) {
	this.compositeur = compositeur;
    }

}
