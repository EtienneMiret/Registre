package fr.elimerl.registre.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Format {@link java.util.Date} for display in French.
 * <p>
 * This class will provide relative times, such as:
 * <ul>
 *   <li>just now,
 *   <li><i>n</i> minutes ago,
 *   <li>today
 *   <li>tomorrow
 *   <li>…
 * </ul>
 */
public class DateConverter implements Converter<Date, String> {

  /** Actual converter used for this job. */
  @Autowired
  private InstantConverter instantConverter;

  @Override
  public String convert (Date source) {
    return instantConverter.convert (source.toInstant ());
  }

}
