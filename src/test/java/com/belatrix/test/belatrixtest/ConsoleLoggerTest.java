package com.belatrix.test.belatrixtest;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.StreamHandler;

import org.junit.Before;
import org.junit.Test;

import com.belatrix.test.enums.LogMessageType;
import com.belatrix.test.utils.JobLogger;

public class ConsoleLoggerTest {
	
	private JobLogger logger;
	private OutputStream logOut;
	private StreamHandler testLogHandler;
	
	@Before
	public void setUp() {
		logger = new JobLogger(true, false, false, true, true, true, null, null);
		logOut = new ByteArrayOutputStream();
		Handler[] handlers = logger.getLoggerInstance().getParent().getHandlers();
		testLogHandler = new StreamHandler(logOut, handlers[0].getFormatter());
		logger.getLoggerInstance().addHandler(testLogHandler);
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
		testLogHandler.flush();
		String current = logOut.toString();
		String expected = logMessageType + " " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " "+ messageText;
		assertTrue(current.contains(expected));
	}
	
}
