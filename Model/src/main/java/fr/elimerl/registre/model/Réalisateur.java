package fr.elimerl.registre.model;

/**
 * Cette classe représente le réalisateur d’un film.
 */
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
    public Réalisateur() {
	this(null);
    }

}
