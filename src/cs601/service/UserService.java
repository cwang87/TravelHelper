package cs601.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


import cs601.dao.DAO;
import cs601.util.Status;
import cs601.util.Tools;

/**
 * Handles all database-related actions for Table "users", including register and query.
 * Uses singleton design pattern.
 */

public class UserService {

	private static UserService userService = new UserService();

	private Random random;

	
	private static final String REGISTER_SQL = "INSERT INTO users (username, usersalt, password) VALUES (?, ?, ?);";
//	private static final String DELETE_SQL = "DELETE FROM users WHERE username = ?";
	private static final String USER_SQL = "SELECT username FROM users WHERE username = ?";
//	private static final String SALT_SQL = "SELECT usersalt FROM users WHERE username = ?";
//	private static final String AUTH_SQL = "SELECT username FROM users WHERE username = ? AND password = ?";

	private UserService() {
		random = new Random(System.currentTimeMillis());
	}
	
	
	
	
	
	/** Gets the single instance of the database handler. */
	public static UserService getInstance() {
		return userService;
	}

	
	/*-------------------------------------------Register users-------------------------------------*/
	
	/**
	 * Registers a new user, placing the username, password hash, and salt into
	 * the database if the username does not already exist.
	 */
	
	public Status registerUser(String newuser, String newpass) {
		
		Status status = Status.ERROR;
		
//		System.out.println("Registering " + newuser + ".");

		// make sure users have input both username and password while registering
		if (Tools.isBlank(newuser) || Tools.isBlank(newpass)) {
			status = Status.INVALID_REGISTER;
			System.out.println(status);
			return status;
		}
		// check if the username is already exist
		
		Connection ct = DAO.getConnection(); 
		status = duplicateUser(ct, newuser);
			
		if (status == Status.OK) {
			
			// generate salt
			byte[] saltBytes = new byte[16]; 
			random.nextBytes(saltBytes);

			// get hashed salt
			String usersalt = Tools.encodeHex(saltBytes, 32); 
			
			// get hashed password
			String passhash = Tools.getHash(newpass, usersalt); 

			String[] parameters = {newuser.toLowerCase(), usersalt, passhash};
			DAO.executeUpdate(REGISTER_SQL, parameters);
			status = Status.OK;
		}

		// close the connection
		try {
			ct.close();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		
		return status;
	}
	
	
	
	
	
	/** Tests if a user already exists in the database. */
	private Status duplicateUser(Connection connection, String user) {

		assert connection != null;
		assert user != null;

		Status status = Status.ERROR;

		try (PreparedStatement statement = connection.prepareStatement(USER_SQL);) {
			statement.setString(1, user);

			ResultSet results = statement.executeQuery();
			status = results.next() ? Status.DUPLICATE_USER : Status.OK;
		} catch (SQLException e) {
			status = Status.SQL_EXCEPTION;
			System.out.println("Exception occured while processing SQL statement:" + e);
		}

		return status;
	}
	
	
	
	



	
	
	
	
//	
//	public Status authLogin(String existUser, String userpw){
//		Status status = Status.ERROR;
//		System.out.println("Loging " + existUser + ".");
//
//		// check if leave any field blank
//		if(Tools.isBlank(existUser) || Tools.isBlank(userpw)){
//			status = Status.INVALID_LOGIN;
//			System.out.println("Invalid login info");
//			return status;
//		}else if(existUser(existUser) != Status.DUPLICATE_USER){
//			status = Status.INVALID_USER;
//			return status;
//		}else {
//			String hashedPW = getHash(userpw, getSalt(existUser));
//			String[] parameters = {existUser, hashedPW};
//			ResultSet rSet = dao.executeQuery(AUTH_SQL, parameters);
//			try {
//				if (rSet.next()){
//					status = Status.OK;
//				}else{
//					status = Status.INVALID_LOGIN;
//				}
//			} catch (SQLException e) {
//				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
//			}
//		
//		}
//		return status;
//	}
//	
//
//	
//	
//	
//	
//	
//	
//
//	
//
//	
//
//	
//	
//	
//	
//	
//	
//	/**
//	 * Gets the salt for a specific user 
//	 * @param user - which user to retrieve salt for
//	 * @return salt for the specified user or null if user does not exist
//	 */
//	private String getSalt(String user) {
//		String salt = null;
//		assert user != null;
//		
//		ResultSet rSet = dao.executeQuery(SALT_SQL, user);
//		try {
//			if(rSet.next()){
//				salt = rSet.getString("usersalt");
//			}
//		} catch (SQLException e) {
//			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
//		}
//
//		return salt;
//	}
	
}
