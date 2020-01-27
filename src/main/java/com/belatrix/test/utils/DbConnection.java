package com.belatrix.test.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import com.belatrix.test.exceptions.DatabaseParamsException;

public class DbConnection {
	
	public static Connection getConnection(Map<String, String> dbParams) throws DatabaseParamsException, SQLException {
		validateParams(dbParams);
		Connection connection = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbParams.get("userName"));
		connectionProps.put("password", dbParams.get("password"));
	
		connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms")
			+ (dbParams.containsKey("serverName")  ? "://" + dbParams.get("serverName") : "")
			+ (dbParams.containsKey("portNumber")  ? ":" + dbParams.get("portNumber") : "")
		, connectionProps);
		return connection;
	}
	
	public static void validateParams(Map<String, String> dbParams) throws DatabaseParamsException {
		if(dbParams == null) {
			throw new DatabaseParamsException("dbParam can not be null to save log in database");
		} else {
			String missingParams = "";
			for (String dbParam : Arrays.asList("dbms", "userName", "password")) {
				if(!dbParams.containsKey(dbParam)) {
					missingParams += (missingParams.length() > 1 ? ", " : "") + dbParam; 
				}
			}
			if(!missingParams.isEmpty()) {
				throw new DatabaseParamsException("Database params " + missingParams + " are required to save log in database");
			}
		}
	}
	

}
