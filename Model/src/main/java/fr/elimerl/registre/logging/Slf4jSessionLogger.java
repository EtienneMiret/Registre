package fr.elimerl.registre.logging;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Route EclipseLink’s logs to SLF4J.
 */
public class Slf4jSessionLogger extends AbstractSessionLog {

    public static final String ECLIPSELINK_NAMESPACE
    	    = "org.eclipse.persistence";

    public static final String DEFAULT_CATEGORY = "default";

    private Map<Integer, LogLevel> mapLevels;
    private Map<String, Logger> categoryLoggers = new HashMap<String, Logger>();

    public Slf4jSessionLogger() {
	super();
	createCategoryLoggers();
	initMapLevels();
    }

    @Override
    public void log(final SessionLogEntry entry) {
	if (!shouldLog(entry.getLevel(), entry.getNameSpace())) {
	    return;
	}

	Logger logger = getLogger(entry.getNameSpace());
	LogLevel logLevel = getLogLevel(entry.getLevel());

	StringBuilder message = new StringBuilder();

	message.append(getSupplementDetailString(entry));
	message.append(formatMessage(entry));

	switch (logLevel) {
	case TRACE:
	    logger.trace(message.toString());
	    break;
	case DEBUG:
	    logger.debug(message.toString());
	    break;
	case INFO:
	    logger.info(message.toString());
	    break;
	case WARN:
	    logger.warn(message.toString());
	    break;
	case ERROR:
	    logger.error(message.toString());
	    break;
	}
    }

    @Override
    public boolean shouldLog(int level, String category) {
	Logger logger = getLogger(category);
	boolean resp = false;

	LogLevel logLevel = getLogLevel(level);

	switch (logLevel) {
	case TRACE:
	    resp = logger.isTraceEnabled();
	    break;
	case DEBUG:
	    resp = logger.isDebugEnabled();
	    break;
	case INFO:
	    resp = logger.isInfoEnabled();
	    break;
	case WARN:
	    resp = logger.isWarnEnabled();
	    break;
	case ERROR:
	    resp = logger.isErrorEnabled();
	    break;
	}

	return resp;
    }

    @Override
    public boolean shouldLog(final int level) {
	return shouldLog(level, "default");
    }

    /**
     * Return true if SQL logging should log visible bind parameters. If the
     * shouldDisplayData is not set, return false.
     */
    @Override
    public boolean shouldDisplayData() {
	if (this.shouldDisplayData != null) {
	    return shouldDisplayData.booleanValue();
	} else {
	    return false;
	}
    }

    /**
     * Initialize loggers eagerly
     */
    private void createCategoryLoggers() {
	for (String category : SessionLog.loggerCatagories) {
	    addLogger(category, ECLIPSELINK_NAMESPACE + "." + category);
	}
	addLogger(DEFAULT_CATEGORY, ECLIPSELINK_NAMESPACE);
    }

    /**
     * Adds Logger to the categoryLoggers.
     */
    private void addLogger(final String loggerCategory,
	    final String loggerNameSpace) {
	categoryLoggers.put(loggerCategory,
		LoggerFactory.getLogger(loggerNameSpace));
    }

    /**
     * Returns the Logger for the given category.
     * @param category the catgory for the
     * @return the Logger for the given category.
     */
    private Logger getLogger(final String category) {
	return categoryLoggers.containsKey(category) ? categoryLoggers
		.get(category) : categoryLoggers.get(DEFAULT_CATEGORY);
    }

    /**
     * Returns the corresponding Slf4j level for a given EclipseLink level.
     * @return the corresponding Slf4j level for a given EclipseLink level.
     */
    private LogLevel getLogLevel(final Integer level) {
	return mapLevels.containsKey(level) ? mapLevels.get(level)
		: LogLevel.OFF;
    }

    /**
     * SLF4J log levels.
     */
    enum LogLevel {
	TRACE, DEBUG, INFO, WARN, ERROR, OFF
    }

    private void initMapLevels() {
	mapLevels = new HashMap<Integer, LogLevel>();

	mapLevels.put(SessionLog.ALL, LogLevel.TRACE);
	mapLevels.put(SessionLog.FINEST, LogLevel.TRACE);
	mapLevels.put(SessionLog.FINER, LogLevel.TRACE);
	mapLevels.put(SessionLog.FINE, LogLevel.DEBUG);
	mapLevels.put(SessionLog.CONFIG, LogLevel.INFO);
	mapLevels.put(SessionLog.INFO, LogLevel.INFO);
	mapLevels.put(SessionLog.WARNING, LogLevel.WARN);
	mapLevels.put(SessionLog.SEVERE, LogLevel.ERROR);
    }

}
