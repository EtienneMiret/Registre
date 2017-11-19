package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Cette classe représente le scénariste d’une bande-dessinée.
 */
@Entity
@Table(name = "scenaristes")
public class Scénariste extends Named {

    /**
     * Construit un nouveau scénariste portant le nom donné en paramètre. Deux
     * scénaristes différents ne peuvent avoir le même nom.
     *
     * @param nom
     *            nom de ce nouveau scénariste.
     */
    public Scénariste(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Scénariste() {
	super();
    }

}
