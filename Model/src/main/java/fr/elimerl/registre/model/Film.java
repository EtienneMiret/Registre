package fr.elimerl.registre.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Type de fiche qui représente un film.
 */
@Entity
@Table(name = "film")
public class Film extends Fiche {

    /**
     * Support physique sur lequel est enregistré un film.
     */
    public enum Support {
	/** Une cassette. */
	K7,
	/** Un DVD. */
	DVD,
	/** Un disque Blu-Ray. */
	BRD;
    }

    /** Support physique sur lequel est enregistré ce film. */
    @Enumerated(EnumType.STRING)
    @Column(name = "support", nullable = false)
    private Support support;

    /** Le réalisateur de ce film. */
    @Column(name = "realisateur")
    private Réalisateur réalisateur;

    /** Les acteurs ayant joué dans ce film. */
    @ManyToMany
    @JoinTable(
	name = "joue_dans",
	joinColumns = @JoinColumn(name = "film"),
	inverseJoinColumns = @JoinColumn(name = "acteur")
    )
    private Set<Acteur> acteurs;

    /** Le compositeur de la musique de ce film. */
    @Column(name = "compositeur")
    private Compositeur compositeur;

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
	if (créateur != null) {
	    this.acteurs = new HashSet<Acteur>();
	}
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    public Film() {
	this(null, null, null);
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
    public Set<Acteur> getActeurs() {
	return acteurs;
    }

    /**
     * Renvoie le compositeur de la musique de ce film.
     *
     * @return le compositeur de la musique de ce film.
     * @see #compositeur
     */
    public Compositeur getCompositeur() {
	return compositeur;
    }

    /**
     * Définit le compositeur de la musique de ce film.
     *
     * @param compositeur
     *            compositeur de la musique de ce film.
     * @see #compositeur
     */
    public void setCompositeur(final Compositeur compositeur) {
	this.compositeur = compositeur;
    }

}
