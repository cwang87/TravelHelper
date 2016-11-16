package cs601.DBsetup;

import java.nio.file.Paths;
import java.sql.*;
import java.util.*;


import cs601.dao.BaseDAO;

/** A class - to create tables and load data to tables in the database. */

public class setupTables {
	
	ThreadSafeHotelData hotelData;
	HotelDataBuilder builder;
	BaseDAO dao;

	
	private static final String CREATE_HOTELS_SQL = "CREATE TABLE hotels (hotelId VARCHAR(32) PRIMARY KEY, "
			+ "hotelName VARCHAR(64) NOT NULL, city VARCHAR(32) NOT NULL, state VARCHAR(32), "
			+ "strAddr VARCHAR(255) NOT NULL, country VARCHAR(32) NOT NULL);";
	
	private static final String CREATE_REVIEWS_SQL = "CREATE TABLE reviews (hotelId VARCHAR(32) NOT NULL, "
			+ "reviewId VARCHAR(64) PRIMARY KEY, username VARCHAR(32) NOT NULL, reviewTitle VARCHAR(64) NOT NULL, "
			+ "reviewText TEXT NOT NULL, reviewDate TIMESTAMP DEFAULT NOW(), isRecomm TINYINT(1) NOT NULL, "
			+ "overallRating TINYINT(5) NOT NULL);";
	
	private final String CREATE_USERS_SQL = "CREATE TABLE users (userId INTEGER PRIMARY KEY AUTO_INCREMENT, "
			+ "username VARCHAR(32) NOT NULL UNIQUE, usersalt CHAR(32) NOT NULL, password CHAR(64) NOT NULL);";
	
	private final String CREATE_OLDUSERS_SQL = "CREATE TABLE oldusers (username VARCHAR(32) PRIMARY KEY, "
			+ "userId INTEGER NOT NULL);";
	
	
	private static final String INSERT_HOTELINFO_SQL ="INSERT INTO hotels (hotelId, hotelName, city, "
			+ "state, strAddr, country ) VALUE (?,?,?,?,?,?);";
	
	private static final String INSERT_REVIEWINFO_SQL ="INSERT INTO reviews (hotelId, reviewId, username, "
			+ "reviewTitle, reviewText, reviewDate, isRecomm, overallRating) VALUE(?,?,?,?,?,?,?,?);";
	
	
	
	
	/** default constructor, load hotel data to maps */
	public setupTables(){
		hotelData = new ThreadSafeHotelData();
		builder = new HotelDataBuilder(hotelData);
		builder.loadHotelInfo("input/hotels200.json");
		builder.loadReviews(Paths.get("input/reviews"));
		dao = new BaseDAO("localDB.properties");
	}
	
	/** constructor, load hotel data to maps */
	public setupTables(String config){
		hotelData = new ThreadSafeHotelData();
		builder = new HotelDataBuilder(hotelData);
		builder.loadHotelInfo("input/hotels200.json");
		builder.loadReviews(Paths.get("input/reviews"));
		dao = new BaseDAO(config);
	}
	
	
	
	
	
	
	
	/** method to load all hotelData in json file to database*/
	public void loadMaps2DB() {
		System.out.println("finished loading data into maps");

		createHotelDataTables();
		loadHotelsTable();
		loadReviewsTable();
	}
	

	


	
	/** to create hotels and reviews tables for database. */
	private void createHotelDataTables(){
		Connection connection = dao.createConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(CREATE_HOTELS_SQL);
			statement.executeUpdate(CREATE_REVIEWS_SQL);
			statement.executeUpdate(CREATE_USERS_SQL);
			statement.executeUpdate(CREATE_OLDUSERS_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	


	
	
	/** to insert all hotel information into database. */
	private void loadHotelsTable() {
		List<String> hotelList = hotelData.getHotels();
		for (String hotelId : hotelList) {
			String[] parameters = hotelData.getHotelParas(hotelId);
			dao.executeUpdate(INSERT_HOTELINFO_SQL, parameters);
		}
	}
	
	
	
	
	
	
	/** to insert all review records into database. */
	private void loadReviewsTable() {
		Connection connection = dao.createConnection();
		List<String> hotelList = hotelData.getHotels();

		List<String> checking = new ArrayList<>();
		for (String hotelId : hotelList) {
			if (!checking.contains(hotelId)) {
				checking.add(hotelId);
				hotelData.insertReviews(connection, INSERT_REVIEWINFO_SQL, hotelId);
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	

	
}


