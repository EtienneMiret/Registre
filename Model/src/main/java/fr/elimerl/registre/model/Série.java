package fr.elimerl.registre.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Une série de références. Par exemple « Luky Luke », ou « James Bond ».
 */
@Entity
@Table(name = "series")
public class Série extends Nommé {

    /**
     * Crée une nouvelle série avec le nom donné en argument. Aucune série avec
     * ce nom ne doit exister en base.
     *
     * @param nom nom de cette nouvelle série.
     */
    public Série(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    public Série() {
	super();
    }

}
