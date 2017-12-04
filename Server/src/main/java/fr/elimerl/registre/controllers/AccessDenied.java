package fr.elimerl.registre.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessDenied {

  @RequestMapping("/error/forbidden")
  public ModelAndView accessDenied () {
    return new ModelAndView ("error/forbidden");
  }

}
