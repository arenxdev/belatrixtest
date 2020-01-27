package com.belatrix.test.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.belatrix.test.enums.LogMessageType;
import com.belatrix.test.exceptions.DatabaseParamsException;
import com.belatrix.test.exceptions.JobLoggerConfigException;
import com.belatrix.test.exceptions.JobLoggerValidationException;

public class JobLogger {

	private static final Logger logger = Logger.getLogger("myLog");

	private boolean logToFile;
	private boolean logToConsole;
	private boolean logToDatabase;
	private boolean logNormal;
	private boolean logWarning;
	private boolean logError;
	private String logFilePath;
	private Map<String, String> dbParams;
	private static Map<String, FileHandler> mapFileHandlers = new HashMap<String, FileHandler>();

	public JobLogger(boolean logToConsole, boolean logToFile, boolean logToDatabase, boolean logMessage,
			boolean logWarning, boolean logError, String logFilePath, Map<String, String> dbParams) {
		this.logToFile = logToFile;
		this.logToConsole = logToConsole;
		this.logToDatabase = logToDatabase;
		this.logNormal = logMessage;
		this.logWarning = logWarning;
		this.logError = logError;
		this.logFilePath = logFilePath;
		this.dbParams = dbParams;
	}

	public void logMessage(String messageText, LogMessageType messageType) throws JobLoggerConfigException,
			JobLoggerValidationException, DatabaseParamsException, SecurityException, IOException {
		validate(messageText, messageType);

		cleanLoggerHandlers();

		String messageInLog = messageType + " " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " "
				+ messageText;

		if (logToConsole) {
			logger.addHandler(new ConsoleHandler());
		}
		if (logToFile) {
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.mkdir();
			}
			if (!mapFileHandlers.containsKey(logFilePath)) {
				mapFileHandlers.put(logFilePath, new FileHandler(logFile.getAbsolutePath() + "\\app.log"));
			}
			logger.addHandler(mapFileHandlers.get(logFilePath));
		}
		if (logToDatabase) {
			Connection connection = null;
			Statement statement;
			try {
				connection = DbConnection.getConnection(dbParams);
				statement = connection.createStatement();
				statement.executeUpdate("INSERT INTO log_values (message, type) VALUES ('" + messageText + "', "
						+ messageType.getValue() + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {}
				}
			}
		}
		logger.log(Level.INFO, messageInLog);

	}

	private void validate(String messageText, LogMessageType messageType)
			throws JobLoggerConfigException, JobLoggerValidationException, DatabaseParamsException {
		if (!logToConsole && !logToFile && !logToDatabase ) {
			throw new JobLoggerConfigException(
					"Invalid configuration: logToConsole or logToFile or logToDatabase must be enabled");
		}
		if(!logNormal && !logWarning && !logError ) {
			throw new JobLoggerConfigException(
					"Invalid configuration: logNormal or logWarning or logError must be enabled");
		}
		messageText = messageText.trim();
		if (messageText == null || messageText.isEmpty()) {
			throw new JobLoggerValidationException("Message can not be null or empty");
		}
		if ((messageType.equals(LogMessageType.MESSAGE) && !logNormal)
				|| (messageType.equals(LogMessageType.WARNING) && !logWarning)
				|| (messageType.equals(LogMessageType.ERROR) && !logError)) {
			throw new JobLoggerValidationException(messageType + " message is not enabled to be logged");
		}
		if (logToFile && (logFilePath == null || logFilePath.isEmpty())) {
			throw new JobLoggerValidationException("logFilePath can not be null or empty when logToFile is true");
		}
		if (logToDatabase) {
			DbConnection.validateParams(dbParams);
		}
	}

	private void cleanLoggerHandlers() {
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			if (!(handler instanceof StreamHandler)) {
				logger.removeHandler(handler);
			}
		}
		logger.setUseParentHandlers(false);
	}

	public Logger getLoggerInstance() {
		return logger;
	}

}
