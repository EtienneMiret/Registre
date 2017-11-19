package fr.elimerl.registre.search.tokens;

import java.util.ArrayList;
import java.util.List;

import fr.elimerl.registre.entities.*;
import fr.elimerl.registre.entities.Comic;

/**
 * This token represents a field within which to search for a keyword. Eg the
 * title, comment or author.
 */
public final class Field extends Signe {

    /*
     * For now, we trust this library users not to modify this field. We may
     * need to make it immutable later.
     */
    /**
     * All existing fields. <em>Read-only.</em>
     */
    public static final List<Field> all = new ArrayList<Field>();

    /** A record’s title. */
    public static final Field TITLE =
	    valueOf("title", String.class, Record.class);

    /** A record’s comment. */
    public static final Field COMMENT =
	    valueOf("comment", String.class, Record.class);

    /** The series a record is part of. */
    public static final Field SERIES =
	    valueOf("series", Series.class, Record.class);

    /** A record’s item’s owner. */
    public static final Field OWNER =
	    valueOf("owner", Owner.class, Record.class);

    /** A record’s item’s location. */
    public static final Field LOCATION =
	    valueOf("location", Location.class, Record.class);

    /** A movie’s director. */
    public static final Field DIRECTOR =
	    valueOf("director", Director.class, Movie.class);

    /** An actor playing in a movie. */
    public static final Field ACTOR =
	    valueOf("actor", Actor.class, Movie.class);

    /** A movie soundtrack’s composer. */
    public static final Field COMPOSER =
	    valueOf("composer", Composer.class, Movie.class);

    /** A comic’s cartoonist. */
    public static final Field CARTOONIST =
	    valueOf("cartoonist", Cartoonist.class, Comic.class);

    /** A comic’s script writer. */
    public static final Field SCRIPT_WRITER =
	    valueOf("scriptWriter", ScriptWriter.class, Comic.class);

    /** A book’s author. */
    public static final Field AUTHOR =
	    valueOf("author", Author.class, Book.class);

    /**
     * Create a new kind of field and register it in {@link #all}.
     *
     * @param name
     *          name of the field to create.
     * @param clazz
     *          class of the objects put in the field to create.
     * @param declaringClass
     *          class the field to create belongs to.
     * @return the new field.
     */
    private static Field valueOf(final String name, final Class<?> clazz,
	    final Class<? extends Record> declaringClass) {
	final Field result = new Field(name, clazz, declaringClass);
	all.add(result);
	return result;
    }

    /**
     * Class of the objects put in this field.
     */
    private final Class<?> clazz;

    /**
     * The class this field belongs to. Eg {@link Movie} for director and
     * {@link Comic} for cartoonist.
     */
    private final Class<? extends Record> declaringClass;

    /**
     * This field’s name. It is both the name of this field’s attribute in
     * {@link Record} or one of its subclass and the keyword to put before ‘:’
     * in order to query this field.
     */
    private final String name;

    /**
     * Private constructor, since there is only a predefined number of
     * instances.
     *
     * @param name
     *          name of this field. See {@link #name}.
     * @param clazz
     *          class of objects put in this field. See {@link #clazz}.
     * @param declaringClass
     *          class this field belongs to. See {@link #declaringClass}.
     */
    private Field(final String name, final Class<?> clazz,
	    final Class<? extends Record> declaringClass) {
	super(name + ':');
	this.clazz = clazz;
	this.declaringClass = declaringClass;
	this.name = name;
    }

    /**
     * Returns the class the objects put in this fields belong to.
     *
     * @return the class the objects put in this fields belong to.
     * @see #clazz
     */
    public Class<?> getClazz() {
	return clazz;
    }

    /**
     * Returns the class this field  belongs to.
     *
     * @return the class this field  belongs to.
     * @see #declaringClass
     */
    public Class<? extends Record> getDeclaringClass() {
	return declaringClass;
    }

    /**
     * Returns this field’s name.
     *
     * @return this field’s name.
     * @see #name
     */
    public String getName() {
	return name;
    }

}
