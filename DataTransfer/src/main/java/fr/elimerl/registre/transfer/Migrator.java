package fr.elimerl.registre.transfer;

import java.sql.SQLException;

/**
 * Interface of the service responsible for migrating each batch of records.
 */
public interface Migrator {

    /**
     * Migrate {@code number} records starting from record {@code first}.
     *
     * @param first
     *		index of the first record to migrate. It is not its id, but its
     *		index when the records are ordered by ascending id.
     * @param number
     * 		max number of records to migrate.
     * @return the number of records actually migrated.
     * @throws SQLException in case there is an error accessing any database.
     */
    int migrateRecords(int first, int number) throws SQLException;

}
