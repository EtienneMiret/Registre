package fr.elimerl.registre.entities;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A user’s session.
 */
@Entity
@Table(name = "sessions")
public class Session {

    /** Key size in bytes. */
    private static final int KEY_SIZE = 15;

    /**
     * Random number generator used to generate keys.
     */
    private static final Random random = new SecureRandom();

    /**
     * This class’ logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    /**
     * Key used to identify the session. Stored in a browser cookie.
     */
    @Id
    @Column(name = "key")
    private String key;

    /** User who created this session. */
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    /**
     * Expiration date for this session. At this date, this session is no longer
     * valid.
     *
     * @see #estValide()
     */
    @Column(name = "expiration")
    private Date expiration;

    /**
     * Create a new session for the specified user and with the specified
     * duration.
     *
     * @param user
     *          this session’s user.
     * @param duration
     *          this session validity duration, in milliseconds.
     */
    public Session(final User user, final long duration) {
	/* Key field. */
	final byte[] octets = new byte[KEY_SIZE];
	synchronized (random) {
	    random.nextBytes(octets);
	}
	key = DatatypeConverter.printBase64Binary(octets);

	/* User field. */
	this.user = user;

	/* Expiration field. */
	expiration = new Date();
	final long now = expiration.getTime();
	expiration.setTime(now + duration);

	logger.debug("Session {} created for {}.", key, user);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Session() {
    }

    /**
     * Returns this session’s user.
     *
     * @return this session’s user.
     */
    public User getUser() {
	return user;
    }

    /**
     * Returns this sessions id (its key).
     *
     * @return this sessions id (its key).
     */
    public String getKey() {
	return key;
    }

    /**
     * Returns this session’s expiration date.
     *
     * @return this session’s expiration date.
     */
    public Date getExpiration() {
	return expiration;
    }

    /**
     * Tell whether this session is still valid.
     *
     * @return {@code true} if this session hasn’t expired yet, {@code false}
     *          otherwise.
     * @see #expiration
     */
    public boolean estValide() {
	final Date now = new Date();
	return now.before(expiration);
    }

    @Override
    public String toString() {
	return key + ":" + user;
    }

    @Override
    public boolean equals(final Object other) {
	if (this == other) {
	    return true;
	} else if (other == null) {
	    return false;
	} else if (other instanceof Session) {
	    final Session otherSession = (Session) other;
	    if (this.key == null) {
		return (otherSession.key == null);
	    } else {
		return this.key.equals(otherSession.key);
	    }
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	return (key == null ? 0 : key.hashCode());
    }

}
