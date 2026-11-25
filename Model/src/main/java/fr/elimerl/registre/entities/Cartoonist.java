package fr.elimerl.registre.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * A comic’s cartoonist.
 */
@Entity
@Table(name = "cartoonists")
public class Cartoonist extends Named {

    /**
     * Register a new cartoonist with the given name. There must be no such
     * cartoonist in the database.
     *
     * @param name
     * 		name of this cartoonist.
     */
    public Cartoonist(final String name) {
	super(name);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Cartoonist() {
	super();
    }

}
