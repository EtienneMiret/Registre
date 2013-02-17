package fr.elimerl.registre.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Un propriétaire d’un objet référencé. Les propriétaires sont souvent des
 * utilisateurs, mais pas toujours.
 */
@Entity
@Table(name = "proprietaires")
public class Propriétaire extends Nommé {

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    public Propriétaire() {
	super();
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
    }

}
