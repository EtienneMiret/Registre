package fr.elimerl.registre.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * A {@code Reference} is an entry in the application index. It links a
 * {@link Word} to a {@link Record}, and specify in which {@link Field} of the
 * record this word is in.
 */
@Entity
@Table(name = "index_")
@NamedQuery(
    name = "unindexRecord",
    query = "delete from Reference r where r.record = :record"
)
public class Reference {

    /** A prime number. */
    private static final int PRIME = 31;

    /**
     * A kind of field of a {@link Record}.
     */
    public enum Field {

        /**
         * The {@link Record#title title} of a {@code Record}.
         */
	TITLE,

        /**
         * The {@link Record#comment comment} of a {@code Record}.
         */
	COMMENT,

        /**
         * Any other field of a {@code Record}.
         */
	OTHER

    }

    /** Id of this reference in the database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The referenced {@link Word}. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word")
    private Word word;

    /** The {@link Field} where the referenced word is. */
    @Enumerated(EnumType.STRING)
    @Column(name = "field")
    private Field field;

    /** The {@link Record} where the referenced word is. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record")
    private Record record;

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Reference() {
    }

    /**
     * Create a new reference.
     *
     * @param word
     *          the referenced word.
     * @param field
     *          the record field where the referenced word is.
     * @param record
     *          the record where the referenced word is.
     */
    public Reference(final Word word, final Field field, final Record record) {
	this.word = word;
	this.field = field;
	this.record = record;
    }

    /**
     * Returns the database ID of this reference.
     *
     * @return the database ID of this reference.
     */
    public Long getId() {
	return id;
    }

    /**
     * Renvoie le mot référencé.
     *
     * @return le mot référencé.
     */
    /**
     * Returns the referenced word.
     *
     * @return the referenced word.
     */
    public Word getWord() {
	return word;
    }

    /**
     * Returns the record field where the referenced word is.
     *
     * @return the record field where the referenced word is.
     */
    public Field getField() {
	return field;
    }

    /**
     * Returns the record where the referenced word is.
     *
     * @return the record where the referenced word is.
     */
    public Record getRecord() {
	return record;
    }

    @Override
    public String toString() {
	return word + " -> " + record + " (" + field + ")";
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean result;
	if (this == objet) {
	    result = true;
	} else if (objet instanceof Reference) {
	    final Reference otherRef = (Reference) objet;
	    result = equalsOrNull(this.getWord(), otherRef.getWord())
		    && this.getField() == otherRef.getField()
		    && equalsOrNull(this.getRecord(), otherRef.getRecord());
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public int hashCode() {
	int result;
	result = (getWord() == null ? 0 : getWord().hashCode());
	result = result * PRIME + (getField() == null ? 0 : getField().hashCode());
	result = result * PRIME + (getRecord() == null ? 0 : getRecord().hashCode());
	return result;
    }

    /**
     * Compare two objects for equality. If they’re both {@code null}, they‘re
     * considered equals.
     *
     * @param a
     *          1<sup>st</sup> object to compare.
     * @param b
     *          2<sup>nd</sup> object to compare.
     * @return {@code true} if both objects are equals or {@code null},
     *          {@code false} otherwise.
     */
    private static boolean equalsOrNull(final Object a, final Object b) {
	final boolean result;
	if (a == null) {
	    result = (b == null);
	} else {
	    result = a.equals(b);
	}
	return result;
    }

}
