package fr.elimerl.registre.converters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class InstantConverterTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule ()
      .strictness (Strictness.STRICT_STUBS);

  @InjectMocks
  private InstantConverter converter;

  @Mock
  private Clock clock;

  @Before
  public void setCurrentTime () {
    when (clock.instant ()).thenReturn (Instant.parse ("2019-04-04T19:40:12Z"));
  }

  @Test
  public void less_than_1_min_ago () {
    String actual = converter.print (Instant.parse ("2019-04-04T19:39:51Z"), null);

    assertThat (actual).isEqualTo ("à l’instant");
  }

  @Test
  public void less_than_1_min_from_now () {
    String actual = converter.print (Instant.parse ("2019-04-04T19:41:02Z"), null);

    assertThat (actual).isEqualTo ("dans un instant");
  }

  @Test
  public void less_than_1_hour_ago () {
    String actual = converter.print (Instant.parse ("2019-04-04T19:35:41Z"), null);

    assertThat (actual).isEqualTo ("il y a 4 minutes");
  }

  @Test
  public void less_than_1_hour_from_now () {
    String actual = converter.print (Instant.parse ("2019-04-04T20:12:04Z"), null);

    assertThat (actual).isEqualTo ("dans 31 minutes");
  }

  @Test
  public void today_more_than_1_hour_ago () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-04-04T13:32:12Z"), null);

    assertThat (actual).isEqualTo ("aujourd’hui à 15h32");
  }

  @Test
  public void today_more_than_1_hour_from_now () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-04-04T21:13:27Z"), null);

    assertThat (actual).isEqualTo ("aujourd’hui à 23h13");
  }

  @Test
  public void yesterday () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-04-02T23:38:14Z"), null);

    assertThat (actual).isEqualTo ("hier à 1h38");
  }

  @Test
  public void tomorrow () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-04-05T00:58:32Z"), null);

    assertThat (actual).isEqualTo ("demain à 2h58");
  }

  @Test
  public void the_day_before_yesterday () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-04-02T10:12:35Z"), null);

    assertThat (actual).isEqualTo ("avant-hier à 12h12");
  }

  @Test
  public void the_day_after_tomorrow () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-04-06T13:09:23Z"), null);

    assertThat (actual).isEqualTo ("après-demain à 15h09");
  }

  @Test
  public void less_than_6_months_ago () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-01-07T16:39:53Z"), null);

    assertThat (actual).isEqualTo ("lundi 7 janvier à 17h39");
  }

  @Test
  public void less_than_6_months_from_now () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2019-07-14T06:00:00Z"), null);

    assertThat (actual).isEqualTo ("dimanche 14 juillet à 8h00");
  }

  @Test
  public void more_than_6_months_ago () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2017-12-25T15:17:02Z"), null);

    assertThat (actual).isEqualTo ("lundi 25 décembre 2017");
  }

  @Test
  public void more_than_6_months_from_now () {
    when (clock.getZone ()).thenReturn (ZoneId.of ("Europe/Paris"));

    String actual = converter.print (Instant.parse ("2020-01-31T23:45:01Z"), null);

    assertThat (actual).isEqualTo ("samedi 1er février 2020");
  }

}
