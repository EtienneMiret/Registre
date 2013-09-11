package fr.elimerl.registre.modèle.recherche.signes;

/**
 * Ce signe est une parenthèse ouvrante ou fermante. Il n’y a donc que deux
 * instances de cette classe.
 */
public final class Parenthèse extends Signe {

    /** Une parenthèse ouvrante. */
    public static final Parenthèse OUVRANTE = new Parenthèse();

    /** Une parenthèse fermante. */
    public static final Parenthèse FERMANTE = new Parenthèse();

    /** Constructeur privé, seules deux instances étant autorisée. */
    private Parenthèse() {
	super();
    }

}
