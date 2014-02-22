package fr.elimerl.registre.modèle.entités;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Un acteur dans un film.
 */
@Entity
@Table(name = "acteurs")
public class Acteur extends Nommé {

    /**
     * Référence un nouvel acteur, portant le nom donné en paramètre. Aucun
     * acteur avec ce nom ne doit exister en base.
     *
     * @param nom
     *            nom du nouvel acteur.
     */
    public Acteur(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans argument, requis par Hibernate.
     */
    protected Acteur() {
	super();
    }

}
