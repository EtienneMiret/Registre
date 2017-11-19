package fr.elimerl.registre.search.tokens;

/**
 * This token models an opening or closing bracket. Hence, there are only two
 * instances of this class.
 */
public final class Bracket extends Signe {

    /** An opening bracket. */
    public static final Bracket OPENING = new Bracket("(");

    /** A closing bracket. */
    public static final Bracket CLOSING = new Bracket(")");

    /**
     * Private constructor, since there are only two instaces.
     *
     * @param representation
     *          character string representing the bracket.
     */
    private Bracket(final String representation) {
	super(representation);
    }

}
