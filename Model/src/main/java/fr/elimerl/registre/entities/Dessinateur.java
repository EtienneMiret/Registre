package fr.elimerl.registre.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Cette classe représente le dessinateur d’une bande-dessinée.
 */
@Entity
@Table(name = "dessinateurs")
public class Dessinateur extends Nommé {

    /**
     * Référence un nouveau dessinateur. Aucun dessinateur avec ce nom ne doit
     * exister dans la base de données.
     *
     * @param nom
     *            nom du nouveau dessinateur.
     */
    public Dessinateur(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Dessinateur() {
	super();
    }

}
