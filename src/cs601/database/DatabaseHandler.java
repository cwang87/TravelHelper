package cs601.database;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cs601.sqlHelper.SqlHelper;
import cs601.tablesHandler.UsersHandler;
import cs601.hotelData.HotelDataBuilder;
import cs601.hotelData.ThreadSafeHotelData;
import cs601.sqlHelper.DBConnector;
import cs601.util.Status;

/**
 * Handles all database-related actions, including create tables, load hotel info and reviews into database.
 * Uses singleton design pattern.
 */

public class DatabaseHandler {

	/* Makes sure only one database handler is instantiated. */
	private static DatabaseHandler dbhandler = new DatabaseHandler();
	
	private static UsersHandler usersHandler = UsersHandler.getInstance();
	
	private static ThreadSafeHotelData hotelData;
	private static HotelDataBuilder builder;
	
	
	
	/*-------------------------------------------Create Tables-------------------------------------------------*/
	private static final String CREATE_USERS_SQL = "CREATE TABLE users (userId INTEGER PRIMARY KEY AUTO_INCREMENT, "
			+ "username VARCHAR(32) NOT NULL UNIQUE, usersalt CHAR(32) NOT NULL, password CHAR(64) NOT NULL, "
			+ "lastVisit TIMESTAMP NULL);";
	
	private static final String CREATE_HOTELS_SQL = "CREATE TABLE hotels (hotelId VARCHAR(32) PRIMARY KEY, "
			+ "hotelName VARCHAR(64) NOT NULL, city VARCHAR(32) NOT NULL, state VARCHAR(32), "
			+ "strAddr VARCHAR(255) NOT NULL, country VARCHAR(32) NOT NULL, "
			+ "lat DOUBLE NOT NULL, lon DOUBLE NOT NULL);";
	
	private static final String CREATE_REVIEWS_SQL = "CREATE TABLE reviews (hotelId VARCHAR(32) NOT NULL, "
			+ "reviewId VARCHAR(64) PRIMARY KEY, username VARCHAR(32) NOT NULL, reviewTitle VARCHAR(64) NOT NULL, "
			+ "reviewText TEXT NOT NULL, reviewDate TIMESTAMP DEFAULT NOW(), isRecom TINYINT(1) NOT NULL, "
			+ "overallRating TINYINT(5) NOT NULL);";
	
	private static final String CREATE_LIKEDREVIEWS_SQL = "CREATE TABLE likedReviews (username VARCHAR(32) NOT NULL, "
			+ "reviewId VARCHAR(64) NOT NULL);";
	
	private static final String CREATE_EXPEDIAHISTORY_SQL = "CREATE TABLE expediaHistory (username VARCHAR(32) NOT NULL, "
			+ "hotelName VARCHAR(64) NOT NULL, hotelId VARCHAR(32) NOT NULL);";
	
	private static final String CREATE_SAVEDHOTELS_SQL = "CREATE TABLE savedHotels (username VARCHAR(32) NOT NULL, "
			+ "hotelName VARCHAR(64) NOT NULL, hotelId VARCHAR(32) NOT NULL);";
	
	
	
	/*-------------------------------------------Load Hotel Data into tables-------------------------------------*/
	private static final String INSERT_HOTELINFO_SQL ="INSERT INTO hotels (hotelId, hotelName, city, "
			+ "state, strAddr, country, lat, lon ) VALUE (?,?,?,?,?,?,?,?);";
	
	private static final String INSERT_REVIEWINFO_SQL ="INSERT INTO reviews (hotelId, reviewId, username, "
			+ "reviewTitle, reviewText, reviewDate, isRecom, overallRating) VALUE(?,?,?,?,?,?,?,?);";
	
	
	
	
	
	
	/* private constructor to ensure singleton*/
	private DatabaseHandler(){
		hotelData = new ThreadSafeHotelData();
		builder = new HotelDataBuilder(hotelData);
		builder.loadHotelInfo("input/hotels200.json");
		builder.loadReviews(Paths.get("input/reviews"));
		System.out.println("Finished loading hotelData!");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/** Gets the single instance of the database handler.*/
	public static DatabaseHandler getInstance(){
		return dbhandler;
	}
	

	
	/*-------------------------------------------Create Tables-------------------------------------------------*/
	
	/** create table - users */
	public Status createUsers(){
		Status status = Status.CREATE_FAILED;
		if(SqlHelper.executeUpdate(CREATE_USERS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	
	/** create table - hotels */
	public Status createHotels(){
		Status status = Status.CREATE_FAILED;
		if(SqlHelper.executeUpdate(CREATE_HOTELS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	

	/** create table - reviews */
	public Status createReviews(){
		Status status = Status.CREATE_FAILED;
		if(SqlHelper.executeUpdate(CREATE_REVIEWS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	/** create table - likedReviews */
	public Status createLikedReviews(){
		Status status = Status.CREATE_FAILED;
		if(SqlHelper.executeUpdate(CREATE_LIKEDREVIEWS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	
	/** create table - expediaHistory */
	public Status createExpediaHistory(){
		Status status = Status.CREATE_FAILED;
		if(SqlHelper.executeUpdate(CREATE_EXPEDIAHISTORY_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	
	/** create table - savedHotels */
	public Status createSavedHotels(){
		Status status = Status.CREATE_FAILED;
		if(SqlHelper.executeUpdate(CREATE_SAVEDHOTELS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	/*-------------------------------------------Load Hotel Data into tables-------------------------------------*/
	
	/** load hotel info into table "hotels" */
	public void loadHotelsTable() {
		
		List<String> hotelList = hotelData.getHotels();
		
		Connection ct = SqlHelper.getConnection();
		
		for (String hotelId : hotelList) {
			hotelData.writeHotel(ct, INSERT_HOTELINFO_SQL, hotelId);
		}
		try {
			ct.close();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
	}
	
	
	
	
	
	
	/** load review info into table "reviews" */
	public void loadReviewsTable() {
		
		List<String> hotelList = hotelData.getHotels();
		
		//used to check duplicate hotelIds
		List<String> checking = new ArrayList<>();
		
		Connection ct = SqlHelper.getConnection();

		for (String hotelId : hotelList) {
			if (!checking.contains(hotelId)) {
				checking.add(hotelId);
				hotelData.writeReview(ct, INSERT_REVIEWINFO_SQL, hotelId);
			}
		}
		try {
			ct.close();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}

	}
	
	
	
	/*-------------------------------------------register zombie users-------------------------------------*/
	
	/** register users(except for the anonymous ones) in json files with password in the following form:
	 * "username + 123456 + *(special character)"
	 */
	public void registerZusers(){
		
		String password = "123456*";
		Set<String> username = hotelData.getUsernames();
		
		for(String name: username){
			if(name != "anonymous"){
				usersHandler.registerUser(name, name+password);
			}
		}
	}
	
	
	/*--------------------------------add and update userId column to table reviews --------------------------*/
	
	/** add userId column to reviews table */
	public void addUserIdCol(){
		
		String sql = "ALTER TABLE reviews ADD userId INTEGER NOT NULL;";
		SqlHelper.executeUpdate(sql);
	}
	
	/** add likeCount column to reviews table */
	public void addLikeCountCol(){
		String sql = "ALTER TABLE reviews ADD likeCount INTEGER DEFAULT 0;";
		SqlHelper.executeUpdate(sql);
	}
	
	
	/** update userId info in reviews table */
	public void updateUserId(){
	
		String join = "SELECT reviewId, users.userId FROM reviews INNER JOIN users ON reviews.username = users.username;";

		try {
			DBConnector connector = new DBConnector();
			Connection connection = connector.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery(join);
		
			while (rSet.next()) {
				String reviewId = rSet.getString(1);
				int userid = rSet.getInt(2);
				
				String userId = Integer.toString(userid);
				String sql = prepareUserIdSQL(userId, reviewId);
				
				SqlHelper.executeUpdate(sql);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getSt(), SqlHelper.getCt());
		}
	}
	
	
	/* prepare updateSQL statement */
	private String prepareUserIdSQL(String userId, String reviewId){
		String p1 = "UPDATE reviews SET userId =";
		String p2 = " WHERE reviewId = '";
		String p3 = "';";
		String sql = p1 + userId + p2 + reviewId + p3;
		
		return sql;
	}
	
	
	
	
	
	
	
}
