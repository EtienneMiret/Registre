package fr.elimerl.registre.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Main class of the data transfer program.
 */
@Service ("main")
public class DataTransfer {

  /** SLF4J logger for this class. */
  private static final Logger logger =
      LoggerFactory.getLogger (DataTransfer.class);

  /**
   * Method responsible for launching this application.
   *
   * @param args
   *     not used.
   */
  public static void main (final String[] args) {
    try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext ("applicationContext.xml")) {
      final DataTransfer main =
          ctx.getBean ("main", DataTransfer.class);
      main.migrateAllRecords ();
    } catch (final BeanInitializationException e) {
      logger.error ("Impossible to instantiate context.", e);
      if (e.getCause () instanceof FileNotFoundException) {
        System.err.println ("There mus be a config.properties file in"
            + " the classpath.");
      }
    }
  }

  /** Number of records to handle in a single transaction. */
  private int batchSize;

  /**
   * Service responsible for migrating each batch of records.
   */
  @Resource (name = "migrator")
  private Migrator migrator;

  /**
   * Main method of this application, it requests handling of each records.
   */
  private void migrateAllRecords () {
    int number = batchSize;
    int i = 0;
    logger.info ("Starting migration.");
    while (number > 0) {
      try {
        number = migrator.migrateRecords (i, batchSize);
        i += number;
        logger.info ("{} records migrated.", new Integer (i));
      } catch (final SQLException e) {
        number = 0;
        logger.error ("An SQL error prevents us from going on.", e);
      }
    }
    logger.info ("Migration over.");
  }

  /**
   * Set the number of records to handle in a single transaction.
   *
   * @param batchSize
   *     number of records to migrate in a single transaction.
   */
  public void setBatchSize (final int batchSize) {
    this.batchSize = batchSize;
  }

}
