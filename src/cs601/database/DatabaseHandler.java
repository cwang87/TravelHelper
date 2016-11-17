package cs601.database;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.webapp.DiscoveredAnnotation;

import cs601.dao.DAO;
import cs601.dao.DBConnector;
import cs601.hotelapp.HotelDataBuilder;
import cs601.hotelapp.ThreadSafeHotelData;
import cs601.service.UserService;
import cs601.util.Status;

/**
 * Handles all database-related actions, including create tables, load hotel info and reviews into database.
 * Uses singleton design pattern.
 */

public class DatabaseHandler {

	/* Makes sure only one database handler is instantiated. */
	private static DatabaseHandler dbhandler = new DatabaseHandler();
	
	private static ThreadSafeHotelData hotelData;
	private static HotelDataBuilder builder;
	
	
	
	/*-------------------------------------------Create Tables-------------------------------------------------*/
	private static final String CREATE_USERS_SQL = "CREATE TABLE users (userId INTEGER PRIMARY KEY AUTO_INCREMENT, "
			+ "username VARCHAR(32) NOT NULL UNIQUE, usersalt CHAR(32) NOT NULL, password CHAR(64) NOT NULL);";
	
	private static final String CREATE_HOTELS_SQL = "CREATE TABLE hotels (hotelId VARCHAR(32) PRIMARY KEY, "
			+ "hotelName VARCHAR(64) NOT NULL, city VARCHAR(32) NOT NULL, state VARCHAR(32), "
			+ "strAddr VARCHAR(255) NOT NULL, country VARCHAR(32) NOT NULL, "
			+ "lat DOUBLE NOT NULL, lon DOUBLE NOT NULL);";
	
	private static final String CREATE_REVIEWS_SQL = "CREATE TABLE reviews (hotelId VARCHAR(32) NOT NULL, "
			+ "reviewId VARCHAR(64) PRIMARY KEY, username VARCHAR(32) NOT NULL, reviewTitle VARCHAR(64) NOT NULL, "
			+ "reviewText TEXT NOT NULL, reviewDate TIMESTAMP DEFAULT NOW(), isRecom TINYINT(1) NOT NULL, "
			+ "overallRating TINYINT(5) NOT NULL);";
	
	
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
	
	/** to create tables for database. */
	public Status createUsers(){
		Status status = Status.CREATE_FAILED;
		if(DAO.executeUpdate(CREATE_USERS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	public Status createHotels(){
		Status status = Status.CREATE_FAILED;
		if(DAO.executeUpdate(CREATE_HOTELS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	public Status createReviews(){
		Status status = Status.CREATE_FAILED;
		if(DAO.executeUpdate(CREATE_REVIEWS_SQL) ){
			status = Status.OK;
		}
		return status;
	}
	
	
	/*-------------------------------------------Load Hotel Data into tables-------------------------------------*/
	
	/** load hotel info into table "hotels" */
	public void loadHotelsTable() {
		
		List<String> hotelList = hotelData.getHotels();
		
		Connection ct = DAO.getConnection();
		
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
		
		Connection ct = DAO.getConnection();

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
	
	/** register users in json files with uniform password "123456" */
	public void registerZusers(){
		
		UserService userService = UserService.getInstance();
		
		String password = "123456";
		Set<String> username = hotelData.getUsernames();
		
		Iterator<String> itr = username.iterator();
		
		while(itr.hasNext()){
			userService.registerUser(itr.next(), password);
		}
		
	}
	
	
	/*--------------------------------add and update userId column to table reviews --------------------------*/
	
	/** add userId column to reviews table */
	public void addUserIdCol(){
		
		String sql = "ALTER TABLE reviews ADD userId INTEGER NOT NULL;";
		DAO.executeUpdate(sql);
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
				
				DAO.executeUpdate(sql);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}finally {
			DAO.close(DAO.getRs(), DAO.getSt(), DAO.getCt());
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
