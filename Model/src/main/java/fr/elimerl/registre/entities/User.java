package fr.elimerl.registre.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An application’s user.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * This class’ logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    /**
     * Id of this user in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Name of this user.
     */
    @Column(name = "name")
    private String name;

    /**
     * Email address of this user.
     */
    @Column(name = "email")
    private String email;

    /**
     * No-args constructor. Used by Hibernate.
     */
    protected User() {
	super();
    }

    /**
     * Create a new user, with the given name and email address.
     *
     * @param name
     *          this new user’s name.
     * @param email
     *          this new user’s email address.
     */
    public User(final String name, final String email) {
	super();
	this.name = name;
	this.email = email;
	logger.debug("User {} created.", this);
    }

    /**
     * Returns this user’s id in the database.
     *
     * @return this user’s id in the database.
     */
    public Long getId() {
	return id;
    }

    /**
     * Returns this user’s name.
     *
     * @return this user’s name.
     */
    public String getName() {
	return name;
    }

    /**
     * Returns this user’s email address.
     *
     * @return this user’s email address.
     */
    public String getEmail() {
	return email;
    }

    @Override
    public String toString() {
	return name;
    }

    @Override
    public boolean equals(final Object other) {
	if (this == other) {
	    return true;
	} else if (other == null) {
	    return false;
	} else if (other instanceof User) {
	    final User otherUser = (User) other;
	    if (this.name == null) {
		return (otherUser.name == null);
	    } else {
		return this.name.equals(otherUser.name);
	    }
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	return (name == null ? 0 : name.hashCode());
    }

}
