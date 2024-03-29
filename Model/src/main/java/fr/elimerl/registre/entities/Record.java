package fr.elimerl.registre.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A records model an object registered in the application, eg a movie.
 */
@Entity
@Table(name = "records")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Record {

    /** This class’ logger. */
    private static final Logger logger = LoggerFactory.getLogger(Record.class);

    /**
     * Id of this record in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Title of the registered object.
     */
    @Column(name = "title")
    private String title;

    /**
     * Series the registered object is part of. May be {@code null}.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "series")
    private Series series;

    /**
     * Number of this record within its series.
     */
    @Column(name = "number")
    private Integer number;

    /**
     * Comment written by the users about this record. Can be very long.
     */
    @Column(name = "comment")
    private String comment;

    /**
     * Path of the picture linked with this record. Can be {@code null}.
     */
    @Column(name = "picture")
    private String picture;

    /**
     * Owner of the registered object. Can be {@code null}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private Owner owner;

    /**
     * Location where the registered object is kept. Can be {@code null}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private Location location;

    /** Is this record about an action story? */
    @Column(name = "action_style")
    private Boolean actionStyle;

    /** Is this record about a documentary? */
    @Column(name = "documentary_style")
    private Boolean documentaryStyle;

    /** Is this record about a fantasy story? */
    @Column(name = "fantasy_style")
    private Boolean fantasyStyle;

    /** Is this record about a war story? */
    @Column(name = "war_style")
    private Boolean warStyle;

    /** Is this record about a true story? */
    @Column(name = "true_story_style")
    private Boolean trueStoryStyle;

    /** Is this record about a story with a historical background? */
    @Column(name = "historical_style")
    private Boolean historicalStyle;

    /** Is this record about a funny story? */
    @Column(name = "humor_style")
    private Boolean humorStyle;

    /** Is this record about a detective story? */
    @Column(name = "detective_style")
    private Boolean detectiveStyle;

    /** Is this record about a romantic story? */
    @Column(name = "romantic_style")
    private Boolean romanticStyle;

    /** Is this record about a science-fiction story? */
    @Column(name = "sf_style")
    private Boolean sfStyle;

    /** Is this record alive (as opposed to deleted)? */
    @Column(name = "alive")
    private boolean alive;

    /**
     * User who registered this record.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator", updatable = false)
    private User creator;

    /**
     * Creation date of this record.
     */
    @Column(name = "creation", updatable = false)
    private Date creation;

    /**
     * Last user who modified this record.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modifier")
    private User lastModifier;

    /**
     * Last modification date of this record.
     */
    @Column(name = "last_modification")
    private Date lastModification;

    /**
     * No-args constructor. Required by Hibernate.
     */
    protected Record() {
    }

    /**
     * Create a new record.
     *
     * @param title
     * 		title of this new record.
     * @param creator
     * 		user who is registering this new record.
     */
    public Record(final String title, final User creator) {
	final Date now = new Date();
	this.title = title;
	this.creator = creator;
	this.creation = now;
	this.lastModifier = creator;
	this.lastModification = now;
        this.alive = true;
	logger.debug("{} creates a record for “{}”.", creator, title);
    }

    /**
     * Notify that the specified user just modified this record. Updates the
     * last editor and last edition fields.
     *
     * @param user
     * 		user updating this record.
     * @see #lastModifier
     * @see #lastModification
     */
    public void toucher(final User user) {
	this.lastModifier = user;
        this.lastModification = new Date();
    }

    /**
     * Returns the content of all fields of this record, except the comment and
     * the title. Used by the indexation service.
     *
     * @return the content of all fields of this record, except the comment and
     * 		the title.
     */
    public List<String> getOtherFields() {
	final List<String> result = new ArrayList<String>();
	if (series != null) {
	    result.add(series.getName());
	}
        if (number != null) {
            result.add(number.toString());
        }
	if (owner != null) {
	    result.add(owner.getName());
	}
	if (location != null) {
	    result.add(location.getName());
	}
	if (creator != null) {
	    result.add(creator.getName());
	}
	if (lastModifier != null) {
	    result.add(lastModifier.getName());
	}
	if (actionStyle != null && actionStyle.booleanValue()) {
	    result.add("action");
	}
	if (fantasyStyle != null && fantasyStyle.booleanValue()) {
	    result.add("fantastique");
	}
	if (trueStoryStyle != null && trueStoryStyle.booleanValue()) {
	    result.add("histoire vraie");
	}
	if (historicalStyle != null && historicalStyle.booleanValue()) {
	    result.add("historique");
	}
	if (humorStyle != null && humorStyle.booleanValue()) {
	    result.add("humour");
	    result.add("drôle");
	}
	if (detectiveStyle != null && detectiveStyle.booleanValue()) {
	    result.add("policier");
	}
	if (romanticStyle != null && romanticStyle.booleanValue()) {
	    result.add("romantique");
	}
	if (sfStyle != null && sfStyle.booleanValue()) {
	    result.add("SF");
	    result.add("science-fiction");
	}
	return result;
    }

    /**
     * Returns the kind of item this record represents. Eg: "book", "movie"...
     *
     * @return the kind of item this record represents.
     */
    public abstract String getType ();

    /**
     * Returns this record’s id.
     *
     * @return this record’s id.
     * @see #id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns whether this record is deleted.
     * @return {@code false} if this record is deleted,
     * {@code true} otherwise.
     * @see #alive
     */
    public boolean isAlive () {
        return alive;
    }

    /**
     * Revives this record.
     *
     * @see #alive
     */
    public void revive() {
        this.alive = true;
    }

    /**
     * Deletes this record.
     *
     * @see #alive
     */
    public void delete() {
        this.alive = false;
    }

    /**
     * Returns this record’s title.
     *
     * @return this record’s title.
     * @see #title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set this record’s title.
     *
     * @param title
     * 		this record’s new title.
     * @see #title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the series this records belongs to.
     *
     * @return the series this records belongs to.
     * @see #series
     */
    public Series getSeries() {
        return series;
    }

    /**
     * Set the series this records belongs to.
     *
     * @param series
     * 		series this records belongs to.
     * @see #series
     */
    public void setSeries(final Series series) {
        this.series = series;
    }

    /**
     * Returns the number of this record within its series.
     * @return the number of this record within its series, or {@code null} if
     * 		unknown.
     * @see #number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Set the number of this record within its series.
     *
     * @param number
     * 		number of this record within its series, or {@code null} if
     * 		unknown.
     * @see #number
     */
    public void setNumber(final Integer number) {
        this.number = number;
    }

    /**
     * Returns the comment made by users on this record.
     *
     * @return the comment made by users on this record.
     * @see #comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment made by users on this record.
     *
     * @param comment
     * 		comment made by users on this record.
     * @see #comment
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Returns the path of this record’s picture.
     *
     * @return the path of this record’s picture. May be {@code null}.
     * @see #picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Set the path of this record’s picture.
     *
     * @param picture
     * 		path of this record’s picture.
     * @see #picture
     */
    public void setPicture(final String picture) {
        this.picture = picture;
    }

    /**
     * Returns the owner of the item this record is about.
     *
     * @return the owner of the item this record is about.
     * @see #owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Set the owner of the item this record is about.
     *
     * @param owner
     * 		owner of the item this record is about.
     * @see #owner
     */
    public void setOwner(final Owner owner) {
        this.owner = owner;
    }

    /**
     * Returns the location where this record’s item is kept.
     *
     * @return the location where this record’s item is kept.
     * @see #location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the location where this record’s item is kept.
     *
     * @param location
     * 		location where this record’s item is kept.
     * @see #location
     */
    public void setLocation(final Location location) {
        this.location = location;
    }

    /**
     * Is this record about an action story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getActionStyle() {
        return actionStyle;
    }

    /**
     * Set whether this record is about an action story.
     *
     * @param actionStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setActionStyle(final Boolean actionStyle) {
        this.actionStyle = actionStyle;
    }

    /**
     * Is this record about a documentary?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getDocumentaryStyle() {
        return documentaryStyle;
    }

    /**
     * Set whether this record is about a documentary.
     *
     * @param documentaryStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setDocumentaryStyle(final Boolean documentaryStyle) {
        this.documentaryStyle = documentaryStyle;
    }

    /**
     * Is this record about a fantasy story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getFantasyStyle() {
        return fantasyStyle;
    }

    /**
     * Set whether this record is about a fantasy story.
     *
     * @param fantasyStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setFantasyStyle(final Boolean fantasyStyle) {
        this.fantasyStyle = fantasyStyle;
    }

    /**
     * Is this record about a war story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getWarStyle() {
        return warStyle;
    }

    /**
     * Set whether this record is about a war story.
     *
     * @param warStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setWarStyle(final Boolean warStyle) {
        this.warStyle = warStyle;
    }

    /**
     * Is this record about a true story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getTrueStoryStyle() {
        return trueStoryStyle;
    }

    /**
     * Set whether this record is about a true story.
     *
     * @param trueStoryStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setTrueStoryStyle(final Boolean trueStoryStyle) {
        this.trueStoryStyle = trueStoryStyle;
    }

    /**
     * Is this record about a story with a historical background?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getHistoricalStyle() {
        return historicalStyle;
    }

    /**
     * Set whether this record is about a story with a historical background.
     *
     * @param historicalStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setHistoricalStyle(final Boolean historicalStyle) {
        this.historicalStyle = historicalStyle;
    }

    /**
     * Is this record about a funny story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getHumorStyle() {
        return humorStyle;
    }

    /**
     * Set whether this record is about a funny story.
     *
     * @param humorStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setHumorStyle(final Boolean humorStyle) {
        this.humorStyle = humorStyle;
    }

    /**
     * Is this record about a detective story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getDetectiveStyle() {
        return detectiveStyle;
    }

    /**
     * Set whether this record is about a detective story.
     *
     * @param detectiveStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setDetectiveStyle(final Boolean detectiveStyle) {
        this.detectiveStyle = detectiveStyle;
    }

    /**
     * Is this record about a romantic story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getRomanticStyle() {
        return romanticStyle;
    }

    /**
     * Set whether this record is about a romantic story.
     *
     * @param romanticStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setRomanticStyle(final Boolean romanticStyle) {
        this.romanticStyle = romanticStyle;
    }

    /**
     * Is this record about a science-fiction story?
     *
     * @return {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *         {@code null} if unknown.
     */
    public Boolean getSfStyle() {
	return sfStyle;
    }

    /**
     * Set whether this record is about a science-fiction story.
     *
     * @param sfStyle
     *            {@link Boolean#TRUE} if yes, {@link Boolean#FALSE} if no, and
     *            {@code null} if unknown.
     */
    public void setSfStyle(final Boolean sfStyle) {
	this.sfStyle = sfStyle;
    }

    /**
     * Returns the user who registered this record.
     *
     * @return the user who registered this record.
     * @see #creator
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Returns the creation date of this record.
     *
     * @return the creation date of this record.
     * @see #creation
     */
    public Date getCreation() {
        return creation;
    }

    /**
     * Returns the user who last modified this record.
     *
     * @return the user who last modified this record.
     * @see #lastModifier
     */
    public User getLastModifier() {
        return lastModifier;
    }

    /**
     * Returns the last modification date of this record.
     *
     * @return the last modification date of this record.
     * @see #lastModification
     */
    public Date getLastModification() {
        return lastModification;
    }

    @Override
    public String toString() {
	return "Record:" + id + ":" + title;
    }

}
