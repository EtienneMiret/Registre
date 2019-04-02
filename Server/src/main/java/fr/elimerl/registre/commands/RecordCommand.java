package fr.elimerl.registre.commands;

import fr.elimerl.registre.constraints.MovieHasSupport;
import fr.elimerl.registre.entities.Movie;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;

@MovieHasSupport
public class RecordCommand {

  private static final int NUM_BLANK_ACTORS = 3;

  public enum Type {
    movie, comic, book
  }

  @NotBlank
  private String title;

  @NotNull
  private Type type;

  private Movie.Support support;

  private String director;

  private List<String> actors;

  private String composer;

  private String cartoonist;

  private String scriptWriter;

  private String author;

  private String series;

  private String comment;

  private String owner;

  private String location;

  private MultipartFile picture;

  /**
   * Create an empty {@link RecordCommand}, good for creation or for populating
   * with a form.
   */
  public RecordCommand () {
    actors = new ArrayList<> ();
    addBlankActors ();
  }

  /**
   * Empty fields that we don't want to be reused for creation of subsequent
   * records. Also add some blank actors fields if needed.
   */
  public void prepareForReuse () {
    this.title = null;
    this.comment = null;
    this.picture = null;
    this.actors = actors.stream ()
        .filter (Objects::nonNull)
        .filter (s -> !s.isEmpty ())
        .collect (toCollection (ArrayList::new));
    addBlankActors ();
  }

  private void addBlankActors () {
    for (int i = 0; i < NUM_BLANK_ACTORS; i++) {
      actors.add ("");
    }
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public Type getType () {
    return type;
  }

  public void setType (Type type) {
    this.type = type;
  }

  public Movie.Support getSupport () {
    return support;
  }

  public void setSupport (Movie.Support support) {
    this.support = support;
  }

  public String getDirector () {
    return director;
  }

  public void setDirector (String director) {
    this.director = director;
  }

  public List<String> getActors () {
    return actors;
  }

  public void setActors (List<String> actors) {
    this.actors = actors;
  }

  public String getComposer () {
    return composer;
  }

  public void setComposer (String composer) {
    this.composer = composer;
  }

  public String getCartoonist () {
    return cartoonist;
  }

  public void setCartoonist (String cartoonist) {
    this.cartoonist = cartoonist;
  }

  public String getScriptWriter () {
    return scriptWriter;
  }

  public void setScriptWriter (String scriptWriter) {
    this.scriptWriter = scriptWriter;
  }

  public String getAuthor () {
    return author;
  }

  public void setAuthor (String author) {
    this.author = author;
  }

  public String getSeries () {
    return series;
  }

  public void setSeries (String series) {
    this.series = series;
  }

  public String getComment () {
    return comment;
  }

  public void setComment (String comment) {
    this.comment = comment;
  }

  public String getOwner () {
    return owner;
  }

  public void setOwner (String owner) {
    this.owner = owner;
  }

  public String getLocation () {
    return location;
  }

  public void setLocation (String location) {
    this.location = location;
  }

  public MultipartFile getPicture () {
    return picture;
  }

  public void setPicture (MultipartFile picture) {
    this.picture = picture;
  }

}
