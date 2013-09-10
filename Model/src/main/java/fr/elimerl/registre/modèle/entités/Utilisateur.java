package fr.elimerl.registre.modèle.entités;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Un utilisateur de l’application.
 */
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    /**
     * Loggeur de cette classe.
     */
    private static final Logger logger =
	    LoggerFactory.getLogger(Utilisateur.class);

    /**
     * Identifiant de cet objet dans la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nom de cet utilisateur.
     */
    @Column(name = "nom")
    private String nom;

    /**
     * Adresse email de cet utilisateur.
     */
    @Column(name = "email")
    private String email;

    /**
     * Constructure sans arguments, utilisé par Hibernate.
     */
    public Utilisateur() {
	super();
    }

    /**
     * Construit un nouvel utilisateur, avec le nom et l’adresse email données
     * en paramètres.
     *
     * @param nom
     *            nom de cet utilisateur.
     * @param email
     *            adresse email de cet utilisateur.
     */
    public Utilisateur(final String nom, final String email) {
	super();
	this.nom = nom;
	this.email = email;
	logger.debug("Création de l’utilisateur : {}", this);
    }

    /**
     * Renvoie l’identifiant de cet utilisateur en base de donnée.
     *
     * @return l’identifiant de cet utilisateur en base de donnée.
     */
    public Long getId() {
	return id;
    }

    /**
     * Renvoie le nom de cet utilisateur.
     *
     * @return le nom de cet utilisateur.
     */
    public String getNom() {
	return nom;
    }

    /**
     * Renvoie l’adresse email de cet utilisateur.
     *
     * @return l’adresse email de cet utilisateur.
     */
    public String getEmail() {
	return email;
    }

    @Override
    public String toString() {
	return nom;
    }

    @Override
    public boolean equals(final Object autre) {
	if (this == autre) {
	    return true;
	} else if (autre == null) {
	    return false;
	} else if (autre instanceof Utilisateur) {
	    final Utilisateur autreUtilisateur = (Utilisateur) autre;
	    if (this.nom == null) {
		return (autreUtilisateur.nom == null);
	    } else {
		return this.nom.equals(autreUtilisateur.nom);
	    }
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	return (nom == null ? 0 : nom.hashCode());
    }

}
