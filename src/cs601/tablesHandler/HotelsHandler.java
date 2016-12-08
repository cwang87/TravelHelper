package cs601.tablesHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs601.sqlHelper.SqlHelper;
import cs601.tableData.HotelAveRate;
import cs601.tableData.HotelDB;
import cs601.util.Status;
import cs601.util.Tools;

public class HotelsHandler {

	private static HotelsHandler hotelsHandler = new HotelsHandler();
	
	
	private static final String SEARCH_ONEHOTEL_WITH_AVG = "SELECT hotels.hotelId, hotelName,"
			+ " city, state, strAddr, country, AVG(overallRating) AS aveRating, lat, lon "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "WHERE hotels.hotelId=? "
			+ "GROUP BY hotels.hotelId;"; 
	
	
	private static final String SEARCH_ALLHOTELS_WITH_AVG = "SELECT hotels.hotelId, hotelName,"
			+ " city, state, strAddr, country, AVG(overallRating) AS aveRating, lat, lon "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "GROUP BY hotels.hotelId;"; 
	
	private static final String SEARCH_HOTELS_BYCITY_WITH_AVG = "SELECT hotels.hotelId, hotelName, "
			+ "city, state, strAddr, country, AVG(overallRating) AS aveRating, lat, lon "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "WHERE city=? AND state=? "
			+ "GROUP BY hotels.hotelId;";
	
	private static final String SEARCH_HOTELS_BYNAME_WITH_AVG = "SELECT hotels.hotelId, hotelName, "
			+ "city, state, strAddr, country, AVG(overallRating) AS aveRating, lat, lon "
			+ "FROM hotels LEFT OUTER JOIN reviews "
			+ "ON hotels.hotelId = reviews.hotelId "
			+ "WHERE hotelName LIKE ? "
			+ "GROUP BY hotels.hotelId;";
	
	private static final String SEARCH_ONEHOTEL_DB = "SELECT * FROM hotels WHERE hotelId = ?;";
	
	private static final String SEARCH_HOTELNAME = "SELECT hotelName FROM hotels WHERE hotelId = ?;";
	
	
	
	private HotelsHandler() {
	}
	
	
	
	
	/*-----------------------------------------get Singleton Instance------------------------------------------*/
	
	/** Gets the single instance of the database handler. */
	
	public static HotelsHandler getInstance() {
		return hotelsHandler;
	}
	
	
	/*---------------------------------------------get one hotel info ---------------------------------------*/

	/** given hotelId, return an instance of HotelAveRate which stores info about this hotel from table hotels 
	 * (without lat & lon), and average rating information from table reviews. */
	public HotelAveRate getHotelAveRate(String hotelIdSearch){
		
		HotelAveRate hotel = null;
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_ONEHOTEL_WITH_AVG, hotelIdSearch);
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
					Double d = rs.getDouble(7);
					aveRating = Tools.formatDouble(d);
				}
				String hotelAddr = strAddr + ", " + city + ", " + state + ", " + country;
				
				String lat = Double.toString(rs.getDouble(8));
				String lon = Double.toString(rs.getDouble(9));
				
				hotel = new HotelAveRate(hotelId, hotelName, hotelAddr, aveRating, lat, lon);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hotel;
	}
	
	
	
	
	
	/** given hotelId, return an instance if HotelDB which stores all info about this hotel from hotels table */
	public HotelDB getHotelDB(String hotelId){
		
		HotelDB hotel = null;
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_ONEHOTEL_DB, hotelId);
		
		try {
			if (rs.next()){
				String hotelName = rs.getString(2);
				String city = rs.getString(3);
				String state = rs.getString(4);
				String strAddr = rs.getString(5);
				String country = rs.getString(6);
				double lat = rs.getDouble(7);
				double lon = rs.getDouble(8);
			
			hotel = new HotelDB(hotelId, hotelName, city, state, strAddr, country, lat, lon);	
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		} finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hotel;
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
	public List<HotelAveRate> getHotelsByCity(String citySearch, String stateSearch){
		
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
	
	public List<HotelAveRate> getHotelsByPartialName(String name){
		
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
					Double d = rs.getDouble(7);
					aveRating = Tools.formatDouble(d);
				}
				String hotelAddr = strAddr + ", " + city + ", " + state + ", " + country;
				
				String lat = Double.toString(rs.getDouble(8));
				String lon = Double.toString(rs.getDouble(9));
				
				HotelAveRate hotel = new HotelAveRate(hotelId, hotelName, hotelAddr, aveRating, lat, lon);
				
				hotelList.add(hotel);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}
		
		return hotelList;
	}
	
	
	
	
	/*-----------------------------------------query about hotels------------------------------------------*/
	
	/**
	 * get hotelName given hotelId
	 */
	public String getHotelName(String hotelId){
		String hotelName = "";
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_HOTELNAME, hotelId);
		
		try {
			while(rs.next()){
				hotelName = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		} finally{
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hotelName;
		
	}
		
		
	
	
	

	
		
		
	
	
	
	
	
	
	
}
