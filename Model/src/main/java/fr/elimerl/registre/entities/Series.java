package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A series of record. Eg “Lucky Luke” or “James Bond”.
 */
@Entity
@Table(name = "series")
public class Series extends Named {

    /**
     * Create a new series with the given name. There must be no series with
     * the same name in the database.
     * @param name
     *          name of this new series.
     */
    public Series(final String name) {
	super(name);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Series() {
	super();
    }

}
