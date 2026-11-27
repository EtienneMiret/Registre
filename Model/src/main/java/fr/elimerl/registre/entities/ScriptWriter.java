package fr.elimerl.registre.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * A comic’s script writer.
 */
@Entity
@Table(name = "script_writers")
public class ScriptWriter extends Named {

    /**
     * Create a new script writer with the given name. Two different script
     * writer cannot have the same name.
     * @param name
     *          name of this new script writer.
     */
    public ScriptWriter(final String name) {
	super(name);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected ScriptWriter() {
	super();
    }

}
