package cs601.DBsetup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import cs601.dao.BaseDAO;

public class modifyTables {

	private Map<String, Integer> userIdMap;
	private BaseDAO dao;
	private final String UPDATE_OLDUSERS_SQL = "INSERT INTO oldusers (username, userId) VALUES (?, ?);";
	
	
	public modifyTables(){
		userIdMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		dao = new BaseDAO();
	}
	
	
	public modifyTables(String config){
		userIdMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		dao = new BaseDAO(config);
	}
	
	
	
	
	
	
	/** method to create users table */
	public void modifyTables(){
		
		// update oldusers table and get username and userId map meanwhile.
		int counter = updateOldusers() + 1;
		String startId = Integer.toString(counter);
		
		// set a start number for userId column of users table
		modifyUsers(startId);
		
		// add a new column - userId to table reviews, and update this column 
		modifyReviews();
		
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	/** create users table in database to store registered users information. */
	private void modifyUsers(String startId){
		Connection connection = dao.createConnection();
		Statement statement = null;
		String alterSQL = "ALTER TABLE users AUTO_INCREMENT=" + startId + ";";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(alterSQL);
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
	
	
	
	
	/** modify reviews table - add userId column and update values of this column. */
	private void modifyReviews(){
		// add userId column to table reviews
		addUserIdCol();
	
		Connection connection = dao.createConnection();
		Statement statement = null;
		String join = "SELECT reviewId, oldusers.userId FROM reviews INNER JOIN oldusers ON reviews.username = oldusers.username;";
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery(join);
			while(rSet.next()){
				String reviewId = rSet.getString(1);
				int userid = rSet.getInt(2);
				String userId = Integer.toString(userid);
				String insertSql = prepareUserIdSQL(userId, reviewId);
				
				Statement statement2 = connection.createStatement();
				statement2.executeUpdate(insertSql);
				statement2.close();
			}
			rSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	/* add userId column to reviews table */
	private void addUserIdCol(){
		
		Connection connection = dao.createConnection();
		Statement statement = null;
		String addUserIdCol = "ALTER TABLE reviews ADD userId INTEGER NOT NULL;";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(addUserIdCol);
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

	
	/* prepare updateSQL statement */
	private String prepareUserIdSQL(String userId, String reviewId){
		String p1 = "UPDATE reviews SET userId =";
		String p2 = " WHERE reviewId = '";
		String p3 = "';";
		String sql = p1 + userId + p2 + reviewId + p3;
		
		return sql;
	}

	
	
	
	
	/* store usernames and userIds in a map and update values in oldusers table. */
	private int updateOldusers() {
		Connection connection = dao.createConnection();
		int counter = 0;
		Statement statement = null;
		ResultSet rSet = null;
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT username FROM reviews;");
			while (rSet.next()) {
				String username = (rSet.getString(1)).trim();
				
				if (!userIdMap.containsKey(username)) {
					counter++;
					userIdMap.put(username, counter);
					PreparedStatement pStatement = connection.prepareStatement(UPDATE_OLDUSERS_SQL);
					pStatement.setString(1, username);
					pStatement.setInt(2, counter);
					pStatement.executeUpdate();
					pStatement.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return counter;
	}
	
	
	

	
	
	
}
