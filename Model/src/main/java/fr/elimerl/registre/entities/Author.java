package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A book author.
 */
@Entity
@Table(name = "auteurs")
public class Author extends Named {

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Author() {
	super();
    }

    /**
     * Create a new author with the given name. There must be no such author in
     * the database.
     *
     * @param name
     * 		name of this new author.
     */
    public Author(final String name) {
	super(name);
    }

}
