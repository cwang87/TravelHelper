package cs601.sqlHelper;

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



/** load database configuration and connect to the database. */

public class DBConnector {
	
	/* URI to use when connecting to database. Should be in the format:jdbc:subprotocol://hostname/database */
	private final String uri;
	
	/* config file to be loaded */
	private final Properties login;
	
	
	
	/*-------------------------------------------Constructors-------------------------------------------------*/
	
	/** Create connector from the default config file - "database.properties" */
	
	public DBConnector() {
		
		// load configuration from file
		Properties config = null;
		try {
//			config = loadConfig("localDB.properties");
			config = loadConfig("database.properties");
		} catch (FileNotFoundException e) {
			System.out.println(Status.MISSING_CONFIG + ": " + e.getMessage());
		} catch (IOException e) {
			System.out.println(Status.MISSING_VALUES + ": " + e.getMessage());
		}
		
		// Create database URI in proper format
		uri = String.format("jdbc:mysql://%s/%s", config.getProperty("hostname"), config.getProperty("database"));
		System.out.println("uri = " + uri);
		
		// Create database login properties
		login = new Properties();
		login.put("user", config.getProperty("username"));
		login.put("password", config.getProperty("password"));
	}
	
	
	
	
	
	
	/** Create connector from a given config file */
	
	public DBConnector(String configPath) {
		
		Properties config = null;
		try {
			config = loadConfig(configPath);
		} catch (FileNotFoundException e) {
			System.out.println(Status.MISSING_CONFIG + ": " + e.getMessage());
		} catch (IOException e) {
			System.out.println(Status.MISSING_VALUES + ": " + e.getMessage());
		}
		
		uri = String.format("jdbc:mysql://%s/%s", config.getProperty("hostname"), config.getProperty("database"));
		System.out.println("uri = " + uri);
		
		login = new Properties();
		login.put("user", config.getProperty("username"));
		login.put("password", config.getProperty("password"));
	}

	
	
	/*----------------------------------Methods used to create a connection--------------------------------------*/

	/** A method - read database config file and load reqired info into an Properties instance. 
	 * If the info in config file is not complete, exception with info about which info is missed will be reported. 
	 * @return Properties instance
	 * */
	
	private Properties loadConfig(String configPath) throws FileNotFoundException, IOException {
		
		// Specify which keys must be in properties file
		Set<String> required = new HashSet<>();
		required.add("username");
		required.add("password");
		required.add("database");
		required.add("hostname");

		// Try to load the configuration from file
		Properties config = new Properties();
		config.load(new FileReader(configPath));

		// Check all the required keys are present
		if (!config.keySet().containsAll(required)) {
			throw new InvalidPropertiesFormatException("Must provide the following in properties file: " + required);
		}
		
		return config;
	}
	
	
	
	
	
	/** A method - attempts to connect to database. */
	
	/* DriverManager.getConnection(String url, Properties info); ("info" = user + password) */
	public Connection getConnection() throws SQLException {
		Connection dbConnection = DriverManager.getConnection(uri+"?useSSL=false", login);
		return dbConnection;
	}

	
	
	
	
	
	
	/*-------------------------------------------Test Connection-----------------------------------------------------*/
	
	/** Opens a database connection, executes a simple statement, and closes the database connection.*/
	
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
			System.err.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		return okay;
	}
	
	
	
	
	/** Opens a database connection and returns a set of found tables. 
	 * Will return an empty set if there are no results. 
	 * */
	
	public Set<String> getTables(Connection db) throws SQLException {
		
		Set<String> tables = new HashSet<>();

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
