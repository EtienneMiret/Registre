package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An owner of a registered item. Owners usually are users, but not necessarily.
 */
@Entity
@Table(name = "owners")
public class Owner extends Named {

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Owner() {
	super();
    }

    /**
     * Construit un {@link Owner} à partir du nom donné. Aucun
     * propriétaire avec ce nom ne doit exister dans la base de données.
     *
     * @param nom
     *            nom de ce propriétaire.
     */
    /**
     * Create an {@link Owner} from a given name. There must be no other owner
     * with this name in the database.
     *
     * @param name
     *          this owner’s name.
     */
    public Owner(final String name) {
	super(name);
    }

}
