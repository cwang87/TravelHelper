package cs601.model;

/**
 * A class - represent the "users" table in database
 */

public class UserPO {
	
	private int userId;
	private String username;
	private String usersalt;
	private String password;
	
	
	
	/**
	 * Constructor
	 * 
	 * @param userId
	 * @param username
	 * @param usersalt
	 * @param hashedpw
	 */
	
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

	
	
	

}
