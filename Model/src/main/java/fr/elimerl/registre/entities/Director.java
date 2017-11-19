package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A movie director.
 */
@Entity
@Table(name = "directors")
public class Director extends Named {

    /**
     * Register a new director with the given name.
     *
     * @param name
     *          name of this new director.
     */
    public Director(final String name) {
	super(name);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Director() {
	super();
    }

}
