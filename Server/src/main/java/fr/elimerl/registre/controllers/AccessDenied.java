package fr.elimerl.registre.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;

@Controller
public class AccessDenied {

  @RequestMapping("/error/forbidden")
  public String accessDenied (
      Model model,
      WebRequest request,
      Principal principal
  ) {
    model.addAttribute ("contextPath", request.getContextPath ());
    model.addAttribute ("_csrf",
        request.getAttribute ("_csrf", RequestAttributes.SCOPE_REQUEST));
    model.addAttribute ("principal", principal);
    return "error/forbidden";
  }

}
