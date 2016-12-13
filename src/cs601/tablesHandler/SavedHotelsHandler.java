package cs601.tablesHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs601.modelData.HotelSimple;
import cs601.sqlHelper.SqlHelper;
import cs601.util.Status;

/**
 * A class - contains methods to manipulate data from savedHotels table in mySql. 
 */

public class SavedHotelsHandler {
	
	
	private static SavedHotelsHandler singleton = new SavedHotelsHandler();
	
	
	private static final String INSERT_NEWSAVE = "INSERT INTO savedHotels (username, hotelId) VALUES (?, ?);";
	
	private static final String SEARCH_SAVE = "SELECT * FROM savedHotels WHERE username = ? AND hotelId = ?;";
	
	private static final String SEARCH_ALLSAVE = "SELECT hotelId FROM savedHotels WHERE username = ?;";
	
	private static final String DELETE_LIST = "DELETE FROM savedHotels WHERE username = ?;";
	
	
	
	
	private SavedHotelsHandler(){
		
	}
	
	
	
	/** Gets the single instance of the database handler. */
	public static SavedHotelsHandler getInstance(){
		return singleton;
	}

	
	/** add a new save into table savedHotels. */
	public Status saveHotel(String username, String hotelId){
		Status status = Status.ERROR;
		
		String[] parameters = {username, hotelId};
		
		if(SqlHelper.executeUpdate(INSERT_NEWSAVE, parameters)){
			status = Status.OK;
		}
		return status;
	}
	
	
	
	/** check whether a hotel has already been saved by user. */
	public boolean checkSaveExisting(String username, String hotelId){
		boolean exist = false;
		
		String[] parameters = {username, hotelId};
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_SAVE, parameters);
		try {
			if(rs.next()){
				exist = true;
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		} finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return exist;
	}
	
	
	
	/** search all hotels that has been saved by user. */
	public List<HotelSimple> getSavedHotels(String username){
		
		List<HotelSimple> list = new ArrayList<>();
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_ALLSAVE, username);
		if(rs == null){
			System.out.println(rs);
			return null;
		}
		
		try {
			while(rs.next()){
				String hotelId = rs.getString(1);
				String hotelName = HotelsHandler.getInstance().getHotelName(hotelId);
				HotelSimple hotel = new HotelSimple(hotelId, hotelName);
				list.add(hotel);
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		} finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return list;
	}
	
	
	/** delete user's saved hotel list. */
	public Status deleteSavedList(String username){
		Status status = Status.ERROR;
		String[] parameters = {username};
		
		SqlHelper.executeUpdate("SET SQL_SAFE_UPDATES=0;");
		
		if(SqlHelper.executeUpdate(DELETE_LIST, parameters)){
			status = Status.OK;
		}
		
		SqlHelper.executeUpdate("SET SQL_SAFE_UPDATES=1;");
		
		return status;
	}
	
	
	
	
}
