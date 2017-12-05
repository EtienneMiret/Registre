package fr.elimerl.registre.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Record kind for movies.
 */
@Entity
@Table(name = "movies")
public class Movie extends Record {

    /**
     * Physical support a movie is recorded on.
     */
    public static enum Support {

	/** A tape. */
	K7("Cassette"),
	/** A DVD. */
	DVD("DVD"),
	/** Dematerialised. */
	DEM("Dématérialisé"),
	/** A Blu-Ray. */
	BRD("Blu-Ray");

	/** Name of this support to display in the UI. */
	private final String name;

	/**
	 * Set the name of this support.
	 *
	 * @param name
	 * 		name of this support to display in the UI.
	 */
	private Support(final String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return name;
	}

    }

    /** Physical support this movie is recorded on. */
    @Enumerated(EnumType.STRING)
    @Column(name = "support")
    private Support support;

    /** This movie’s director. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director")
    private Director director;

    /** The actors who played in this movie. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
	name = "plays_in",
	joinColumns = @JoinColumn(name = "movie"),
	inverseJoinColumns = @JoinColumn(name = "actor")
    )
    private Set<Actor> actors;

    /** This movie’s soundtrack composer. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "composer")
    private Composer composer;

    /**
     * Create a new movie record.
     * @param title
     * 		this movie title.
     * @param creator
     * 		user who registered this movie.
     * @param support
     * 		physical support this movie is on.
     */
    public Movie(final String title, final User creator,
	    final Support support) {
	super(title, creator);
	this.support = support;
	this.actors = new HashSet<Actor>();
    }

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Movie() {
	super();
    }

    @Override
    public List<String> getOtherFields() {
	final List<String> result = super.getOtherFields();
	result.add("film");
	if (support != null) {
	    result.add(support.toString());
	}
	if (director != null) {
	    result.add(director.getName());
	}
	if (actors != null) {
	    for (final Actor acteur : actors) {
		result.add(acteur.getName());
	    }
	}
	if (composer != null) {
	    result.add(composer.getName());
	}
	return result;
    }

    @Override
    public String getType () {
      return "movie";
    }

  /**
     * Returns the physical support this movie is recorded on.
     *
     * @return the physical support this movie is recorded on.
     * @see #support
     */
    public Support getSupport() {
	return support;
    }

    /**
     * Set the physical support this movie is recorded on.
     *
     * @param support
     * 		physical support this movie is recorded on.
     * @see #support
     */
    public void setSupport(final Support support) {
	this.support = support;
    }

    /**
     * Returns this movie’s director.
     *
     * @return this movie’s director.
     * @see #director
     */
    public Director getDirector() {
	return director;
    }

    /**
     * Set this movie’s director.
     *
     * @param director
     * 		this movie’s director.
     * @see #director
     */
    public void setDirector(final Director director) {
	this.director = director;
    }

    /**
     * Returns the set of actors who played in this movie.
     *
     * @return the set of actors who played in this movie.
     * @see #actors
     */
    public Set<Actor> getActors() {
	return actors;
    }

    /**
     * Renvoie le compositeur de la musique de ce film.
     * Returns this movie’s soundtrack composer.
     *
     * @return this movie’s soundtrack composer.
     * @see #composer
     */
    public Composer getComposer() {
	return composer;
    }

    /**
     * Set this movie’s soundtrack composer.
     *
     * @param composer
     * 		this movie’s soundtrack composer.
     * @see #composer
     */
    public void setComposer(final Composer composer) {
	this.composer = composer;
    }

}
