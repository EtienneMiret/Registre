package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Un auteur de livre.
 */
@Entity
@Table(name = "auteurs")
public class Auteur extends Nommé {

    /**
     * Constructeur sans argument, requis par Hibernate.
     */
    protected Auteur() {
	super();
    }

    /**
     * Crée un nouvel auteur. Aucun auteur avec ce nom ne doit exister en base.
     *
     * @param nom
     *            nom de ce nouvel auteur.
     */
    public Auteur(final String nom) {
	super(nom);
    }

}
