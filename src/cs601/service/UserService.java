package cs601.service;

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

	/*---------------------------------------------Register users--------------------------------------------------*/
	
	private static final String REGISTER_SQL = "INSERT INTO users (username, usersalt, password) VALUES (?, ?, ?);";
	private static final String USER_SQL = "SELECT username FROM users WHERE username = ?";
	
	
	/*---------------------------------------------Login users--------------------------------------------------*/
	
	private static final String SALT_SQL = "SELECT usersalt FROM users WHERE username = ? ;";

	private static final String AUTH_SQL = "SELECT username FROM users WHERE username = ? AND password = ? ;";

	private static final String SEARCH_USERID_SQL = "SELECT userId FROM users WHERE username = ? ;";


	
	
	
	private UserService() {
		random = new Random(System.currentTimeMillis());
	}
	
	
	
	
	
	
	
	/** Gets the single instance of the database handler. */
	public static UserService getInstance() {
		return userService;
	}

	
	
	public int getUserId(String username){
		
		int userId = 0;
		
		ResultSet rs = DAO.executeQuery(SEARCH_USERID_SQL, username);
		try {
			if(rs.next()){
				userId = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			e.printStackTrace();
		}finally {
			DAO.close(DAO.getRs(), DAO.getPs(), DAO.getCt());
		}
		
		return userId;
	}
	
	
	
	
	
	/*---------------------------------------------Register users--------------------------------------------------*/
	
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
		
		status = duplicateUser(newuser);
			
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

		
		return status;
	}
	
	
	
	
	
	/** Tests if a user already exists in the database. */
	private Status duplicateUser(String user) {

		Status status = Status.ERROR;
		
		ResultSet rs = DAO.executeQuery(USER_SQL, user);
		
		try {
			if(rs.next()){
				status = Status.DUPLICATE_USER;
			}else{
				status = Status.OK;
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			e.printStackTrace();
		}finally {
			DAO.close(DAO.getRs(), DAO.getPs(), DAO.getCt());
		}
			
		return status;
	}
	
	
	/*---------------------------------------------Login users--------------------------------------------------*/
	

	
	/** login a user if both username and password match the record in users table */
	
	public Status loginUser(String username, String password){
		Status status = Status.ERROR;
		System.out.println("Logging in " + username + ".");

		// check if leave any field blank
		if(Tools.isBlank(username) || Tools.isBlank(password)){
			status = Status.INVALID_LOGIN;
			System.out.println("Invalid login info");
			return status;
			
		// check if leave any field blank	
		}else if(duplicateUser(username) != Status.DUPLICATE_USER){
			status = Status.INVALID_USER;
			return status;
			
		}else {
			String hashedPW = Tools.getHash(password, getSalt(username));
			String[] parameters = {username, hashedPW};
			ResultSet rs = DAO.executeQuery(AUTH_SQL, parameters);
			try {
				if (rs.next()){
					status = Status.OK;
				}else{
					status = Status.INVALID_LOGIN;
				}
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}finally {
				DAO.close(DAO.getRs(), DAO.getPs(), DAO.getCt());
			}
			return status;
		}
		
	}
	

	
	/** Gets the salt for a specific user */
	private String getSalt(String user) {
		
		String salt = null;
		
		ResultSet rs = DAO.executeQuery(SALT_SQL, user);
		
		try {
			if(rs.next()){
				salt = rs.getString("usersalt");
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			DAO.close(DAO.getRs(), DAO.getPs(), DAO.getCt());
		}

		return salt;
	}

	
	
}
