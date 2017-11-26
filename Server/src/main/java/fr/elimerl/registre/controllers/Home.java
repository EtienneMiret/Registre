package fr.elimerl.registre.controllers;

import fr.elimerl.registre.security.RAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Controller for the home page.
 */
@Controller
public class Home {

  @RequestMapping("/")
  public String home (Model model, Principal principal, HttpServletRequest request) {
    RAuthenticationToken token = (RAuthenticationToken) principal;
    model.addAttribute ("user", token.getPrincipal ());
    model.addAttribute ("contextPath", request.getContextPath ());
    model.addAttribute ("_csrf", request.getAttribute ("_csrf"));
    return "home";
  }

}
