package fr.elimerl.registre.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent clas for all database objects that simply have a name.
 */
@MappedSuperclass
public abstract class Named {

    /** This class’ logger. */
    private static final Logger logger = LoggerFactory.getLogger(Named.class);

    /**
     * Id of this object in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** This object’s name. */
    @Column(name = "nom")
    private String name;

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Named() {
    }

    /**
     * Create a new {@link Named} from a name. Two named can’t have the same
     * name.
     *
     * @param name
     *          this object’s name.
     */
    public Named(final String name) {
	this.name = name;
	logger.debug("“{}” created.", this);
    }

    /**
     * Returns this object’s id in the database.
     *
     * @return this object’s id in the database, or {@code null} if this object
     *          isn’t in the database.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns this object’s name.
     *
     * @return this object’s name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set this object’s name.
     *
     * @param name this object’s new name.
     */
    public void setName(final String name) {
	logger.debug("New name for “{}”: “{}”.", this, name);
        this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }

}
