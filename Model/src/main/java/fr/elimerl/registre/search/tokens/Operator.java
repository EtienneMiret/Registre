package fr.elimerl.registre.search.tokens;

import java.util.ArrayList;
import java.util.List;

/**
 * This token models an operator of the query language. For example the boolean
 * OR.
 */
public final class Operator extends Token {

    /** The boolean “OR”. */
    public static final Operator OR = new Operator("or");

    /**
     * Returns all existing operators.
     *
     * @return all existing operators.
     */
    public static List<Operator> all() {
	final List<Operator> result = new ArrayList<Operator>(1);
	result.add(OR);
	return result;
    }

    /**
     * Private constructor, since only a limited number of instances is allowed.
     *
     * @param representation
     *          keyword of the query language matching this operator.
     */
    private Operator(final String representation) {
	super(representation);
    }

}
