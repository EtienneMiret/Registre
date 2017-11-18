package fr.elimerl.registre.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une fiche représente un objet référencé par l’application, par exemple un
 * film.
 */
@Entity
@Table(name = "fiches")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Fiche {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(Fiche.class);

    /**
     * Identifiant de cet objet dans la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Titre de l’objet référencé.
     */
    @Column(name = "titre")
    private String titre;

    /**
     * Série dont fait partie l’objet référencé. Peut être {@code null}.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serie")
    private Série série;

    /**
     * Commentaire laissé par les utilisateurs sur cette fiche. Peut être long.
     */
    @Column(name = "commentaire")
    private String commentaire;

    /**
     * UUID de l’image associée à cette fiche. Peut être {@code null}.
     */
    @Column(name = "image")
    private String image;

    /**
     * Propriétaire de l’objet référencé. Peut être {@code null}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire")
    private Propriétaire propriétaire;

    /**
     * Emplacement où est rangé l’objet référencé. Peut être {@code null}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emplacement")
    private Location emplacement;

    /** Est-ce que cette fiche référence une histoire d’action ? */
    @Column(name = "genre_action")
    private Boolean genreAction;

    /** Est-ce que cette fiche référence un documentaire ? */
    @Column(name = "genre_documentaire")
    private Boolean genreDocumentaire;

    /** Est-ce que cette fiche référence une histoire fantastique ? */
    @Column(name = "genre_fantastique")
    private Boolean genreFantastique;

    /** Est-ce que cette fiche référence une histoire de guerre ? */
    @Column(name = "genre_guerre")
    private Boolean genreGuerre;

    /** Est-ce que cette fiche référence une histoire vraie ? */
    @Column(name = "genre_histoire_vraie")
    private Boolean genreHistoireVraie;

    /**
     * Est-ce que l’histoire de cette fiche a pour cadre une période de
     * l’Histoire ?
     */
    @Column(name = "genre_historique")
    private Boolean genreHistorique;

    /** Est-ce que cette fiche référence une histoire drôle ? */
    @Column(name = "genre_humour")
    private Boolean genreHumour;

    /** Est-ce que cette fiche référence une histoire d’enquête de police ? */
    @Column(name = "genre_policier")
    private Boolean genrePolicier;

    /** Est-ce que l’histoire de cette fiche est romantique ? */
    @Column(name = "genre_romantique")
    private Boolean genreRomantique;

    /** Est-ce que cette fiche référence une histoire de science-fiction ? */
    @Column(name = "genre_sf")
    private Boolean genreSf;

    /**
     * Utilisateur qui a créé cette référence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createur", updatable = false)
    private Utilisateur créateur;

    /**
     * Date de création de cette référence.
     */
    @Column(name = "creation", updatable = false)
    private Date création;

    /**
     * Dernier utilisateur à avoir modifié cette fiche.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dernier_editeur")
    private Utilisateur dernierÉditeur;

    /**
     * Date de dernière modification de cette fiche.
     */
    @Column(name = "derniere_edition")
    private Date dernièreÉdition;

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Fiche() {
    }

    /**
     * Crée une nouvelle fiche.
     *
     * @param titre
     *            titre de la nouvelle fiche.
     * @param créateur
     *            utilisateur qui crée cette nouvelle fiche.
     */
    public Fiche(final String titre, final Utilisateur créateur) {
	final Date maintenant = new Date();
	this.titre = titre;
	this.créateur = créateur;
	this.création = maintenant;
	this.dernierÉditeur = créateur;
	this.dernièreÉdition = maintenant;
	logger.debug("{} crée une fiche pour « {} ».", créateur, titre);
    }

    /**
     * Indique que l’utilisateur passé en argument vient de modifier la fiche.
     * Met à jour le dernier éditeur et la date de dernière modification.
     *
     * @param utilisateur
     *            utilisateur qui modifie la fiche.
     * @see #dernierÉditeur
     * @see #dernièreÉdition
     */
    public void toucher(final Utilisateur utilisateur) {
	this.dernierÉditeur = utilisateur;
        this.dernièreÉdition = new Date();
    }

    /**
     * Renvoie le contenu de tous les champs de cette fiche, à part le titre et
     * le commentaire. Cette méthode est utilisé par le service d’indexation.
     *
     * @return le contenu de tous les champs de cette fiche, à part le titre et
     *         le commentaire.
     */
    public List<String> getAutresChamps() {
	final List<String> résultat = new ArrayList<String>();
	if (série != null) {
	    résultat.add(série.getNom());
	}
	if (propriétaire != null) {
	    résultat.add(propriétaire.getNom());
	}
	if (emplacement != null) {
	    résultat.add(emplacement.getNom());
	}
	if (créateur != null) {
	    résultat.add(créateur.getNom());
	}
	if (dernierÉditeur != null) {
	    résultat.add(dernierÉditeur.getNom());
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
     * Renvoie l’identifiant de cette fiche.
     *
     * @return l’identifiant de cette fiche.
     */
    public Long getId() {
        return id;
    }

    /**
     * Renvoie le titre de cette fiche.
     *
     * @return le titre de cette fiche.
     * @see #titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Modifie le titre de cette fiche.
     *
     * @param titre
     *            nouveau titre de cette fiche.
     * @see #titre
     */
    public void setTitre(final String titre) {
        this.titre = titre;
    }

    /**
     * Renvoie la série à laquelle appartient cette fiche.
     *
     * @return la série à laquelle appartient cette fiche.
     * @see #série
     */
    public Série getSérie() {
        return série;
    }

    /**
     * Définit la série à laquelle appartient cette fiche.
     *
     * @param série
     *            série à laquelle appartient cette fiche.
     */
    public void setSérie(final Série série) {
        this.série = série;
    }

    /**
     * Renvoie le commentaire placé sur cette fiche par les utilisateurs.
     *
     * @return le commentaire placé sur cette fiche par les utilisateurs.
     * @see #commentaire
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit le commentaire sur cette fiche.
     *
     * @param commentaire
     *            commentaire sur cette fiche.
     * @see #commentaire
     */
    public void setCommentaire(final String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * Renvoie l’UUID de l’image de cette fiche.
     *
     * @return l’UUID de l’image de cette fiche, ou {@code null} si cette fiche
     *         n’a pas d’image.
     * @see #image
     */
    public String getImage() {
        return image;
    }

    /**
     * Définit l’UUID de l’image de cette fiche.
     *
     * @param image
     *            UUID de l’image de cette fiche.
     * @see #image
     */
    public void setImage(final String image) {
        this.image = image;
    }

    /**
     * Renvoie le propriétaire de cette fiche.
     *
     * @return le propriétaire de cette fiche.
     * @see #propriétaire
     */
    public Propriétaire getPropriétaire() {
        return propriétaire;
    }

    /**
     * Définit le propriétaire de cette fiche.
     *
     * @param propriétaire
     *            propriétaire de cette fiche.
     * @see #propriétaire
     */
    public void setPropriétaire(final Propriétaire propriétaire) {
        this.propriétaire = propriétaire;
    }

    /**
     * Renvoie l’emplacement de cette fiche.
     *
     * @return l’emplacement de cette fiche.
     * @see #emplacement
     */
    public Location getEmplacement() {
        return emplacement;
    }

    /**
     * Définit l’emplacement de cette fiche.
     *
     * @param emplacement
     *            emplacement de cette fiche.
     * @see #emplacement
     */
    public void setEmplacement(final Location emplacement) {
        this.emplacement = emplacement;
    }

    /**
     * Est-ce que cette fiche référence une histoire d’action ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreAction() {
        return genreAction;
    }

    /**
     * Définit si cette fiche référence une histoire d’action.
     *
     * @param genreAction
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreAction(final Boolean genreAction) {
        this.genreAction = genreAction;
    }

    /**
     * Est-ce que cette fiche référence un documentaire ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreDocumentaire() {
        return genreDocumentaire;
    }

    /**
     * Définit si cette fiche référence un documentaire.
     *
     * @param genreDocumentaire
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreDocumentaire(final Boolean genreDocumentaire) {
        this.genreDocumentaire = genreDocumentaire;
    }

    /**
     * Est-ce que cette fiche référence une histoire fantastique ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreFantastique() {
        return genreFantastique;
    }

    /**
     * Définit si cette fiche référence une histoire fantastique.
     *
     * @param genreFantastique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreFantastique(final Boolean genreFantastique) {
        this.genreFantastique = genreFantastique;
    }

    /**
     * Est-ce que cette fiche référence une histoire de guerre ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreGuerre() {
        return genreGuerre;
    }

    /**
     * Définit si cette fiche référence une histoire de guerre.
     *
     * @param genreGuerre
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreGuerre(final Boolean genreGuerre) {
        this.genreGuerre = genreGuerre;
    }

    /**
     * Est-ce que cette fiche référence une histoire vraie ?
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
     * Est-ce que l’histoire de cette fiche a pour cadre une période de
     * l’Histoire ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHistorique() {
        return genreHistorique;
    }

    /**
     * Définit si l’histoire de cette fiche a pour cadre une période de
     * l’Histoire.
     *
     * @param genreHistorique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHistorique(final Boolean genreHistorique) {
        this.genreHistorique = genreHistorique;
    }

    /**
     * Est-ce que cette fiche référence une histoire drôle ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreHumour() {
        return genreHumour;
    }

    /**
     * Définit si cette fiche référence une histoire drôle.
     *
     * @param genreHumour
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreHumour(final Boolean genreHumour) {
        this.genreHumour = genreHumour;
    }

    /**
     * Est-ce que cette fiche référence une histoire d’enquête de police ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenrePolicier() {
        return genrePolicier;
    }

    /**
     * Définit si cette fiche référence une histoire d’enquête de police.
     *
     * @param genrePolicier
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenrePolicier(final Boolean genrePolicier) {
        this.genrePolicier = genrePolicier;
    }

    /**
     * Est-ce que l’histoire de cette fiche est romantique ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreRomantique() {
        return genreRomantique;
    }

    /**
     * Définit si l’histoire de cette fiche est romantique.
     *
     * @param genreRomantique
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreRomantique(final Boolean genreRomantique) {
        this.genreRomantique = genreRomantique;
    }

    /**
     * Est-ce que cette fiche référence une histoire de science-fiction ?
     *
     * @return {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *         {@code null} si on ne sait pas.
     */
    public Boolean getGenreSf() {
	return genreSf;
    }

    /**
     * Définit si cette fiche référence une histoire de science-fiction.
     *
     * @param genreSf
     *            {@link Boolean#TRUE} si oui, {@link Boolean#FALSE} si non, et
     *            {@code null} si on ne sait pas.
     */
    public void setGenreSf(final Boolean genreSf) {
	this.genreSf = genreSf;
    }

    /**
     * Renvoie le créateur de cette fiche.
     *
     * @return le créateur de cette fiche.
     * @see #créateur
     */
    public Utilisateur getCréateur() {
        return créateur;
    }

    /**
     * Renvoie la date de création de cette fiche.
     *
     * @return la date de création de cette fiche.
     * @see #création
     */
    public Date getCréation() {
        return création;
    }

    /**
     * Renvoie le dernier éditeur de cette fiche.
     *
     * @return le dernier éditeur de cette fiche.
     * @see #dernierÉditeur
     */
    public Utilisateur getDernierÉditeur() {
        return dernierÉditeur;
    }

    /**
     * Renvoie la date de dernière modification de cette fiche.
     *
     * @return la date de dernière modification de cette fiche.
     * @see #dernièreÉdition
     */
    public Date getDernièreÉdition() {
        return dernièreÉdition;
    }

    @Override
    public String toString() {
	return "Fiche:" + id + ":" + titre;
    }

}
