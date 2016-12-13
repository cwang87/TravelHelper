package cs601.tablesHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs601.sqlHelper.SqlHelper;
import cs601.util.Status;
import cs601.util.Tools;

/**
 * A class - contains methods to manipulate data from users table in mySql. 
 */

public class UsersHandler {

	private static UsersHandler usersHandler = new UsersHandler();

	private Random random;

	/*------------------------------------------------Update SQL---------------------------------------------*/
	
	private static final String REGISTER_SQL = "INSERT INTO users (username, usersalt, password) VALUES (?, ?, ?);";
	
	private static final String UPDATE_LASTVISIT_SQL = "UPDATE users SET lastVisit = ? WHERE username = ?;";
	
	/*---------------------------------------------Query SQL---------------------------------------------------*/
	
	private static final String AUTH_SQL = "SELECT username FROM users WHERE username = ? AND password = ? ;";
	
	private static final String USERID_SQL = "SELECT userId FROM users WHERE username = ? ;";
	
	private static final String USER_SQL = "SELECT username FROM users WHERE username = ? ;";
	
	private static final String SALT_SQL = "SELECT usersalt FROM users WHERE username = ? ;";
	
	private static final String LASTVISIT_SQL = "SELECT lastVisit FROM users WHERE username = ?";
	

	
	
	
	
	/* private constructor to keep singleton */
	private UsersHandler() {
		random = new Random(System.currentTimeMillis());
	}
	
	
	
	
	
	/*-----------------------------------------get Singleton Instance------------------------------------------*/
	
	/** Gets the single instance of the database handler. */
	public static UsersHandler getInstance() {
		return usersHandler;
	}

	
	
	
	
	/*---------------------------------------------Register users--------------------------------------------------*/
	
	/**
	 * Registers a new user, placing the username, password hash, and salt into
	 * the database if the username does not already exist.
	 */
	
	public Status registerUser(String newuser, String newpass) {
		
		Status status = Status.ERROR;
		
		/* make sure users have input both username and password while registering */
		if (Tools.isBlank(newuser) || Tools.isBlank(newpass)) {
			status = Status.INVALID_REGISTER;
			System.out.println(status);
			return status;
		}
		
		/* check duplicate users */
		status = duplicateUser(newuser);
		if(status != Status.OK){
			return status;
		}
		
		/* check whether the input password meet our set requirements */
		status = validatePassword(newpass);
		if(status != Status.OK){
			return status;
		}
		
		/* generate hashed password+salt, and store user's info in database */
		// generate salt - a random byte array with length of 16
		byte[] saltBytes = new byte[16];
		random.nextBytes(saltBytes);

		// encode the salt into 32-length-hex String
		String usersalt = Tools.encodeHex(saltBytes, 32);

		// get hashed password (encoded salt + user-input pw)
		String passhash = Tools.getHash(newpass, usersalt);

		String[] parameters = { newuser.toLowerCase(), usersalt, passhash };
		SqlHelper.executeUpdate(REGISTER_SQL, parameters);
		status = Status.OK;

		
		return status;
	}
	
	
	
	
	/** check if a given password meet our set requirements. */
	public Status validatePassword(String newpass){
		Status status = Status.ERROR;
		
		newpass = newpass.trim();
		
		//check the requirements: at least 6 characters, at least one digit and one special character
		String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=~*])(?=\\S+$).{6,}$";
		
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(newpass);
		if(matcher.matches()){
			status = Status.OK;
			return status;
		}else{
			status = Status.INVALID_PASSWORD;
			return status;
		}
	}
	
	/*---------------------------------------------Login users--------------------------------------------------*/
	

	
	/** login a user if both username and password match the record in users table */
	
	public Status loginUser(String username, String password){
		Status status = Status.ERROR;
		username = username.trim();
		password = password.trim();

		// check if leave any field blank
		if(Tools.isBlank(username) || Tools.isBlank(password)){
			status = Status.INVALID_LOGIN;
			return status;
			
		// check if the user exists
		}else if(duplicateUser(username) != Status.DUPLICATE_USER){
			status = Status.INVALID_USER;
			return status;
			
		}else {
			String hashedPW = Tools.getHash(password, getSalt(username));
			String[] parameters = {username, hashedPW};
			ResultSet rs = SqlHelper.executeQuery(AUTH_SQL, parameters);
			try {
				if (rs.next()){
					status = Status.OK;
				}else{
					status = Status.INVALID_LOGIN;
				}
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}finally {
				SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
			}
			return status;
		}
		
	}
	
	
	/*---------------------------------------check whether a given user already exist-------------------------------*/	
	
	/** check if a given user already exists in the database. */
	private Status duplicateUser(String user) {

		Status status = Status.ERROR;
		
		ResultSet rs = SqlHelper.executeQuery(USER_SQL, user.trim().toLowerCase());
		
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
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
			
		return status;
	}
	
	
	
	/*---------------------------------------get info from "users" with query SQL-------------------------------*/
	
	public int getUserId(String username){
		
		int userId = 0;
		
		ResultSet rs = SqlHelper.executeQuery(USERID_SQL, username.trim().toLowerCase());
		try {
			if(rs.next()){
				userId = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			e.printStackTrace();
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return userId;
	}
	
	
	

	
	/** Gets the salt for a specific user */
	private String getSalt(String user) {
		
		String salt = null;
		
		ResultSet rs = SqlHelper.executeQuery(SALT_SQL, user.trim().toLowerCase());
		
		try {
			if(rs.next()){
				salt = rs.getString("usersalt");
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}

		return salt;
	}

	
	/** Gets lastVist for a specific user */
	public Date getLastVisit(String username){
		ResultSet rs = SqlHelper.executeQuery(LASTVISIT_SQL, username);
		Date lastVisit = null;
		try {
			if(rs.next()){
				lastVisit = rs.getTimestamp(1);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return lastVisit;
	}
	
	
	/*---------------------------------------update visit info-------------------------------*/	
	
	/** update lastVisit info */
	public Status updateVisitDate(Date visitDateTime, String username){
		Status status = Status.ERROR;
		Connection ct = SqlHelper.getConnection();
		
		PreparedStatement ps = null;
		try {
			ps = ct.prepareStatement(UPDATE_LASTVISIT_SQL);
			ps.setTimestamp(1, Tools.toTimestamp(visitDateTime));
			ps.setString(2, username);
			ps.executeUpdate();
			status = Status.OK;
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			status = Status.SQL_EXCEPTION;
		} finally {
			try {
				ps.close();
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			
		}
		
		return status;
	}
	
	

	
	
	
	
	
	
	
	
	
	
}
