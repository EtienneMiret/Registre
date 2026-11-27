package fr.elimerl.registre.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import fr.elimerl.registre.commands.RecordCommand;
import fr.elimerl.registre.constraints.MovieHasSupport;

public class MovieHasSupportValidator
    implements ConstraintValidator<MovieHasSupport, RecordCommand> {

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
