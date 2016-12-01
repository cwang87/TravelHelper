package cs601.tablesHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs601.controller.main.JettyServer;
import cs601.sqlHelper.SqlHelper;
import cs601.tableData.HotelAveRate;
import cs601.tableData.HotelPO;
import cs601.util.Status;

public class HotelsHandler {

	private static HotelsHandler hotelsHandler = new HotelsHandler();
	
	
	private static final String SEARCH_ONEHOTELS_WITH_AVG = "SELECT hotels.hotelId, hotelName,"
			+ " city, state, strAddr, country, AVG(overallRating) AS aveRating "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "WHERE hotelId=? "
			+ "GROUP BY hotels.hotelId;"; 
	
	
	private static final String SEARCH_ALLHOTELS_WITH_AVG = "SELECT hotels.hotelId, hotelName,"
			+ " city, state, strAddr, country, AVG(overallRating) AS aveRating "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "GROUP BY hotels.hotelId;"; 
	
	private static final String SEARCH_HOTELS_BYCITY_WITH_AVG = "SELECT hotels.hotelId, hotelName, "
			+ "city, state, strAddr, country, AVG(overallRating) AS aveRating "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "WHERE city=? AND state=? "
			+ "GROUP BY hotels.hotelId;";
	
	private static final String SEARCH_HOTELS_BYNAME_WITH_AVG = "SELECT hotels.hotelId, hotelName, "
			+ "city, state, strAddr, country, AVG(overallRating) AS aveRating "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "WHERE hotelName Like ? "
			+ "GROUP BY hotels.hotelId;";
	
	private static final String SEARCH_ALLHOTELS = "SELECT * FROM hotels;";
	
	private static final String AVG_RATING = "SELECT AVG(overallRating) AS avgRating FROM reviews WHERE hotelId = ?;";
	
	private static final String HAS_REVIEW_HOTELID = "SELECT hotelId FROM reviews WHERE hotelId = ?;";
	
	private static final String HAS_REVIEW_USERNAME = "SELECT username FROM reviews WHERE username = ?;";
	

	
	
	private HotelsHandler() {
	}
	
	
	
	
	/*-----------------------------------------get Singleton Instance------------------------------------------*/
	
	/** Gets the single instance of the database handler. */
	
	public static HotelsHandler getInstance() {
		return hotelsHandler;
	}
	
	
	
	/*----------------------------------Get HotelsList by different search criterias-----------------------------*/
	
	/**
	 * A method to get a full list of hotels
	 * If there's no review, "no reveiews" will be shown.
	 * Write the info in resultset into javascript table.
	 */
	public List<HotelAveRate> getHotelsFull(){
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_ALLHOTELS_WITH_AVG);
		
		List<HotelAveRate> hotelList = parseHotelListResultSet(rs);
		
		SqlHelper.close(SqlHelper.getRs(), SqlHelper.getSt(), SqlHelper.getCt());
		
		return hotelList;
	}
	
	
	/**
	 * A method to get a full list of hotels given a particular city/state
	 * If there's no review, "no reveiews" will be shown.
	 * Write the info in resultset into javascript table.
	 */
	public List<HotelAveRate> getHotelsCity(String citySearch, String stateSearch){
		
		String[] parameters = {citySearch, stateSearch};
		ResultSet rs = SqlHelper.executeQuery(SEARCH_HOTELS_BYCITY_WITH_AVG, parameters);
		
		List<HotelAveRate> hotelList = parseHotelListResultSet(rs);
		
		SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		
		return hotelList;
	}
	
	
	/**
	 * A method to get a full list of hotels given a hotelName
	 * If there's no review, "no reveiews" will be shown.
	 * Write the info in resultset into javascript table.
	 */
	
	public List<HotelAveRate> getHotelsName(String name){
		
		String nameLike = "%" + name + "%";
		ResultSet rs = SqlHelper.executeQuery(SEARCH_HOTELS_BYNAME_WITH_AVG, nameLike);
		
		List<HotelAveRate> hotelList = parseHotelListResultSet(rs);
		
		SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		
		return hotelList;
	}
	
	
	
	
	
	/* method used to parse the ResultSet*/
	private List<HotelAveRate> parseHotelListResultSet(ResultSet rs){
		List<HotelAveRate> hotelList = new ArrayList<>();
		try {
			while(rs.next()){
				String hotelId = rs.getString(1);
				String hotelName = rs.getString(2);
				String city = rs.getString(3);
				String state = rs.getString(4);
				String strAddr = rs.getString(5);
				String country = rs.getString(6);
				String aveRating = "no reviews";
				if(rs.getObject(7) != null){
					aveRating = Double.toString(rs.getDouble(7));
				}
				String hotelAddr = strAddr + ", " + city + ", " + state + ", " + country;
				
				HotelAveRate hotel = new HotelAveRate(hotelId, hotelName, hotelAddr, aveRating);
				hotelList.add(hotel);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}
		
		return hotelList;
	}
	
	
	
	/**
	 * A method - join hotels and reviews to get a full list of hotels and average rating respectively.
	 * If there's no review, "no reveiews" will be shown.
	 * Write the info in resultset into javascript table.
	 */
	public String getHotels_Select(){
		
		StringBuffer sb = new StringBuffer();
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_ALLHOTELS);
		
		try {
			while(rs.next()){
				String hotelId = rs.getString(1);
				String hotelName = rs.getString(2);
				String city = rs.getString(3);
				String state = rs.getString(4);
				String strAddr = rs.getString(5);
				String country = rs.getString(6);
				
				String hotelAddr = strAddr + ", " + city + ", " + state + ", " + country;
				
				String oneHotel = getOneHotel_Select(hotelId, hotelName, hotelAddr);
				sb.append(oneHotel);
			}
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getSt(), SqlHelper.getCt());
		}
		
		return sb.toString();
	}
	
	
	
	
	
	/* add info into javascript table, hotelname with hyperlinks directing user to add review page */
	
	private String getOneHotel_Select(String hotelId, String hotelName, String hotelAddr){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + hotelId + "</td>");
		
		sb.append("<td>");
		sb.append("<a href=\"http://localhost:" + JettyServer.PORT + "/user/add_review?hotelId=" + hotelId + "\">");
		sb.append(hotelName + "</a></td>");
		
		sb.append("<td>" + hotelAddr + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
		
	}
	
	
	
	
	/*--------------------------------------------query about reviews-------------------------------------------*/
	
	
	/** check if a given hotel has reviews */
	public boolean hasReviewHotelId(String hotelId){
		boolean hasReview = false;
		
		ResultSet rs = SqlHelper.executeQuery(HAS_REVIEW_HOTELID, hotelId);
		
		try {
			while(rs.next()){
				hasReview = true;
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hasReview;
	}
	
	
	
	
	
	
	
	/** check if a given username has reviews */
	public boolean hasReviewUsername(String username){
		boolean hasReview = false;
		
		ResultSet rs = SqlHelper.executeQuery(HAS_REVIEW_USERNAME, username);
		
		try {
			while(rs.next()){
				hasReview = true;
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hasReview;
	}
	
	
	
	
	
	
	
	/** given hotelId, return an instance of HotelAveRate which stores all info about this hotel in table hotels */
	public HotelPO getHotelPO(String hotelId){
		
		HotelPO hotelPO = null;
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_HOTEL, hotelId);
		try {
			while(rs.next()){
				String hId = rs.getString(1);
				String hotelName = rs.getString(2);
				String city = rs.getString(3);
				String state = rs.getString(4);
				String strAddr = rs.getString(5);
				String country = rs.getString(6);
				double lat = rs.getDouble(7);
				double lon = rs.getDouble(8);
				hotelPO = new HotelPO(hId, hotelName, city, state, strAddr, country, lat, lon);
			}
		}catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hotelPO;
	}
	
	
	
	
	
	
	
	/** given hotelId, return the average rating based on all reviews of it in db */
	public double getAvgRating(String hotelId){
		
		ResultSet rs = SqlHelper.executeQuery(AVG_RATING, hotelId);
		
		try {
			while(rs.next()){
				double avgRating = rs.getDouble(1);
				return avgRating;
			}
			return 0;
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			return 0;
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
}
