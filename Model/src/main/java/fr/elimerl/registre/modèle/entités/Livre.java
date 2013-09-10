package fr.elimerl.registre.modèle.entités;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Type de fiche qui représente un livre.
 */
@Entity
@Table(name = "livres")
public class Livre extends Fiche {

    /** L’auteur de ce livre. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur")
    private Auteur auteur;

    /** Est-ce que l’action se déroule dans un univers fantastique ? */
    @Column(name = "genre_fantastique")
    private Boolean genreFantastique;

    /** Est-ce que ce livre raconte une histoire vraie ? */
    @Column(name = "genre_histoire_vraie")
    private Boolean genreHistoireVraie;

    /** Est-ce que l’action se déroule dans une période historique ? */
    @Column(name = "genre_historique")
    private Boolean genreHistorique;

    /** Est-ce que ce livre est drôle ? */
    @Column(name = "genre_humour")
    private Boolean genreHumour;

    /** Est-ce que ce livre est un policier ? */
    @Column(name = "genre_policier")
    private Boolean genrePolicier;

    /** Est-ce que l’histoire de ce livre est romantique ? */
    @Column(name = "genre_romantique")
    private Boolean genreRomantique;

    /** Est-ce que l’action se déroule dans le futur ? */
    @Column(name = "genre_sf")
    private Boolean genreSf;

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    public Livre() {
	super();
    }

    /**
     * Crée une nouvelle référence de livre.
     *
     * @param titre
     *            titre de ce livre.
     * @param créateur
     *            utilisateur qui a référencé ce livre.
     */
    public Livre(final String titre, final Utilisateur créateur) {
	super(titre, créateur);
    }

    /**
     * Renvoie l’auteur de ce livre.
     *
     * @return l’auteur de ce livre.
     */
    public Auteur getAuteur() {
	return auteur;
    }

    /**
     * Définit l’auteur de ce livre.
     *
     * @param auteur
     *            l’auteur de ce livre.
     */
    public void setAuteur(final Auteur auteur) {
	this.auteur = auteur;
    }

    /**
     * Est-ce que l’action se déroule dans un univers fantastique ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreFantastique() {
	return genreFantastique;
    }

    /**
     * Définit si l’action se déroule dans un univers fantastique.
     *
     * @param genreFantastique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreFantastique(final Boolean genreFantastique) {
	this.genreFantastique = genreFantastique;
    }

    /**
     * Est-ce que ce livre raconte une histoire vraie ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHistoireVraie() {
	return genreHistoireVraie;
    }

    /**
     * Définit si ce livre raconte une histoire vraie.
     *
     * @param genreHistoireVraie
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHistoireVraie(final Boolean genreHistoireVraie) {
	this.genreHistoireVraie = genreHistoireVraie;
    }

    /**
     * Est-ce que l’action se déroule dans une période de l’Histoire ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHistorique() {
	return genreHistorique;
    }

    /**
     * Définit si l’action se déroule dans une période de l’Histoire.
     *
     * @param genreHistorique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHistorique(final Boolean genreHistorique) {
	this.genreHistorique = genreHistorique;
    }

    /**
     * Est-ce que ce livre est drôle ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHumour() {
	return genreHumour;
    }

    /**
     * Définit si ce livre est drôle.
     *
     * @param genreHumour
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHumour(final Boolean genreHumour) {
	this.genreHumour = genreHumour;
    }

    /**
     * Est-ce que ce livre est un livre policier ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenrePolicier() {
	return genrePolicier;
    }

    /**
     * Définit si ce livre est un livre policier.
     *
     * @param genrePolicier
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenrePolicier(final Boolean genrePolicier) {
	this.genrePolicier = genrePolicier;
    }

    /**
     * Est-ce que l’histoire de ce livre est romantique ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreRomantique() {
	return genreRomantique;
    }

    /**
     * Définit si l’histoire de ce livre est romantique.
     *
     * @param genreRomantique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreRomantique(final Boolean genreRomantique) {
	this.genreRomantique = genreRomantique;
    }

    /**
     * Est-ce que ce livre est un livre de science-fiction ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreSf() {
	return genreSf;
    }

    /**
     * Définit si ce livre est un livre de science-fiction.
     *
     * @param genreSf
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreSf(final Boolean genreSf) {
	this.genreSf = genreSf;
    }

}
