package fr.elimerl.registre.model;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une session ouverte par un utilisateur.
 */
@Entity
@Table(name = "sessions")
public class Session {

    /** Taille des clefs en octets. */
    private static final int TAILLE_CLEF = 15;

    /**
     * Générateur de nombre aléatoire utilisé pour générer les clefs.
     */
    private static final Random random = new SecureRandom();

    /**
     * Loggeur de cette classe.
     */
    private static final Logger logger =
	    LoggerFactory.getLogger(Session.class);

    /**
     * Clef utilisée pour identifier la session. Stockée dans un cookie du
     * navigateur.
     */
    @Id
    @Column(name = "clef")
    private String clef;

    /** Utilisateur qui a créé cette session. */
    @Column(name = "utilisateur")
    private Utilisateur utilisateur;

    /**
     * Date d’expiration de la session. À cette date, la session n’est plus
     * valide.
     *
     * @see #estValide()
     */
    @Column(name = "expiration")
    private Date expiration;

    /**
     * Construit une nouvelle session, de la durée précisée et pour
     * l’utilisateur indiqué.
     *
     * @param utilisateur
     *            utilisateur associé à cette session.
     * @param durée
     *            durée de cette session en millisecondes.
     */
    public Session(final Utilisateur utilisateur, final long durée) {
	if (utilisateur != null) {
	    /* Champ clef. */
	    final byte[] octets = new byte[TAILLE_CLEF];
	    random.nextBytes(octets);
	    clef = DatatypeConverter.printBase64Binary(octets);

	    /* Champ utilisateur. */
	    this.utilisateur = utilisateur;

	    /* Champ expiration. */
	    expiration = new Date();
	    final long now = expiration.getTime();
	    expiration.setTime(now + durée);

	    logger.debug("Session {} créée pour {}.", clef,
		    utilisateur.getNom());
	}
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    public Session() {
	this(null, 0L);
    }

    /**
     * Renvoie l’utilisateur de cette session.
     *
     * @return l’utilisateur de cette session.
     */
    public Utilisateur getUtilisateur() {
	return utilisateur;
    }

    /**
     * Indique si la session est encore valide.
     *
     * @return {@code true} si la session n’a pas expirée, {@code false} sinon.
     * @see #expiration
     */
    public boolean estValide() {
	final Date now = new Date();
	return now.before(expiration);
    }

}
