package cs601.tablesHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs601.model.HotelPO;
import cs601.sqlHelper.SqlHelper;
import cs601.util.Status;

public class HotelsHandler {

	private static HotelsHandler hotelService = new HotelsHandler();
	private static List<String> hotelList;
	
	
	private static final String SEARCH_HOTEL = "SELECT * FROM hotels WHERE hotelId = ?;";
	
	private static final String SEARCH_HOTELID_COLUMN = "SELECT hotelId FROM hotels;";
	
	private static final String AVG_RATING = "SELECT AVG(overallRating) AS avgRating FROM reviews WHERE hotelId = ?;";
	
	private static final String HAS_REVIEW_HOTELID = "SELECT hotelId FROM reviews WHERE hotelId = ?;";
	
	private static final String HAS_REVIEW_USERNAME = "SELECT username FROM reviews WHERE username = ?;";
	

	
	
	private HotelsHandler() {
		hotelList = new ArrayList<>();
	}
	
	
	
	
	/*-----------------------------------------get Singleton Instance------------------------------------------*/
	
	/** Gets the single instance of the database handler. */
	
	public static HotelsHandler getInstance() {
		return hotelService;
	}
	
	
	
	
	
	/*-----------------------------------------methods to query table "hotels"---------------------------------*/
	
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
	
	
	
	
	
	
	
	
	/** get a list of all hotelIds */
	public List<String> getHotelList(){
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_HOTELID_COLUMN);
		
		try {
			while(rs.next()){
				String hotelId = rs.getString(1);
				hotelList.add(hotelId);
			}
		}catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getSt(), SqlHelper.getCt());
		}
		
		return hotelList;
	}
	
	
	
	
	
	
	
	
	
	/** given hotelId, return an instance of HotelPO which stores all info about this hotel in table hotels */
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
