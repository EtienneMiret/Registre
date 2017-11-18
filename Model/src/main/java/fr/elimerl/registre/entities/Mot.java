package fr.elimerl.registre.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Représente un mot indexée par le moteur de recherche de l’application.
 */
@Entity
@Table(name = "dictionaire")
public class Mot {

    /** Identifiant de ce mot dans la base de donnée. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Le mot (en tant que {@code String}) représenté par ce {@code Mot}. */
    @Column(name = "mot")
    private String valeur;

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Mot() {
    }

    /**
     * Crée un nouveau mot.
     *
     * @param valeur
     *            mot (en tant que {@code String}) représenté par ce
     *            {@code Mot}.
     */
    public Mot(final String valeur) {
	this.valeur = valeur;
    }

    /**
     * Renvoie l’identifiant de ce mot.
     *
     * @return l’identifiant de ce mot.
     */
    public Long getId() {
	return id;
    }

    /**
     * Renvoie le mot (en tant que {@code String}) représenté par ce
     * {@code Mot}.
     *
     * @return le mot (en tant que {@code String}) représenté par ce
     *         {@code Mot}.
     */
    public String getValeur() {
	return valeur;
    }

    @Override
    public int hashCode() {
	return (valeur == null ? 0 : valeur.hashCode());
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean result;
	if (this == objet) {
	    result = true;
	} else if (objet instanceof Mot) {
	    final Mot autreMot = (Mot) objet;
	    if (this.valeur == null) {
		result = (autreMot.valeur == null);
	    } else {
		result = this.valeur.equals(autreMot.valeur);
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public String toString() {
	return valeur;
    }

}
