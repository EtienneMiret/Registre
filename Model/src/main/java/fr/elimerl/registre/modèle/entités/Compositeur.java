package fr.elimerl.registre.modèle.entités;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Cette classe représente le compositeur de la musique d’un film.
 */
@Entity
@Table(name = "compositeurs")
public class Compositeur extends Nommé {

    /**
     * Référence un nouveau compositeur portant le nom donné en paramètre.
     *
     * @param nom
     *            nom du nouveau compositeur.
     */
    public Compositeur(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Compositeur() {
	super();
    }

}
