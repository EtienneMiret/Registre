package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Cette classe représente le réalisateur d’un film.
 */
@Entity
@Table(name = "realisateurs")
public class Réalisateur extends Nommé {

    /**
     * Référence un nouveau réalisateur portant le nom donné en paramètre.
     *
     * @param nom
     *            nom du nouveau réalisateur.
     */
    public Réalisateur(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Réalisateur() {
	super();
    }

}
