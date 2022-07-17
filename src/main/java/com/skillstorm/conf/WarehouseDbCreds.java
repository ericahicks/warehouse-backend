package com.skillstorm.conf;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** 
 * Represents a Database Connection Credentials including URL, username,
 * and password.
 *
 */
public class WarehouseDbCreds {

	/** Singleton instance of this class. */
	private static WarehouseDbCreds instance;
	/** URL of the database */
	private String url;
	/** Username for the database */
	private String username;
	/** Password for the database */
	private String password;
	
	/** 
	 * Extracts the database credentials from the application.&nbsp;properties file
	 * and set the instance variables. The instance variables may fail to be set 
	 * if an IOException or ClassNotFoundExceptions occurs 
	 * (these are handled in a try-catch) but are not recoverable from.
	 * Constructor is marked private bc this is a Singleton class and only the
	 * <code>getInstance</code> method should call it.
	 */
	private WarehouseDbCreds() {
		try {
			// Load it into memory immediately so that I have it
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Read the properties (key/value pairs) from the application.properties
			try (InputStream input = WarehouseDbCreds.class.getClassLoader()
					.getResourceAsStream("application.properties")) {
				// Properties object
				Properties props = new Properties();
				props.load(input); // Load in the file we opened
				
				// Grab out the keys that I want
				this.url = props.getProperty("db.url");
				this.username = props.getProperty("db.username");
				this.password = props.getProperty("db.password");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Lazily initializes an instance of this class.
	 * @return instance The singleton instance of this class.
	 */
	public static WarehouseDbCreds getInstance() {
		if (instance == null) { // Lazily initialize a connection to the DB
			instance = new WarehouseDbCreds();
		}
		return instance;
	}

	/**
	 * Gets the url needed to connect to the database.
	 * @return url The database's url including port and /schema specified
	 */
	public String getUrl() {
		return url;
	}

	/** 
	 * Gets the username for the database connection.
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password for the database connection credentials.
	 * Note: the password is returned as plain text and not encrypted.
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	
	/** 
	 * Gets a connection object to use for connecting to the database.
	 * @return connection
	 * @throws SQLException
	 */
	// Makes it simpler to retrieve a connection
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
}
