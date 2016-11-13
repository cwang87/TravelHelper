package cs601.model.PO;

/**
 * A class - represent the "users" table in database
 */

public class User {
	
	private String username;
	private String hasedSalt;
	private String hasedPwSalt;
	
	public User(String username, String hasedSalt, String hasedPwSalt) {
		this.username = username;
		this.hasedSalt = hasedSalt;
		this.hasedPwSalt = hasedPwSalt;
	}

	
	// getters
	public String getUsername() {
		return username;
	}

	public String getHasedSalt() {
		return hasedSalt;
	}

	public String getHasedPwSalt() {
		return hasedPwSalt;
	}

	
	// setters
	public void setUsername(String username) {
		this.username = username;
	}

	public void setHasedSalt(String hasedSalt) {
		this.hasedSalt = hasedSalt;
	}

	public void setHasedPwSalt(String hasedPwSalt) {
		this.hasedPwSalt = hasedPwSalt;
	}
	
	
	
	
	

}
