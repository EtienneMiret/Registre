package fr.elimerl.registre.modèle.entités;

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
    private Set<Acteur> acteurs;

    /** Le compositeur de la musique de ce film. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compositeur")
    private Compositeur compositeur;

    /** Est-ce que ce film est un film d’action ? */
    @Column(name = "genre_action")
    private Boolean genreAction;

    /** Est-ce que ce film est un documentaire ? */
    @Column(name = "genre_documentaire")
    private Boolean genreDocumentaire;

    /** Est-ce que ce film est un film fantastique ? */
    @Column(name = "genre_fantastique")
    private Boolean genreFantastique;

    /** Est-ce que ce film est un film de guerre ? */
    @Column(name = "genre_guerre")
    private Boolean genreGuerre;

    /** Est-ce que ce film raconte une histoire vraie ? */
    @Column(name = "genre_histoire_vraie")
    private Boolean genreHistoireVraie;

    /** Est-ce que ce film a pour cadre une période de l’Histoire ? */
    @Column(name = "genre_historique")
    private Boolean genreHistorique;

    /** Est-ce que ce film fait rire ? */
    @Column(name = "genre_humour")
    private Boolean genreHumour;

    /** Est-ce que ce film est un film policier ? */
    @Column(name = "genre_policier")
    private Boolean genrePolicier;

    /** Est-ce que ce film est un film romantique ? */
    @Column(name = "genre_romantique")
    private Boolean genreRomantique;

    /** Est-ce que ce fim est un film de science-fiction ? */
    @Column(name = "genre_sf")
    private Boolean genreSf;

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
	this.acteurs = new HashSet<Acteur>();
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
	    for (final Acteur acteur : acteurs) {
		résultat.add(acteur.getNom());
	    }
	}
	if (compositeur != null) {
	    résultat.add(compositeur.getNom());
	}
	if (genreAction != null && genreAction.booleanValue()) {
	    résultat.add("action");
	}
	if (genreFantastique != null && genreFantastique.booleanValue()) {
	    résultat.add("fantastique");
	}
	if (genreHistoireVraie != null && genreHistoireVraie.booleanValue()) {
	    résultat.add("histoire vraie");
	}
	if (genreHistorique != null && genreHistorique.booleanValue()) {
	    résultat.add("historique");
	}
	if (genreHumour != null && genreHumour.booleanValue()) {
	    résultat.add("humour");
	    résultat.add("drôle");
	}
	if (genrePolicier != null && genrePolicier.booleanValue()) {
	    résultat.add("policier");
	}
	if (genreRomantique != null && genreRomantique.booleanValue()) {
	    résultat.add("romantique");
	}
	if (genreSf != null && genreSf.booleanValue()) {
	    résultat.add("SF");
	    résultat.add("science-fiction");
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

    /**
     * Est-ce que ce film est un film d’action ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreAction() {
        return genreAction;
    }

    /**
     * Définit si ce film est un film d’action.
     *
     * @param genreAction
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreAction(final Boolean genreAction) {
        this.genreAction = genreAction;
    }

    /**
     * Est-ce que ce film est un documentaire ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreDocumentaire() {
        return genreDocumentaire;
    }

    /**
     * Définit si ce film est un documentaire.
     *
     * @param genreDocumentaire
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreDocumentaire(final Boolean genreDocumentaire) {
        this.genreDocumentaire = genreDocumentaire;
    }

    /**
     * Est-ce que ce film est un film fantastique ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreFantastique() {
        return genreFantastique;
    }

    /**
     * Définit si ce film est un film fantastique.
     *
     * @param genreFantastique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreFantastique(final Boolean genreFantastique) {
        this.genreFantastique = genreFantastique;
    }

    /**
     * Est-ce que ce film est un film de guerre ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreGuerre() {
        return genreGuerre;
    }

    /**
     * Définit si ce film est un film de guerre.
     *
     * @param genreGuerre
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreGuerre(final Boolean genreGuerre) {
        this.genreGuerre = genreGuerre;
    }

    /**
     * Est-ce que ce film raconte une histoire vraie ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHistoireVraie() {
        return genreHistoireVraie;
    }

    /**
     * Définit si ce film raconte une histoire vraie.
     *
     * @param genreHistoireVraie
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHistoireVraie(final Boolean genreHistoireVraie) {
        this.genreHistoireVraie = genreHistoireVraie;
    }

    /**
     * Est-ce que ce film a pour cadre une période de l’Histoire ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHistorique() {
        return genreHistorique;
    }

    /**
     * Définit si ce film a pour cadre une période de l’Histoire.
     *
     * @param genreHistorique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHistorique(final Boolean genreHistorique) {
        this.genreHistorique = genreHistorique;
    }

    /**
     * Est-ce que ce film fait rire ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHumour() {
        return genreHumour;
    }

    /**
     * Définit si ce film fait rire.
     *
     * @param genreHumour
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHumour(final Boolean genreHumour) {
        this.genreHumour = genreHumour;
    }

    /**
     * Est-ce que ce film est un film policier ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenrePolicier() {
        return genrePolicier;
    }

    /**
     * Définit si ce film est un film policier.
     *
     * @param genrePolicier
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenrePolicier(final Boolean genrePolicier) {
        this.genrePolicier = genrePolicier;
    }

    /**
     * Est-ce que ce film est romantique ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreRomantique() {
        return genreRomantique;
    }

    /**
     * Définit si ce film est romantique.
     *
     * @param genreRomantique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreRomantique(final Boolean genreRomantique) {
        this.genreRomantique = genreRomantique;
    }

    /**
     * Est-ce que ce film est un film de science-fiction ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreSf() {
	return genreSf;
    }

    /**
     * Définit si ce film est un film de science-fiction.
     *
     * @param genreSf
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreSf(final Boolean genreSf) {
	this.genreSf = genreSf;
    }

}
