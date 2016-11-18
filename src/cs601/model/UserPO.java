package cs601.model;

/**
 * A class - represent the "users" table in database
 */

public class UserPO {
	
	private int userId;
	private String username;
	private String usersalt;
	private String password;
	
	public UserPO( int userId,String username, String usersalt, String hashedpw) {
		this.userId = userId;
		this.username = username;
		this.usersalt = usersalt;
		this.password = hashedpw;
	}

	
	// getters
	public int getUserId(){
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public String getUsersalt() {
		return usersalt;
	}

	public String getPassword() {
		return password;
	}

	
	/* setters
	public void setUsername(String username) {
		this.username = username;
	}

	public void setHasedSalt(String hasedSalt) {
		this.hasedSalt = hasedSalt;
	}

	public void setHasedPwSalt(String hasedPwSalt) {
		this.hasedPwSalt = hasedPwSalt;
	}
	
	*/
	
	
	

}
