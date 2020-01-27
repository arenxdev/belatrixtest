package com.belatrix.test.belatrixtest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.belatrix.test.enums.LogMessageType;
import com.belatrix.test.exceptions.DatabaseParamsException;
import com.belatrix.test.exceptions.JobLoggerConfigException;
import com.belatrix.test.exceptions.JobLoggerValidationException;
import com.belatrix.test.utils.JobLogger;

public class CongifLoggerTest {
	
	@Test(expected = JobLoggerConfigException.class)
	public void exception_when_is_bad_configuration_in_log_to() throws Exception {
		JobLogger logger = new JobLogger(false, false, false, true, true, true, null, null);
		String messageText = "Message normal test";
		LogMessageType messageType = LogMessageType.MESSAGE;
		logger.logMessage(messageText, messageType);
	}
	
	@Test(expected = JobLoggerConfigException.class)
	public void exception_when_is_bad_configuration_in_message_to_log() throws Exception {
		JobLogger logger = new JobLogger(true, false, false, false, false, false, null, null);
		String messageText = "Message normal test";
		LogMessageType messageType = LogMessageType.MESSAGE;
		logger.logMessage(messageText, messageType);
	}
	
	@Test(expected = JobLoggerValidationException.class)
	public void exception_when_message_is_empty() throws Exception {
		JobLogger logger = new JobLogger(true, false, false, true, false, false, null, null);
		String messageText = "";
		LogMessageType messageType = LogMessageType.MESSAGE;
		logger.logMessage(messageText, messageType);
	}
	
	@Test(expected = JobLoggerValidationException.class)
	public void exception_when_info_message_is_not_enabled() throws Exception {
		JobLogger logger = new JobLogger(true, false, false, false, true, true, null, null);
		String messageText = "Message normal test";
		LogMessageType messageType = LogMessageType.MESSAGE;
		logger.logMessage(messageText, messageType);
	}
	
	@Test(expected = DatabaseParamsException.class)
	public void exception_when_there_are_missing_dbparams() throws Exception {
		Map<String, String> dbParams = new HashMap<>();
		JobLogger logger = new JobLogger(false, false, true, true, false, false, null, dbParams);
		String messageText = "Message normal test";
		LogMessageType messageType = LogMessageType.MESSAGE;
		logger.logMessage(messageText, messageType);
	}
	
}
