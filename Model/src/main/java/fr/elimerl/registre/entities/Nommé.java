package fr.elimerl.registre.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe parente pour tous les objets de la base de données qui ont simplement
 * un nom.
 */
@MappedSuperclass
public abstract class Nommé {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(Nommé.class);

    /**
     * Id de cet objet dans la base de donnée.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Nom de cet objet. */
    @Column(name = "nom")
    private String nom;

    /**
     * Constructeur sans argument, requis par Hibernate.
     */
    protected Nommé() {
    }

    /**
     * Construit un nouveau {@link Nommé} à partir d’un nom. Deux nommés ne
     * peuvent avoir le même nom.
     *
     * @param nom
     *            nom de cette objet.
     */
    public Nommé(final String nom) {
	this.nom = nom;
	logger.debug("Création de {}.", this);
    }

    /**
     * Renvoie l’id de cet objet dans la base de données.
     *
     * @return l’id de cet objet dans la base de données, ou {@code null} si cet
     *         objet n’est pas en base.
     */
    public Long getId() {
        return id;
    }

    /**
     * Renvoie le nom de cet objet.
     *
     * @return le nom de cet objet.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Change le nom de cet objet.
     *
     * @param nom le nouveau nom de cet objet.
     */
    public void setNom(final String nom) {
	logger.debug("Nouveau nom de {} : {}.", this, nom);
        this.nom = nom;
    }

    @Override
    public String toString() {
	return nom;
    }

}
