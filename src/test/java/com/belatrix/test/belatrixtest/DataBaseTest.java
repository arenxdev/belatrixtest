package com.belatrix.test.belatrixtest;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.belatrix.test.enums.LogMessageType;
import com.belatrix.test.utils.DbConnection;
import com.belatrix.test.utils.JobLogger;

public class DataBaseTest {
	
	private JobLogger logger;
	private Connection connection;
	
	@Before
	public void setUp() throws Exception {
		Map<String, String> dbParams = new HashMap<>();
		dbParams.put("dbms", "h2:mem:test;MODE=MYSQL");
		dbParams.put("userName", "sa");
		dbParams.put("password", "sa");
		connection = DbConnection.getConnection(dbParams);
		Statement statement = connection.createStatement();
		String sqlDropLogTable = "DROP TABLE IF EXISTS log_values";
		String sqlCreateLogTable = "CREATE TABLE log_values (message VARCHAR(255), type TINYINT)";
		statement.executeUpdate(sqlDropLogTable);
		statement.executeUpdate(sqlCreateLogTable);
		logger = new JobLogger(false, false, true, true, true, true, null, dbParams);
	}
	
	@After
	public void clean() throws Exception {
		if(connection != null) {
			connection.close();
		}
	}
	
	@Test
	public void log_when_message_is_normal() throws Exception {
		String messageText = "Message normal test";
		LogMessageType messageType = LogMessageType.MESSAGE;
		test_log_message(messageText, messageType);
	}
	
	@Test
	public void log_when_message_is_warning() throws Exception {
		String messageText = "Message warning test";
		LogMessageType messageType = LogMessageType.WARNING;
		test_log_message(messageText, messageType);
	}
	
	@Test
	public void log_when_message_is_error() throws Exception {
		String messageText = "Message error test";
		LogMessageType messageType = LogMessageType.ERROR;
		test_log_message(messageText, messageType);
	}
	
	private void test_log_message(String messageText, LogMessageType logMessageType) throws Exception {
		logger.logMessage(messageText, logMessageType);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM log_values");
		boolean isLogSaved = false;
		while(resultSet.next()) {
			String messageTextSaved = resultSet.getString("message");
			Integer LogMessageTypeSaved = resultSet.getInt("type");
			isLogSaved = messageText.equals(messageTextSaved) && logMessageType.getValue() == LogMessageTypeSaved;
			break;
		}
		assertTrue(isLogSaved);
	}

}
