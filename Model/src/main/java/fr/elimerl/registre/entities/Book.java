package fr.elimerl.registre.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Record kind for books.
 */
@Entity
@Table(name = "books")
public class Book extends Record {

    /** This book’s author. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur")
    private Author author;

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Book() {
	super();
    }

    /**
     * Crée une nouvelle référence de livre.
     *
     * @param titre
     *            titre de ce livre.
     * @param créateur
     *            utilisateur qui a référencé ce livre.
     */
    /**
     * Create a new book record.
     * @param title
     * 		this book’s title.
     * @param creator
     * 		user who registered this book.
     */
    public Book(final String title, final User creator) {
	super(title, creator);
    }

    @Override
    public List<String> getOtherFields() {
	final List<String> result = super.getOtherFields();
	result.add("livre");
	if (author != null) {
	    result.add(author.getName());
	}
	return result;
    }

    /**
     * Returns this book’s author.
     *
     * @return this book’s author.
     */
    public Author getAuthor() {
	return author;
    }

    /**
     * Set this book’s author.
     *
     * @param author
     * 		this book’s author.
     */
    public void setAuthor(final Author author) {
	this.author = author;
    }

}
