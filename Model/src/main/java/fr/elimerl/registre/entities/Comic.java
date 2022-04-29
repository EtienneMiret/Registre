package fr.elimerl.registre.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Record kind for comics.
 */
@Entity
@Table(name = "comics")
public class Comic extends Record {

    /**
     * Cartoonist of this comic.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartoonist")
    private Cartoonist cartoonist;

    /**
     * Script writer of this comic.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_writer")
    private ScriptWriter scriptWriter;

    /**
     * Register a new comic.
     * @param title
     * 		title for this comic.
     * @param creator
     * 		user who registered this comic.
     */
    public Comic(final String title, final User creator) {
	super(title, creator);
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Comic() {
	super();
    }

    @Override
    public List<String> getOtherFields() {
	final List<String> result = super.getOtherFields();
	result.add("BD");
	result.add("bande-déssinée");
	if (cartoonist != null) {
	    result.add(cartoonist.getName());
	}
	if (scriptWriter != null) {
	    result.add(scriptWriter.getName());
	}
	return result;
    }

    @Override
    public String getType () {
      return "comic";
    }

  /**
     * Returns the cartoonist of this comic.
     * @return the cartoonist of this comic, or {@code null} if unknown.
     * @see #cartoonist
     */
    public Cartoonist getCartoonist() {
        return cartoonist;
    }

    /**
     * Set the cartoonist of this comic.
     *
     * @param cartoonist
     * 		cartoonist of this comic, or {@code null} if unknown.
     * @see #cartoonist
     */
    public void setCartoonist(final Cartoonist cartoonist) {
        this.cartoonist = cartoonist;
    }

    /**
     * Returns the script writer of this comic.
     *
     * @return the script writer of this comic, or {@code null} if unknown.
     * @see #scriptWriter
     */
    public ScriptWriter getScriptWriter() {
        return scriptWriter;
    }

    /**
     * Set the script writer of this comic.
     *
     * @param scriptWriter
     * 		script writer of this comic, or {@code null} if unknown.
     * @see #scriptWriter
     */
    public void setScriptWriter(final ScriptWriter scriptWriter) {
        this.scriptWriter = scriptWriter;
    }

}
