package com.belatrix.test.belatrixtest;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.belatrix.test.enums.LogMessageType;
import com.belatrix.test.utils.JobLogger;

public class FileLoggerTest {
	
	private JobLogger logger;
	private String logDirectory;
	private String logFileName;
	private String messageLogTag;
	
	@Before
	public void setUp() throws Exception {
		logDirectory = "logtest";
		logFileName = "app.log";
		logger = new JobLogger(false, true, false, true, true, true, logDirectory, null);
		messageLogTag = "message";
		deleteOrCleanLogTestDirectory();
	}
	
	@After
	public void clean() throws Exception {
		deleteOrCleanLogTestDirectory();
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
		File logFile = new File(logDirectory, logFileName);
		FileReader fileReader = new FileReader(logFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String current = null;
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			if(line.contains("<" + messageLogTag + ">")) {
				current = line.replaceAll("^.*<" + messageLogTag + ">(.*?)</" + messageLogTag +">.*$", "$1");
				break;
			}
		}
		bufferedReader.close();
		fileReader.close();
		String expected = logMessageType + " " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " "+ messageText;
		assertTrue(current.contains(expected));
	}
	
	private void deleteOrCleanLogTestDirectory() throws Exception {
		File logFile = new File(logDirectory);
		if(logFile.exists()) {
			for (File file : logFile.listFiles()) {
				if(!file.delete()) {
					PrintWriter writer = new PrintWriter(file);
					writer.print("");
					writer.close();
				}
			}
			logFile.delete();
		}
	}

}
