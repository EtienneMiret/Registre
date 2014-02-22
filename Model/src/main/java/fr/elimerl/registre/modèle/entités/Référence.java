package fr.elimerl.registre.modèle.entités;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Une {@code Référence} est une entrée dans l’index de l’application. Elle
 * associe un {@link Mot} à une {@link Fiche}, en précisant dans quel
 * {@link Champ} de la fiche il se trouve.
 */
@Entity
@Table(name = "`index`")
@NamedQuery(
    name = "désindexerFiche",
    query = "delete from Référence r where r.fiche = :fiche"
)
public class Référence {

    /** Un nombre premier. */
    private static final int PREMIER = 31;

    /**
     * Un {@code Champ} est un champ d’une {@link Fiche}.
     */
    public enum Champ {

	/**
	 * Correspond au {@link Fiche#getTitre() titre} d’une {@code Fiche}.
	 */
	TITRE,

	/**
	 * Correspond au {@link Fiche#getCommentaire() commentaire} d’une
	 * {@code Fiche}.
	 */
	COMMENTAIRE,

	/**
	 * Correspond aux autres champ d’une {@code Fiche}.
	 */
	AUTRE

    }

    /** Identifiant de cet référence en base. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Le {@code Mot} référencé. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mot")
    private Mot mot;

    /** Le {@code Champ} dans lequel se trouve le mot référencé. */
    @Enumerated(EnumType.STRING)
    @Column(name = "champ")
    private Champ champ;

    /** La {@code Fiche} dans laquelle se trouve cette référence. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiche")
    private Fiche fiche;

    /**
     * Constructeur sans argument, requis par Hibernate.
     */
    protected Référence() {
    }

    /**
     * Créée une nouvelle référence.
     *
     * @param mot
     *            le mot référencé.
     * @param champ
     *            le champ dans lequel se trouve le mot référencé.
     * @param fiche
     *            la fiche dans laquelle se trouve le mot référencé.
     */
    public Référence(final Mot mot, final Champ champ, final Fiche fiche) {
	this.mot = mot;
	this.champ = champ;
	this.fiche = fiche;
    }

    /**
     * Renvoie l’identifiant en base de donnée de cette référence.
     *
     * @return l’identifiant en base de donnée de cette référence.
     */
    public Long getId() {
	return id;
    }

    /**
     * Renvoie le mot référencé.
     *
     * @return le mot référencé.
     */
    public Mot getMot() {
	return mot;
    }

    /**
     * Renvoie le champ dans lequel se trouve le mot référencé.
     *
     * @return le champ dans lequel se trouve le mot référencé.
     */
    public Champ getChamp() {
	return champ;
    }

    /**
     * Renvoie la fiche dans laquelle se trouve le mot référencé.
     *
     * @return la fiche dans laquelle se trouve le mot référencé.
     */
    public Fiche getFiche() {
	return fiche;
    }

    @Override
    public String toString() {
	return mot + " -> " + fiche + " (" + champ + ")";
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean résultat;
	if (this == objet) {
	    résultat = true;
	} else if (objet instanceof Référence) {
	    final Référence autreRéf = (Référence) objet;
	    résultat = égauxOuNuls(this.mot, autreRéf.mot)
		    && this.champ == autreRéf.champ
		    && égauxOuNuls(this.fiche, autreRéf.fiche);
	} else {
	    résultat = false;
	}
	return résultat;
    }

    @Override
    public int hashCode() {
	int résultat;
	résultat = (mot == null ? 0 : mot.hashCode());
	résultat = résultat * PREMIER + (champ == null ? 0 : champ.hashCode());
	résultat = résultat * PREMIER + (fiche == null ? 0 : fiche.hashCode());
	return résultat;
    }

    /**
     * Compare deux objets et indique s’ils sont égaux. Si les deux objets sont
     * {@code null}, considère qu’ils sont égaux.
     *
     * @param a
     *            1<sup>er</sup> objet à comparer.
     * @param b
     *            2<sup>ème</sup> objet à comparer.
     * @return {@code true} si les deux objets sont égaux ou {@code null},
     *         {@code false} sinon.
     */
    private static boolean égauxOuNuls(final Object a, final Object b) {
	final boolean résultat;
	if (a == null) {
	    résultat = (b == null);
	} else {
	    résultat = a.equals(b);
	}
	return résultat;
    }

}
