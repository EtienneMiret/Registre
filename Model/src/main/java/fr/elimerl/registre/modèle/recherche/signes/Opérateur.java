package fr.elimerl.registre.modèle.recherche.signes;

/**
 * Ce signe représente un opérateur du langage de requêtes, par exemple le OU
 * booléen.
 */
public final class Opérateur extends Signe {

    /** L’opérateur booléen « ou ». */
    public static final Opérateur OU = new Opérateur("ou");

    /**
     * Constructeur privé, seul un nombre limité d’instances étant autorisées.
     *
     * @param représentation
     *            mot-clé du langage de requête correspondant à cet opérateur.
     */
    private Opérateur(final String représentation) {
	super(représentation);
    }

}
