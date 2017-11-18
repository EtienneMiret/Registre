package fr.elimerl.registre.transfer;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Main class of the data transfer program.
 */
@Service("main")
public class DataTransfer {

    /** SLF4J logger for this class. */
    private static final Logger logger =
	    LoggerFactory.getLogger(DataTransfer.class);

    /**
     * Method responsible for launching this application.
     *
     * @param args
     *            not used.
     */
    public static void main(final String[] args) {
	removeAtGenerated();
	ClassPathXmlApplicationContext ctx = null;
	try {
	    ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	    final DataTransfer main =
		    ctx.getBean("main", DataTransfer.class);
	    main.migrateAllRecords();
	} catch (final BeanInitializationException e) {
	    logger.error("Impossible to instantiate context.", e);
	    if (e.getCause() instanceof FileNotFoundException) {
		System.err.println("There mus be a config.properties file in"
			+ " the classpath.");
	    }
	} finally {
	    if (ctx != null) {
		ctx.close();
	    }
	}
    }

    /**
     * Removes the {@code @Generated} annotation from the {@code Record} class
     * id. This allows us to keep the id of migrated records.
     */
    private static void removeAtGenerated() {
	try {
	    final ClassPool pool = ClassPool.getDefault();
	    final CtClass record = pool.get("fr.elimerl.registre.entities.Record");
	    final CtField id = record.getDeclaredField("id");
	    final FieldInfo info = id.getFieldInfo();
	    final AnnotationsAttribute attribute = (AnnotationsAttribute)
		    info.getAttribute(AnnotationsAttribute.visibleTag);
	    final Annotation[] oldAnnotations =
		    attribute.getAnnotations();
	    final Annotation[] newAnnotations =
		    new Annotation[oldAnnotations.length - 1];
	    int i = 0;
	    for (final Annotation annotation : oldAnnotations) {
		if (!annotation.getTypeName().equals(
			"javax.persistence.GeneratedValue")) {
		    newAnnotations[i] = annotation;
		    i++;
		}
	    }
	    attribute.setAnnotations(newAnnotations);
	    record.toClass();
	} catch (final NotFoundException e) {
	    final String message =
		    "Could not find the Record class or is id field.";
	    logger.error(message);
	    throw new RuntimeException(message, e);
	} catch (final CannotCompileException e) {
	    final String message =
		    "Could not compile the new Record class.";
	    logger.error(message);
	    throw new RuntimeException(message, e);
	}
    }

    /** Number of records to handle in a single transaction. */
    private int batchSize;

    /**
     * Service responsible for migrating each batch of records.
     */
    @Resource(name = "migrator")
    private Migrator migrator;

    /**
     * Main method of this application, it requests handling of each records.
     */
    public void migrateAllRecords() {
	int number = batchSize;
	int i = 0;
	logger.info("Starting migration.");
	while (number > 0) {
	    try {
		number = migrator.migrateRecords(i, batchSize);
		i += number;
		logger.info("{} records migrated.", new Integer(i));
	    } catch (final SQLException e) {
		number = 0;
		logger.error("An SQL error prevents us from going on.", e);
	    }
	}
	logger.info("Migration over.");
    }

    /**
     * Set the number of records to handle in a single transaction.
     *
     * @param batchSize
     *            number of records to migrate in a single transaction.
     */
    public void setBatchSize(final int batchSize) {
	this.batchSize = batchSize;
    }

}
