package fr.elimerl.registre.validators;

import fr.elimerl.registre.commands.RecordCommand;
import fr.elimerl.registre.constraints.MovieHasSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MovieHasSupportValidator
    implements ConstraintValidator<MovieHasSupport, RecordCommand> {

  @Override
  public void initialize (MovieHasSupport constraintAnnotation) {}

  @Override
  public boolean isValid (RecordCommand value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    if (value.getType () != RecordCommand.Type.movie) {
      return true;
    }
    return value.getSupport () != null;
  }

}
