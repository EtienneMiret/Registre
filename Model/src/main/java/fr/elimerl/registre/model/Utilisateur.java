package fr.elimerl.registre.model;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Un utilisateur de l’application.
 */
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    /**
     * Taille du sel (nombre de caractères de la représentation hexadécimale).
     */
    private static final int TAILLE_SEL = 8;

    /**
     * Encodage de caractère utilisé pour calculer le hash.
     */
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * Générateur de nombres aléatoires utilisé pour générer les sels.
     */
    private static final Random random = new Random();

    /**
     * Loggeur de cette classe.
     */
    private static final Logger logger =
	    LoggerFactory.getLogger(Utilisateur.class);

    /**
     * Identifiant de cet objet dans la base de données.
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Nom de cet utilisateur.
     */
    @Column(name = "nom")
    private String nom;

    /**
     * Sel utilisé pour stocker le mot de passe. Doit faire {@value #TAILLE_SEL}
     * caractères de long.
     */
    @Column(name = "sel")
    private String sel;

    /**
     * Hash MD5 du mot de passe salé. Est constitué du {@link #sel}, du
     * caractère ‘:’ et du vrai mot de passe, concaténés et hachés.
     */
    @Column(name = "mdp")
    private String hashMdp;

    /**
     * Constructure sans arguments, utilisé par Hibernate.
     */
    public Utilisateur() {
	super();
    }

    /**
     * Construit un nouvel utilisateur, avec le nom et le mot de passe donnés en
     * paramètres.
     *
     * @param nom
     *            nom de cet utilisateur.
     * @param mdp
     *            mot de passe initial de cet utilisateur.
     */
    public Utilisateur(final String nom, final String mdp) {
	super();
	this.nom = nom;
	sel = selAléatoire();
	hashMdp = hacherMdp(mdp);
	logger.debug("Création de l’utilisateur : {}", this);
    }

    /**
     * Change le mot de passe de cet utilisateur.
     *
     * @param mdp
     *            nouveau mot de passe de cet utilisateur.
     */
    public void définirMdp(final String mdp) {
	sel = selAléatoire();
	hashMdp = hacherMdp(mdp);
	logger.debug("Mot de passe de {} changé.", this);
    }

    /**
     * Vérifie que le mot de passe donné en argument est correct. Le mot de
     * passe est d’abord normalisé, ce qui permet de prendre en charge les
     * accents de manière fiable.
     *
     * @param mdp
     *            mot de passe à vérifier.
     * @return {@code true} si {@code mdp} est bien le mot de passe de cet
     *         utilisateur, {@code false} sinon.
     */
    public boolean vérifierMdp(final String mdp) {
	return hashMdp.equals(hacherMdp(mdp));
    }

    /**
     * Renvoie l’identifiant de cet utilisateur en base de donnée.
     *
     * @return l’identifiant de cet utilisateur en base de donnée.
     */
    public Long getId() {
	return id;
    }

    /**
     * Renvoie le nom de cet utilisateur.
     *
     * @return le nom de cet utilisateur.
     */
    public String getNom() {
	return nom;
    }

    @Override
    public String toString() {
	return nom;
    }

    @Override
    public boolean equals(final Object autre) {
	if (this == autre) {
	    return true;
	} else if (autre == null) {
	    return false;
	} else if (autre instanceof Utilisateur) {
	    final Utilisateur autreUtilisateur = (Utilisateur) autre;
	    if (this.nom == null) {
		return (autreUtilisateur.nom == null);
	    } else {
		return this.nom.equals(autreUtilisateur.nom);
	    }
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	return (nom == null ? 0 : nom.hashCode());
    }

    /**
     * Génère un sel aléatoire, sous forme de {@value #TAILLE_SEL} caractères
     * hexadécimaux.
     *
     * @return un sel aléatoire.
     * @see #sel
     */
    private static String selAléatoire() {
	final int sel;
	synchronized (random) {
	    sel = random.nextInt();
	}
	final String baseSel = Integer.toHexString(sel);
	final StringBuilder selBuilder = new StringBuilder(TAILLE_SEL);
	for (int i = baseSel.length(); i < TAILLE_SEL; i++) {
	    selBuilder.append('0');
	}
	selBuilder.append(baseSel);
	logger.debug("Sel généré : {}", selBuilder);
	return selBuilder.toString();
    }

    /**
     * Hache un mot de passe avec le sel de cet utilisateur. Renvoie une
     * représentation hexadécimale du hash calculé.
     *
     * @param mdp
     *            mot de passe à hacher.
     * @return le hash en hexadécimal du mot de passe donné.
     * @see #hashMdp
     */
    private String hacherMdp(final String mdp) {
	/* Saler le mot de passe. */
	final String nfkcMdp = Normalizer.normalize(mdp, Form.NFKC);
	final StringBuilder saléBuilder =
		new StringBuilder(sel.length() + 1 + nfkcMdp.length());
	saléBuilder.append(sel);
	saléBuilder.append(':');
	saléBuilder.append(nfkcMdp);
	final String salé = saléBuilder.toString();

	/* Hacher le mot de passe. */
	final MessageDigest md5;
	try {
	    md5 = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException("Pas de MD5.", e);
	}
	md5.update(salé.getBytes(UTF_8));
	final byte[] hash = md5.digest();

	/* Convertir le hash en héxadécimal. */
	final StringBuilder hexBuilder = new StringBuilder(hash.length * 2);
	for (final byte b : hash) {
	    hexBuilder.append(String.format("%02x", Byte.valueOf(b)));
	}
	logger.debug("Hash calculé : {}", hexBuilder);
	return hexBuilder.toString();
    }

}
