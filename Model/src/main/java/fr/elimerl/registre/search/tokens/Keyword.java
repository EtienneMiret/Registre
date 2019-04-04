package fr.elimerl.registre.search.tokens;

import java.util.regex.Pattern;

/**
 * A keyword is a word within a query that the user wishes to find in its
 * results.
 */
public class Keyword extends Token {

    /** Pattern for all keywords. */
    public static final Pattern PATTERN =
	    Pattern.compile("\\G\\s*([\\p{L}\\d]+)");

    /** This keyword’s word, as a string. */
    private final String value;

    /**
     * Build a keyword matching the given string.
     *
     * @param value
     *          the string of this keyword. Must not contain any spaces or
     *          punctuation.
     */
    public Keyword(final String value) {
	super(value);
	this.value = value;
    }

    /**
     * Returns the string of this keyword.
     *
     * @return the string of this keyword.
     */
    public String getValue() {
	return value;
    }

    @Override
    public final boolean equals(final Object other) {
	final boolean result;
	if (this == other) {
	    result = true;
	} else if (other == null) {
	    result = false;
	} else if (other instanceof Keyword) {
	    final Keyword keyword = (Keyword) other;
	    if (value == null) {
		result = (keyword.value == null);
	    } else {
		result = value.equals(keyword.value);
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public final int hashCode() {
	return (value == null ? 0 : value.hashCode());
    }

}
