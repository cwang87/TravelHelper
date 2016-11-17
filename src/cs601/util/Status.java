package cs601.util;

/**
 * A Status enum for tracking errors. 
 * Each Status enum type will use the ordinal as the error code. 
 * The Status ordinal is determined by its position in the list.(start from 0)
 */
public enum Status {

	OK("No errors occured."),
	ERROR("Unknown error occurred."),
	MISSING_CONFIG("Unable to find configuration file."),
	MISSING_VALUES("Missing values in configuration file."),
	CONNECTION_FAILED("Failed to establish a database connection."),
	CREATE_FAILED("Failed to create necessary tables."),
	INVALID_REGISTER("Invalid regiser information."),
	INVALID_LOGIN("Invalid username and/or password."),
	INVALID_USER("User does not exist."),
	DUPLICATE_USER("User with that username already exists."),
	SQL_EXCEPTION("Unable to execute SQL statement.");

	private final String message;
	
	//constructor
	private Status(String message) {
		this.message = message;
	}
	
	
	public String message() {
		return message;
	}

	
	@Override
	public String toString() {
		return this.message;
	}
}