package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A location where are kept some records. Eg “Etienne’s bedroom”.
 */
@Entity
@Table(name = "emplacements")
public class Location extends Named {

    /**
     * Create a new location with the given name. There must be no such location
     * in the database.
     * @param name
     * 		name of this new location.
     */
    public Location(final String name) {
	super(name);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Location() {
	super();
    }

}
