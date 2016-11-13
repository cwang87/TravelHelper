package cs601.service;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.eclipse.jetty.http2.parser.DataBodyParser;

import cs601.dao.BaseDAO;
import cs601.util.Status;
import cs601.util.Tools;

/** A class - service all "users" table related operations. */

public class UserService {

	/**
	 * Eager Singleton - Makes sure only one UserService is instantiated to
	 * operate the "users" table.
	 */
	private static UserService singleton = new UserService();

	private BaseDAO dao;
	private Random random;

	// sql strings
	private static final String REGISTER_SQL = "INSERT INTO users (username, usersalt, password ) VALUES (?, ?, ?);";
//	private static final String DELETE_SQL = "DELETE FROM users WHERE username = ?";
	private static final String USER_SQL = "SELECT username FROM users WHERE username = ?";
	private static final String SALT_SQL = "SELECT usersalt FROM users WHERE username = ?";
	private static final String AUTH_SQL = "SELECT username FROM users WHERE username = ? AND password = ?";

	/**
	 * Private constructor to make this class is a singleton. Other classes need
	 * to call getInstance()
	 */
	private UserService() {
		dao = new BaseDAO();
		random = new Random(System.currentTimeMillis());
	}
	
	
	/**
	 * Gets the single instance of the database handler.
	 * @return instance of the database handler
	 */
	public static UserService getInstance() {
		return singleton;
	}

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Tests if a user already exists in the database.
	 * @param username to check
	 * @return Status.OK if user does not exist in database
	 */
	private Status existUser(String username) {

		Status status = Status.ERROR;
		ResultSet rSet = dao.executeQuery(USER_SQL, username);
		try {
			status = rSet.next() ? Status.DUPLICATE_USER : Status.OK;
		} catch (SQLException e) {
			status = Status.SQL_EXCEPTION;
			System.out.println("Exception occured while processing SQL statement:");
		}
		return status;
	}
	
	
	
	
	
	
	
	
	

	/**
	 * Registers a new user, placing the username, password hash, and salt into
	 * the database if the username does not already exist.
	 * @param newuse - username of new user
	 * @param newpass - password of new user
	 * @return {@link Status.OK} if registration successful
	 */
	public Status registerUser(String newuser, String newpass) {
		Status status = Status.ERROR;
		System.out.println("Registering " + newuser + ".");

		// check if username or password is empty
		if (Tools.isBlank(newuser) || Tools.isBlank(newpass)) {
			status = Status.INVALID_LOGIN;
			System.out.println("Invalid regiser info");
			return status;

			// check if the username is duplicate
		} else if (existUser(newuser) == Status.OK) {
			byte[] saltBytes = new byte[16]; // generate salt
			random.nextBytes(saltBytes);

			String usersalt = Tools.encodeHex(saltBytes, 32); // hash salt
			String passhash = getHash(newpass, usersalt); // combine password and salt, then hash again

			String[] parameters = { newuser, usersalt, passhash };
			dao.executeUpdate(REGISTER_SQL, parameters);
			status = Status.OK;
		}
	return status;
	}

	
	
	
	
	
	public Status authLogin(String existUser, String userpw){
		Status status = Status.ERROR;
		System.out.println("Loging " + existUser + ".");

		// check if leave any field blank
		if(Tools.isBlank(existUser) || Tools.isBlank(userpw)){
			status = Status.INVALID_LOGIN;
			System.out.println("Invalid login info");
			return status;
		}else if(existUser(existUser) != Status.DUPLICATE_USER){
			status = Status.INVALID_USER;
			return status;
		}else {
			String hashedPW = getHash(userpw, getSalt(existUser));
			String[] parameters = {existUser, hashedPW};
			ResultSet rSet = dao.executeQuery(AUTH_SQL, parameters);
			try {
				if (rSet.next()){
					status = Status.OK;
				}else{
					status = Status.INVALID_LOGIN;
				}
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
		
		}
		return status;
	}
	

	
	
	
	
	
	

	/**
	 * Calculates the hash of a password and salt using SHA-256.
	 * @param password to hash
	 * @param salt associated with user
	 * @return hashed password
	 */
	public static String getHash(String password, String salt) {
		String salted = salt + password;
		String hashed = salted;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salted.getBytes());
			hashed = Tools.encodeHex(md.digest(), 64);
		} catch (Exception ex) {
			System.out.println("Unable to properly hash password." + ex);
		}
		return hashed;
	}

	

	
	
	
	
	
	
	/**
	 * Gets the salt for a specific user 
	 * @param user - which user to retrieve salt for
	 * @return salt for the specified user or null if user does not exist
	 */
	private String getSalt(String user) {
		String salt = null;
		assert user != null;
		
		ResultSet rSet = dao.executeQuery(SALT_SQL, user);
		try {
			if(rSet.next()){
				salt = rSet.getString("usersalt");
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}

		return salt;
	}
}
