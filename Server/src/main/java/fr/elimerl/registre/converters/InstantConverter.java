package fr.elimerl.registre.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Format JSR 310 {@link Instant} for display in French.
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
public class InstantConverter implements Formatter<Instant> {

  private static final DateTimeFormatter TODAY =
      DateTimeFormatter.ofPattern ("'aujourd’hui à' H'h'mm");

  private static final DateTimeFormatter YESTERDAY =
      DateTimeFormatter.ofPattern ("'hier à' H'h'mm");

  private static final DateTimeFormatter TOMORROW =
      DateTimeFormatter.ofPattern ("'demain à' H'h'mm");

  private static final DateTimeFormatter DAY_BEFORE_YESTERDAY =
      DateTimeFormatter.ofPattern ("'avant-hier à' H'h'mm");

  private static final DateTimeFormatter DAY_AFTER_TOMORROW =
      DateTimeFormatter.ofPattern ("'après-demain à' H'h'mm");

  private static final DateTimeFormatter DATE_WITH_TIME =
      DateTimeFormatter.ofPattern ("EEEE d MMMM 'à' H'h'mm")
          .withLocale (Locale.FRANCE);

  private static final DateTimeFormatter FIRST_WITH_TIME =
      DateTimeFormatter.ofPattern ("EEEE '1er' MMMM 'à' H'h'mm")
          .withLocale (Locale.FRANCE);

  private static final DateTimeFormatter DATE_WITH_YEAR =
      DateTimeFormatter.ofPattern ("EEEE d MMMM yyyy")
          .withLocale (Locale.FRANCE);

  private static final DateTimeFormatter FIRST_WITH_YEAR =
      DateTimeFormatter.ofPattern ("EEEE '1er' MMMM yyyy")
          .withLocale (Locale.FRANCE);

  /**
   * A clock, used to retrieve the current time and timezone.
   */
  @Autowired
  private Clock clock;

  @Override
  public String print (Instant source, Locale locale) {
    Instant now = Instant.now (clock);
    if (source.compareTo (now) <= 0) {
      return convertPast (source, now);
    } else {
      return convertFuture (source, now);
    }
  }

  @Override
  public Instant parse (String text, Locale locale) {
    throw new UnsupportedOperationException ();
  }

  /**
   * Format the {@code source} instant, relative to {@code now} and assumed to
   * be in the past.
   *
   * @param source
   *     instant to format in French.
   * @param now
   *     current instant, for relative display.
   * @return {@code source}, in French.
   */
  private String convertPast (Instant source, Instant now) {
    if (now.minusSeconds (60).isBefore (source)) {
      return "à l’instant";
    } else if (now.minusSeconds (3600).isBefore (source)) {
      long delta = Duration.between (source, now).get (SECONDS) / 60;
      return String.format ("il y a %d minutes", delta);
    } else {
      LocalDate today = now.atZone (clock.getZone ()).toLocalDate ();
      LocalDateTime sourceDateTime =
          source.atZone (clock.getZone ()).toLocalDateTime ();
      return convertPast (sourceDateTime, today);
    }
  }

  /**
   * Format the {@code source} local date/time, relative to {@code today} and
   * assumed to be a bit awhile in the past.
   *
   * @param source
   *     local date/time to format in French.
   * @param today
   *     the current date, for relative display.
   * @return {@code source}, in French.
   */
  private String convertPast (LocalDateTime source, LocalDate today) {
    LocalDate sourceDate = source.toLocalDate ();
    if (sourceDate.equals (today)) {
      return TODAY.format (source);
    } else if (sourceDate.equals (today.minusDays (1))) {
      return YESTERDAY.format (source);
    } else if (sourceDate.equals (today.minusDays (2))) {
      return DAY_BEFORE_YESTERDAY.format (source);
    } else if (sourceDate.compareTo (today.minusMonths (6)) >= 0) {
      return formatWithTime (source);
    } else {
      return formatWithYear (source);
    }
  }

  /**
   * Format the {@code source} instant, relative to {@code now} and assumed to
   * be in the future.
   *
   * @param source
   *     instant to format in French.
   * @param now
   *     current instant, for relative display.
   * @return {@code source}, in French.
   */
  private String convertFuture (Instant source, Instant now) {
    if (now.plusSeconds (60).isAfter (source)) {
      return "dans un instant";
    } else if (now.plusSeconds (3600).isAfter (source)) {
      long delta = Duration.between (now, source).get (SECONDS) / 60;
      return String.format ("dans %d minutes", delta);
    } else {
      LocalDate today = now.atZone (clock.getZone ()).toLocalDate ();
      LocalDateTime sourceDateTime =
          source.atZone (clock.getZone ()).toLocalDateTime ();
      return convertFuture (sourceDateTime, today);
    }
  }

  /**
   * Format the {@code source} local date/time, relative to {@code today} and
   * assumed to be in the future.
   *
   * @param source
   *     local date/time to format in French.
   * @param today
   *     the current date, for relative display.
   * @return {@code source}, in French.
   */
  private String convertFuture (LocalDateTime source, LocalDate today) {
    LocalDate sourceDate = source.toLocalDate ();
    if (sourceDate.equals (today)) {
      return TODAY.format (source);
    } else if (sourceDate.equals (today.plusDays (1))) {
      return TOMORROW.format (source);
    } else if (sourceDate.equals (today.plusDays (2))) {
      return DAY_AFTER_TOMORROW.format (source);
    } else if (sourceDate.compareTo (today.plusMonths (6)) <= 0) {
      return formatWithTime (source);
    } else {
      return formatWithYear (source);
    }
  }

  /**
   * Format a local date/time in French, with the time and without the year.
   *
   * @param source
   *     local date/time to format in French.
   * @return {@code source} in French, with the time and without the year.
   */
  private String formatWithTime (LocalDateTime source) {
    if (source.getDayOfMonth () == 1) {
      return FIRST_WITH_TIME.format (source);
    } else {
      return DATE_WITH_TIME.format (source);
    }
  }

  /**
   * Format a local date/time in French, with the year and without the time.
   *
   * @param source
   *     local date/time to format in French.
   * @return {@code source} in French, with the year and without the time.
   */
  private String formatWithYear (LocalDateTime source) {
    if (source.getDayOfMonth () == 1) {
      return FIRST_WITH_YEAR.format (source);
    } else {
      return DATE_WITH_YEAR.format (source);
    }
  }

}
