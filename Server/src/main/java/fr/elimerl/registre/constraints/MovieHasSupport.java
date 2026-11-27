package fr.elimerl.registre.constraints;

import java.lang.annotation.Retention;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import fr.elimerl.registre.validators.MovieHasSupportValidator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Requires that a record of type
 * {@link fr.elimerl.registre.commands.RecordCommand.Type#movie} has a support
 * defined.
 */
@Constraint (validatedBy = MovieHasSupportValidator.class)
@Retention (RUNTIME)
public @interface MovieHasSupport {

  String message () default "{movie.record.support.required}";

  Class<?>[] groups () default {};

  Class<? extends Payload>[] payload () default {};

}
