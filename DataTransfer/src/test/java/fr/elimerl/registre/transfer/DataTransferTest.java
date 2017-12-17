package fr.elimerl.registre.transfer;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class DataTransferTest {

  private static final Long SERIES_ME = 0L;
  private static final Long SERIES_WISH = 1L;

  private static final Long OWNER_ALICE = 0L;
  private static final Long OWNER_BOB = 1L;

  private static final Long LOCATION_PARIS = 0L;
  private static final Long LOCATION_NEWYORK = 1L;

  private static final Long USER_SYSTEM = 0L;
  private static final Long USER_BOB = 1L;
  private static final Long USER_ALICE = 2L;

  private static final Long DIRECTOR_SPIELBERG = 0L;
  private static final Long DIRECTOR_ME = 1L;

  private static final Long COMPOSER_ANDREW = 0L;
  private static final Long COMPOSER_ME = 1L;
  private static final Long COMPOSER_HOWARD = 2L;

  private static final Long ACTOR_ALICE = 0L;
  private static final Long ACTOR_BOB = 1L;
  private static final Long ACTOR_CLARA = 2L;
  private static final Long ACTOR_ME = 3L;

  private static final Long CARTOONIST_ALAN = 0L;

  private static final Long SCRIPT_WRITER_TURING = 0L;

  private static final Long AUTHOR_FOO = 0L;

  @Test
  public void test () throws Exception {
    try (Connection connection = DriverManager.getConnection ("jdbc:hsqldb:mem:input", "SA", "")) {
      ScriptUtils.executeSqlScript (connection, new EncodedResource (
          new ClassPathResource ("old-schema.sql"), StandardCharsets.UTF_8
      ));
      ScriptUtils.executeSqlScript (connection, new EncodedResource (
          new ClassPathResource ("test-data.sql"), StandardCharsets.UTF_8
      ));
    }
    try (Connection connection = DriverManager.getConnection ("jdbc:hsqldb:mem:output", "SA", "")) {
      ScriptUtils.executeSqlScript (connection, new EncodedResource (
          new ClassPathResource ("hsql-schema.sql"), StandardCharsets.UTF_8
      ));
    }

    DataTransfer.main (new String[0]);

    try (
        Connection connection = DriverManager.getConnection ("jdbc:hsqldb:mem:output", "SA", "");
        Statement statement = connection.createStatement ();
    ) {
      try (ResultSet rs = statement.executeQuery ("select * from records order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (0);
        assertThat (rs.getString ("title")).isEqualTo ("Hello");
        assertThat (rs.getString ("dtype")).isEqualTo ("Movie");
        assertThat (rs.getObject ("series")).isNull ();
        assertThat (rs.getString ("comment")).isEqualTo ("Hello World!");
        assertThat (rs.getString ("picture")).isEqualTo ("0.jpg");
        assertThat (rs.getObject ("owner")).isNull ();
        assertThat (rs.getObject ("location")).isNull ();
        assertThat (getBoolean (rs, "action_style")).isTrue ();
        assertThat (getBoolean (rs, "documentary_style")).isFalse ();
        assertThat (getBoolean (rs, "fantasy_style")).isTrue ();
        assertThat (getBoolean (rs, "war_style")).isFalse ();
        assertThat (getBoolean (rs, "true_story_style")).isFalse ();
        assertThat (getBoolean (rs, "historical_style")).isFalse ();
        assertThat (getBoolean (rs, "humor_style")).isFalse ();
        assertThat (getBoolean (rs, "detective_style")).isFalse ();
        assertThat (getBoolean (rs, "romantic_style")).isFalse ();
        assertThat (getBoolean (rs, "sf_style")).isFalse ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_SYSTEM);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-01-12T22:33:41"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_SYSTEM);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-01-12T22:33:41"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (1);
        assertThat (rs.getString ("title")).isEqualTo ("World");
        assertThat (rs.getString ("dtype")).isEqualTo ("Movie");
        assertThat (rs.getObject ("series")).isNull ();
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isEqualTo ("1.jpg");
        assertThat (rs.getObject ("owner")).isEqualTo (OWNER_ALICE);
        assertThat (rs.getObject ("location")).isNull ();
        assertThat (getBoolean (rs, "action_style")).isFalse ();
        assertThat (getBoolean (rs, "documentary_style")).isFalse ();
        assertThat (getBoolean (rs, "fantasy_style")).isFalse ();
        assertThat (getBoolean (rs, "war_style")).isTrue ();
        assertThat (getBoolean (rs, "true_story_style")).isFalse ();
        assertThat (getBoolean (rs, "historical_style")).isTrue ();
        assertThat (getBoolean (rs, "humor_style")).isFalse ();
        assertThat (getBoolean (rs, "detective_style")).isFalse ();
        assertThat (getBoolean (rs, "romantic_style")).isFalse ();
        assertThat (getBoolean (rs, "sf_style")).isFalse ();
        assertThat (rs.getObject ("creator")).isEqualTo (0L);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-01-12T22:33:41"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (1L);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-07-01T14:28:05"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (2);
        assertThat (rs.getString ("title")).isEqualTo ("How are you?");
        assertThat (rs.getString ("dtype")).isEqualTo ("Book");
        assertThat (rs.getObject ("series")).isNull ();
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isNull ();
        assertThat (rs.getObject ("owner")).isEqualTo (OWNER_ALICE);
        assertThat (rs.getObject ("location")).isEqualTo (LOCATION_PARIS);
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isFalse ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isFalse ();
        assertThat (getBoolean (rs, "historical_style")).isFalse ();
        assertThat (getBoolean (rs, "humor_style")).isFalse ();
        assertThat (getBoolean (rs, "detective_style")).isTrue ();
        assertThat (getBoolean (rs, "romantic_style")).isFalse ();
        assertThat (getBoolean (rs, "sf_style")).isFalse ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_SYSTEM);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-01-12T22:33:41"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_BOB);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-07-02T11:25:31"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (3);
        assertThat (rs.getString ("title")).isEqualTo ("I like this");
        assertThat (rs.getString ("dtype")).isEqualTo ("Comic");
        assertThat (rs.getObject ("series")).isEqualTo (SERIES_ME);
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isNull ();
        assertThat (rs.getObject ("owner")).isNull ();
        assertThat (rs.getObject ("location")).isEqualTo (LOCATION_PARIS);
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isNull ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isNull ();
        assertThat (getBoolean (rs, "historical_style")).isNull ();
        assertThat (getBoolean (rs, "humor_style")).isNull ();
        assertThat (getBoolean (rs, "detective_style")).isNull ();
        assertThat (getBoolean (rs, "romantic_style")).isNull ();
        assertThat (getBoolean (rs, "sf_style")).isNull ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_SYSTEM);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-01-12T22:33:41"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-07-03T12:29:34"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (4);
        assertThat (rs.getString ("title")).isEqualTo ("I’m the best");
        assertThat (rs.getString ("dtype")).isEqualTo ("Movie");
        assertThat (rs.getObject ("series")).isEqualTo (SERIES_ME);
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isNull ();
        assertThat (rs.getObject ("owner")).isEqualTo (OWNER_BOB);
        assertThat (rs.getObject ("location")).isNull ();
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isNull ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isNull ();
        assertThat (getBoolean (rs, "historical_style")).isNull ();
        assertThat (getBoolean (rs, "humor_style")).isNull ();
        assertThat (getBoolean (rs, "detective_style")).isNull ();
        assertThat (getBoolean (rs, "romantic_style")).isNull ();
        assertThat (getBoolean (rs, "sf_style")).isNull ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-02-14T07:12:28"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-07-04T05:18:21"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (5);
        assertThat (rs.getString ("title")).isEqualTo ("I’m the worst");
        assertThat (rs.getString ("dtype")).isEqualTo ("Movie");
        assertThat (rs.getObject ("series")).isEqualTo (SERIES_ME);
        assertThat (rs.getString ("comment")).isEqualTo ("This is bad");
        assertThat (rs.getString ("picture")).isNull ();
        assertThat (rs.getObject ("owner")).isEqualTo (OWNER_BOB);
        assertThat (rs.getObject ("location")).isEqualTo (LOCATION_NEWYORK);
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isNull ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isNull ();
        assertThat (getBoolean (rs, "historical_style")).isNull ();
        assertThat (getBoolean (rs, "humor_style")).isNull ();
        assertThat (getBoolean (rs, "detective_style")).isNull ();
        assertThat (getBoolean (rs, "romantic_style")).isNull ();
        assertThat (getBoolean (rs, "sf_style")).isNull ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_BOB);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-03-19T14:32:25"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-07-05T16:14:30"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (6);
        assertThat (rs.getString ("title")).isEqualTo ("Happy Birthday");
        assertThat (rs.getString ("dtype")).isEqualTo ("Comic");
        assertThat (rs.getObject ("series")).isEqualTo (SERIES_WISH);
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isEqualTo ("6.jpg");
        assertThat (rs.getObject ("owner")).isNull ();
        assertThat (rs.getObject ("location")).isNull ();
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isNull ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isNull ();
        assertThat (getBoolean (rs, "historical_style")).isNull ();
        assertThat (getBoolean (rs, "humor_style")).isNull ();
        assertThat (getBoolean (rs, "detective_style")).isNull ();
        assertThat (getBoolean (rs, "romantic_style")).isNull ();
        assertThat (getBoolean (rs, "sf_style")).isNull ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-04-21T19:56:02"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-04-21T19:56:02"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (7);
        assertThat (rs.getString ("title")).isEqualTo ("Merry Christmas");
        assertThat (rs.getString ("dtype")).isEqualTo ("Movie");
        assertThat (rs.getObject ("series")).isEqualTo (SERIES_WISH);
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isEqualTo ("7.jpg");
        assertThat (rs.getObject ("owner")).isNull ();
        assertThat (rs.getObject ("location")).isEqualTo (LOCATION_NEWYORK);
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isNull ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isNull ();
        assertThat (getBoolean (rs, "historical_style")).isNull ();
        assertThat (getBoolean (rs, "humor_style")).isNull ();
        assertThat (getBoolean (rs, "detective_style")).isNull ();
        assertThat (getBoolean (rs, "romantic_style")).isNull ();
        assertThat (getBoolean (rs, "sf_style")).isNull ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-04-29T15:17:43"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_ALICE);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-04-29T15:17:43"));

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (8);
        assertThat (rs.getString ("title")).isEqualTo ("Happy new year!");
        assertThat (rs.getString ("dtype")).isEqualTo ("Book");
        assertThat (rs.getObject ("series")).isEqualTo (SERIES_WISH);
        assertThat (rs.getString ("comment")).isNull ();
        assertThat (rs.getString ("picture")).isEqualTo ("8.jpg");
        assertThat (rs.getObject ("owner")).isEqualTo (OWNER_ALICE);
        assertThat (rs.getObject ("location")).isNull ();
        assertThat (getBoolean (rs, "action_style")).isNull ();
        assertThat (getBoolean (rs, "documentary_style")).isNull ();
        assertThat (getBoolean (rs, "fantasy_style")).isNull ();
        assertThat (getBoolean (rs, "war_style")).isNull ();
        assertThat (getBoolean (rs, "true_story_style")).isNull ();
        assertThat (getBoolean (rs, "historical_style")).isNull ();
        assertThat (getBoolean (rs, "humor_style")).isNull ();
        assertThat (getBoolean (rs, "detective_style")).isNull ();
        assertThat (getBoolean (rs, "romantic_style")).isNull ();
        assertThat (getBoolean (rs, "sf_style")).isNull ();
        assertThat (rs.getObject ("creator")).isEqualTo (USER_BOB);
        assertThat (rs.getTimestamp ("creation").toInstant ())
            .isEqualTo (parse ("2017-05-06T04:23:12"));
        assertThat (rs.getObject ("last_modifier")).isEqualTo (USER_BOB);
        assertThat (rs.getTimestamp ("last_modification").toInstant ())
            .isEqualTo (parse ("2017-07-08T15:14:32"));

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from movies order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (0);
        assertThat (rs.getString ("support")).isEqualTo ("DVD");
        assertThat (rs.getObject ("director")).isEqualTo (DIRECTOR_SPIELBERG);
        assertThat (rs.getObject ("composer")).isEqualTo (COMPOSER_ANDREW);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (1);
        assertThat (rs.getString ("support")).isEqualTo ("BRD");
        assertThat (rs.getObject ("director")).isEqualTo (DIRECTOR_SPIELBERG);
        assertThat (rs.getObject ("composer")).isNull ();

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (4);
        assertThat (rs.getString ("support")).isEqualTo ("DVD");
        assertThat (rs.getObject ("director")).isEqualTo (DIRECTOR_ME);
        assertThat (rs.getObject ("composer")).isEqualTo (COMPOSER_ME);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (5);
        assertThat (rs.getString ("support")).isEqualTo ("DVD");
        assertThat (rs.getObject ("director")).isNull ();
        assertThat (rs.getObject ("composer")).isNull ();

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (7);
        assertThat (rs.getString ("support")).isEqualTo ("BRD");
        assertThat (rs.getObject ("director")).isNull ();
        assertThat (rs.getObject ("composer")).isEqualTo (COMPOSER_HOWARD);

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from plays_in order by movie asc, actor asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("movie")).isEqualTo (0);
        assertThat (rs.getLong ("actor")).isEqualTo (ACTOR_ALICE);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("movie")).isEqualTo (0);
        assertThat (rs.getLong ("actor")).isEqualTo (ACTOR_BOB);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("movie")).isEqualTo (0);
        assertThat (rs.getLong ("actor")).isEqualTo (ACTOR_CLARA);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("movie")).isEqualTo (4);
        assertThat (rs.getLong ("actor")).isEqualTo (ACTOR_ME);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("movie")).isEqualTo (7);
        assertThat (rs.getLong ("actor")).isEqualTo (ACTOR_ALICE);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("movie")).isEqualTo (7);
        assertThat (rs.getLong ("actor")).isEqualTo (ACTOR_BOB);

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from comics order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (3);
        assertThat (rs.getObject ("cartoonist")).isEqualTo (CARTOONIST_ALAN);
        assertThat (rs.getObject ("script_writer")).isEqualTo (SCRIPT_WRITER_TURING);
        assertThat (rs.getObject ("number")).isEqualTo (42);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (6);
        assertThat (rs.getObject ("cartoonist")).isNull ();
        assertThat (rs.getObject ("script_writer")).isNull ();
        assertThat (rs.getObject ("number")).isNull ();

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from books order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (2);
        assertThat (rs.getObject ("author")).isEqualTo (AUTHOR_FOO);

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (8);
        assertThat (rs.getObject ("author")).isEqualTo (AUTHOR_FOO);

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from actors order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (ACTOR_ALICE);
        assertThat (rs.getString ("name")).isEqualTo ("Alice");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (ACTOR_BOB);
        assertThat (rs.getString ("name")).isEqualTo ("Bob");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (ACTOR_CLARA);
        assertThat (rs.getString ("name")).isEqualTo ("Clara");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (ACTOR_ME);
        assertThat (rs.getString ("name")).isEqualTo ("Me");

        assertThat (rs.next ()).isFalse ();
      }
      
      try (ResultSet rs = statement.executeQuery ("select * from authors order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (AUTHOR_FOO);
        assertThat (rs.getString ("name")).isEqualTo ("Foo");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from composers order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (COMPOSER_ANDREW);
        assertThat (rs.getString ("name")).isEqualTo ("Andrew");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (COMPOSER_ME);
        assertThat (rs.getString ("name")).isEqualTo ("Me");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (COMPOSER_HOWARD);
        assertThat (rs.getString ("name")).isEqualTo ("Howard");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from cartoonists order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (CARTOONIST_ALAN);
        assertThat (rs.getString ("name")).isEqualTo ("Alan");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from locations order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (LOCATION_PARIS);
        assertThat (rs.getString ("name")).isEqualTo ("Paris");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (LOCATION_NEWYORK);
        assertThat (rs.getString ("name")).isEqualTo ("New-York");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from owners order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (OWNER_ALICE);
        assertThat (rs.getString ("name")).isEqualTo ("Alice");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (OWNER_BOB);
        assertThat (rs.getString ("name")).isEqualTo ("Bob");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from directors order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (DIRECTOR_SPIELBERG);
        assertThat (rs.getString ("name")).isEqualTo ("Spielberg");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (DIRECTOR_ME);
        assertThat (rs.getString ("name")).isEqualTo ("Me");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from script_writers order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (SCRIPT_WRITER_TURING);
        assertThat (rs.getString ("name")).isEqualTo ("Turing");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from series order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (SERIES_ME);
        assertThat (rs.getString ("name")).isEqualTo ("Me");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (SERIES_WISH);
        assertThat (rs.getString ("name")).isEqualTo ("Wish");

        assertThat (rs.next ()).isFalse ();
      }

      try (ResultSet rs = statement.executeQuery ("select * from users order by id asc")) {
        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (USER_SYSTEM);
        assertThat (rs.getString ("name")).isEqualTo ("Système");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (USER_BOB);
        assertThat (rs.getString ("name")).isEqualTo ("Bob");

        assertThat (rs.next ()).isTrue ();
        assertThat (rs.getLong ("id")).isEqualTo (USER_ALICE);
        assertThat (rs.getString ("name")).isEqualTo ("Alice");

        assertThat (rs.next ()).isFalse ();
      }
    }
  }
  
  private static Instant parse (String datetime) {
    return LocalDateTime.parse (datetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .atZone (ZoneId.systemDefault ())
        .toInstant ();
  }
  
  private static Boolean getBoolean (ResultSet rs, String columnLabel)
    throws SQLException {
    boolean b = rs.getBoolean (columnLabel);
    return rs.wasNull () ? null : b;
  }

}
