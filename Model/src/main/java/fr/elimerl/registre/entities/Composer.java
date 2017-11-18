package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A movie soundtrack composer.
 */
@Entity
@Table(name = "compositeurs")
public class Composer extends Nommé {

    /**
     * Register a new composer with the given name.
     * @param name
     * 		name of this composer.
     */
    public Composer(final String name) {
	super(name);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Composer() {
	super();
    }

}
