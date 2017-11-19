package fr.elimerl.registre.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Word indexed by the application’s search engine.
 */
@Entity
@Table(name = "dictionary")
public class Word {

    /** Id of this word in the database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** This word as a {@link String}. */
    @Column(name = "mot")
    private String value;

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Word() {
    }

    /**
     * Create a new word.
     *
     * @param value
     * 		this word as a {@link String}.
     */
    public Word(final String value) {
	this.value = value;
    }

    /**
     * Returns this word’s id.
     *
     * @return this word’s id.
     */
    public Long getId() {
	return id;
    }

    /**
     * Returns this word as a {@link String}.
     *
     * @return this word as a {@link String}.
     */
    public String getValue() {
	return value;
    }

    @Override
    public int hashCode() {
	return (value == null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean result;
	if (this == objet) {
	    result = true;
	} else if (objet instanceof Word) {
	    final Word otherWord = (Word) objet;
	    if (this.value == null) {
		result = (otherWord.value == null);
	    } else {
		result = this.value.equals(otherWord.value);
	    }
	} else {
	    result = false;
	}
	return result;
    }

    @Override
    public String toString() {
	return value;
    }

}
