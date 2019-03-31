package fr.elimerl.registre.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class Advice {

  private static final Logger logger =
      LoggerFactory.getLogger (Advice.class);

  @ExceptionHandler
  public ModelAndView handleError (Exception e, HttpServletResponse response) {
    logger.error ("Internal server error sent to client.", e);
    response.setStatus (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    return new ModelAndView ("error/internal-server-error");
  }

  @ExceptionHandler
  public ModelAndView handleValidationError (
      BindException e,
      HttpServletResponse response
  ) {
    logger.warn ("Bad request.", e);
    response.setStatus (HttpServletResponse.SC_BAD_REQUEST);
    return new ModelAndView ("error/bad-request");
  }

}
