package fr.elimerl.registre.commands;

import fr.elimerl.registre.constraints.MovieHasSupport;
import fr.elimerl.registre.entities.Book;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Movie;
import fr.elimerl.registre.entities.Named;
import fr.elimerl.registre.entities.Record;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

@MovieHasSupport
public class RecordCommand {

  private static final int NUM_BLANK_ACTORS = 3;

  public enum Type {
    movie, comic, book
  }

  private Long id;

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

  private Integer number;

  private String comment;

  private String owner;

  private String location;

  private MultipartFile picture;

  private boolean pictureDeleted;

  /**
   * Create an empty {@link RecordCommand}, good for creation or for populating
   * with a form.
   */
  public RecordCommand () {
    actors = new ArrayList<> ();
    addBlankActors ();
  }

  /**
   * Create a {@link RecordCommand} for edition of the given {@link Record}.
   *
   * @param record
   *          the record to prepare for edition.
   */
  public RecordCommand (Record record) {
    this.id = record.getId ();
    this.title = record.getTitle ();
    this.actors = new ArrayList<> ();
    if (record instanceof Movie) {
      Movie movie = (Movie) record;
      this.type = Type.movie;
      this.support = movie.getSupport ();
      this.director = get (movie, Movie::getDirector);
      this.actors.addAll (Optional.of (movie)
          .map (Movie::getActors)
          .map (Collection::stream)
          .orElse (Stream.empty ())
          .map (Named::getName)
          .collect(toSet ()));
      this.composer = get (movie, Movie::getComposer);
    } else if (record instanceof Comic) {
      Comic comic = (Comic) record;
      this.type = Type.comic;
      this.cartoonist = get (comic, Comic::getCartoonist);
      this.scriptWriter = get (comic, Comic::getScriptWriter);
    } else if (record instanceof Book) {
      Book book = (Book) record;
      this.type = Type.book;
      this.author = get (book, Book::getAuthor);
    }
    this.series = get (record, Record::getSeries);
    this.number = record.getNumber ();
    this.comment = record.getComment ();
    this.owner = get (record, Record::getOwner);
    this.location = get (record, Record::getLocation);
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
    this.number = null;
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

  public Long getId () {
    return id;
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

  public Integer getNumber () {
    return number;
  }

  public void setNumber (Integer number) {
    this.number = number;
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

  public boolean isPictureDeleted () {
    return pictureDeleted;
  }

  public void setPictureDeleted (boolean pictureDeleted) {
    this.pictureDeleted = pictureDeleted;
  }

  private static <T> String get (T t, Function<T, Named> f) {
    return Optional.of (t)
        .map (f)
        .map (Named::getName)
        .orElse (null);
  }

}
