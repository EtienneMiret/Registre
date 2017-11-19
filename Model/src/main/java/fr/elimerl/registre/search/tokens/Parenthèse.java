package fr.elimerl.registre.search.tokens;

/**
 * Ce signe est une parenthèse ouvrante ou fermante. Il n’y a donc que deux
 * instances de cette classe.
 */
public final class Parenthèse extends Signe {

    /** Une parenthèse ouvrante. */
    public static final Parenthèse OUVRANTE = new Parenthèse("(");

    /** Une parenthèse fermante. */
    public static final Parenthèse FERMANTE = new Parenthèse(")");

    /**
     * Constructeur privée, seules deux instances étant autorisées.
     *
     * @param représentation
     *            chaîne de charactère représentant la parenthèse.
     */
    private Parenthèse(final String représentation) {
	super(représentation);
    }

}
