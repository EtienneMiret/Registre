package fr.elimerl.registre.search.signes;

import java.util.ArrayList;
import java.util.List;

/**
 * Ce signe représente un opérateur du langage de requêtes, par exemple le OU
 * booléen.
 */
public final class Opérateur extends Signe {

    /** L’opérateur booléen « ou ». */
    public static final Opérateur OU = new Opérateur("ou");

    /**
     * Renvoie la liste de tous les opérateurs existants.
     *
     * @return la liste de tous les opérateurs existants.
     */
    public static List<Opérateur> tous() {
	final List<Opérateur> résultat = new ArrayList<Opérateur>(1);
	résultat.add(OU);
	return résultat;
    }

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
