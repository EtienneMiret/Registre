package fr.elimerl.registre.search.tokens;

import java.util.regex.Pattern;

/**
 * Root class of the query language’s token’s class hierarchy.
 */
public abstract class Token {

    /** This token as a string. */
    private final String value;

    /**
     * The pattern that allows detection of this token in a string.
     */
    private final Pattern pattern;

    /**
     * Create a new sign from its string value.
     *
     * @param value
     *          this new pattern’s value as a string.
     */
    public Token(final String value) {
	this.value = value;
	this.pattern = Pattern.compile("\\G\\s*" + Pattern.quote(value),
		Pattern.CASE_INSENSITIVE);
    }

    /**
     * Returns the pattern that allows detection of this token in a string.
     *
     * @return the pattern that allows detection of this token in a string.
     */
    public Pattern getPattern() {
	return pattern;
    }

    @Override
    public String toString() {
	return value;
    }

}
