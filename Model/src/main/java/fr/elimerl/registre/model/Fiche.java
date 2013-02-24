package fr.elimerl.registre.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
public abstract class Fiche {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(Fiche.class);

    /**
     * Identifiant de cet objet dans la base de données.
     */
    @Id
    @GeneratedValue
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
    @ManyToOne
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
    @ManyToOne
    @JoinColumn(name = "proprietaire")
    private Propriétaire propriétaire;

    /**
     * Emplacement où est rangé l’objet référencé. Peut être {@code null}.
     */
    @ManyToOne
    @JoinColumn(name = "emplacement")
    private Emplacement emplacement;

    /**
     * Utilisateur qui a créé cette référence.
     */
    @ManyToOne
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
    @ManyToOne
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
    public Fiche() {
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
    public Emplacement getEmplacement() {
        return emplacement;
    }

    /**
     * Définit l’emplacement de cette fiche.
     *
     * @param emplacement
     *            emplacement de cette fiche.
     * @see #emplacement
     */
    public void setEmplacement(final Emplacement emplacement) {
        this.emplacement = emplacement;
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
