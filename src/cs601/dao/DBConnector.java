package cs601.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

import cs601.util.Status;

/**
 * A class - load database configuration and connect to the database.
 * Configuration file - "Database.properties" is used to store username, password, database, and hostname.
 * If off-campus, the tunnel to stargate.cs.usfca.edu must be run first.
 * 
 * Command Line as follows:
 * "ssh cwang87@stargate.cs.usfca.edu -L 3307:sql.cs.usfca.edu:3306 -N"
 */

public class DBConnector {
	
	/** URI to use when connecting to database. Should be in the format:
	 * jdbc:subprotocol://hostname/database
	 * (subprotocol can be mysql, oracle, etc.)
	 */
	public final String uri;
	
	/** Properties with username and password for connecting to database. */
	private final Properties dbConfig;
	
	
	/**Default constructor - no parameters needed
	 * Creates a connector from a "database.properties" file located in the current working directory.
	 */
	public DBConnector() throws FileNotFoundException, IOException {
		this("database.properties");
	}
	
	/**Constructor with parameter - String configPath
	 * Creates a connector from the provided database properties file.
	 * @param configPath path to the database properties file
	 */
	public DBConnector(String configPath) throws FileNotFoundException, IOException {

		Properties config = loadConfig(configPath);

		uri = String.format("jdbc:mysql://%s/%s", config.getProperty("hostname"), config.getProperty("database"));
		System.out.println("uri = " + uri);

		dbConfig = new Properties();
		dbConfig.put("user", config.getProperty("username"));
		dbConfig.put("password", config.getProperty("password"));
	}


	/**
	 * A method - attempts to load properties file with database configuration. 
	 * Must include username, password, database and hostname.
	 * @param configPath path to database properties file
	 * @return database properties
	 */
	private Properties loadConfig(String configPath) throws FileNotFoundException, IOException {
		// Specify which keys must be in properties file
		Set<String> required = new HashSet<>();
		required.add("username");
		required.add("password");
		required.add("database");
		required.add("hostname");

		// Load properties file
		Properties config = new Properties();
		config.load(new FileReader(configPath));

		// Check all the required keys are present
		if (!config.keySet().containsAll(required)) {
			String error = "Must provide the following in properties file: ";
			throw new InvalidPropertiesFormatException(error + required);
		}
		return config;
	}
	
	
	/** A method - attempts to connect to database using loaded configuration.
	 * getConnection(url, user, password);
	 * @return database connection
	 */
	public Connection getConnection() throws SQLException {
		Connection dbConnection = DriverManager.getConnection(uri, dbConfig);
		return dbConnection;
	}

	
	
	
	
	
	
	
	
	
	/** Opens a database connection, executes a simple statement, and closes the database connection.
	 * @return true if all operations successful
	 */
	public boolean testConnection() {
		boolean okay = false;

		try (Connection db = getConnection();) {
			System.out.println("Executing SHOW TABLES...");
			Set<String> tables = getTables(db);

			if (tables != null) {
				System.out.print("Found " + tables.size() + " tables: ");
				System.out.println(tables);
				okay = true;
			}
		}
		catch (SQLException e) {
			System.err.println(Status.SQL_EXCEPTION);
		}
		return okay;
	}
	
	/** Opens a database connection and returns a set of found tables. 
	 * Will return an empty set if there are no results.
	 * @return set of tables
	 */
	public Set<String> getTables(Connection db) throws SQLException {
		Set<String> tables = new HashSet<>();

		// Create statement and close when done.
		// Database connection will be closed elsewhere.
		try (Statement sql = db.createStatement();) {
			if (sql.execute("SHOW TABLES;")) {
				ResultSet results = sql.getResultSet();
				while (results.next()) {
					tables.add(results.getString(1));
				}
			}
		}
		return tables;
	}
	

}
