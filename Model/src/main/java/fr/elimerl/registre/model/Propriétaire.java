package fr.elimerl.registre.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Un propriétaire d’un objet référencé. Les propriétaires sont souvent des
 * utilisateurs, mais pas toujours.
 */
@Entity
@Table(name = "proprietaires")
public class Propriétaire extends Nommé {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(Propriétaire.class);

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    public Propriétaire() {
	this(null);
    }

    /**
     * Construit un {@link Propriétaire} à partir du nom donné. Aucun
     * propriétaire avec ce nom ne doit exister dans la base de données.
     *
     * @param nom
     *            nom de ce propriétaire.
     */
    public Propriétaire(final String nom) {
	super(nom);
	if (nom != null) {
	    logger.debug("Création du propriétaire {}.", nom);
	}
    }

}
