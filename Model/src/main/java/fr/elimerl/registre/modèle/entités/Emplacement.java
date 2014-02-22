package fr.elimerl.registre.modèle.entités;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Un emplacement où sont rangées des références. Par exemple
 * « Chambre d’Etienne » ou « Appartement de Claire ».
 */
@Entity
@Table(name = "emplacements")
public class Emplacement extends Nommé {

    /**
     * Crée un nouvel emplacement avec le nom donné en paramètre. Aucun
     * emplacement avec ce nom ne doit exister dans la base de données.
     *
     * @param nom
     *            nom de ce nouvel emplacement.
     */
    public Emplacement(final String nom) {
	super(nom);
    }

    /**
     * Constructeur sans arguments requis par Hibernate.
     */
    protected Emplacement() {
	super();
    }

}
