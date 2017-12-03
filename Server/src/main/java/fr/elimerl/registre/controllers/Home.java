package fr.elimerl.registre.controllers;

import fr.elimerl.registre.security.RAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the home page.
 */
@Controller
public class Home {

  @RequestMapping("/")
  public ModelAndView home (Principal principal) {
    Map<String, Object> model = new HashMap<> ();
    RAuthenticationToken token = (RAuthenticationToken) principal;
    model.put ("user", token.getPrincipal ());
    return new ModelAndView ("home", model);
  }

}
