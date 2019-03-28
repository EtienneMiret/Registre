package fr.elimerl.registre.commands;

import fr.elimerl.registre.entities.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RecordCommand {

  public enum Type {
    movie, comic, book
  }

  private String title;

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
