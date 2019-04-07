package fr.elimerl.registre.controllers;

import fr.elimerl.registre.entities.Actor;
import fr.elimerl.registre.entities.Author;
import fr.elimerl.registre.entities.Cartoonist;
import fr.elimerl.registre.entities.Composer;
import fr.elimerl.registre.entities.Director;
import fr.elimerl.registre.entities.Location;
import fr.elimerl.registre.entities.Owner;
import fr.elimerl.registre.entities.ScriptWriter;
import fr.elimerl.registre.entities.Series;
import fr.elimerl.registre.services.AutoCompleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller responsible for providing a list of auto-completion suggestions
 * for {@link fr.elimerl.registre.entities.Named}.
 */
@Controller
@RequestMapping ("/auto-complete")
public class AutoComplete {

  /** Auto-completion service, provided by Spring. */
  @Autowired
  private AutoCompleter autoCompleter;

  /** Auto-complete actors. */
  @GetMapping ("/actors")
  @ResponseBody
  public List<String> actors (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Actor.class);
  }

  /** Auto-complete authors. */
  @GetMapping ("/authors")
  @ResponseBody
  public List<String> authors (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Author.class);
  }

  /** Auto-complete cartoonists. */
  @GetMapping ("/cartoonists")
  @ResponseBody
  public List<String> cartoonists (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Cartoonist.class);
  }

  /** Auto-complete composers. */
  @GetMapping ("/composers")
  @ResponseBody
  public List<String> composers (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Composer.class);
  }

  /** Auto-complete directors. */
  @GetMapping ("/directors")
  @ResponseBody
  public List<String> directors (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Director.class);
  }

  /** Auto-complete locations. */
  @GetMapping ("/locations")
  @ResponseBody
  public List<String> locations (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Location.class);
  }

  /** Auto-complete owners. */
  @GetMapping ("/owners")
  @ResponseBody
  public List<String> owners (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Owner.class);
  }

  /** Auto-complete script writers. */
  @GetMapping ("/script-writers")
  @ResponseBody
  public List<String> scriptWriters (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, ScriptWriter.class);
  }

  /** Auto-complete series. */
  @GetMapping ("/series")
  @ResponseBody
  public List<String> series (@RequestParam ("q") String query) {
    return autoCompleter.autoComplete (query, Series.class);
  }

}
