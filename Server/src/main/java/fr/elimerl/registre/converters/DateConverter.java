package fr.elimerl.registre.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.util.Date;
import java.util.Locale;

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
public class DateConverter implements Formatter<Date> {

  /** Actual converter used for this job. */
  @Autowired
  private InstantConverter instantConverter;

  @Override
  public String print (Date source, Locale locale) {
    return instantConverter.print (source.toInstant (), locale);
  }

  @Override
  public Date parse (String text, Locale locale) {
    return Date.from (instantConverter.parse (text, locale));
  }

}
