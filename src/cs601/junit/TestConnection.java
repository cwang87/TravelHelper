package cs601.junit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs601.dao.DBConnector;

public class TestConnection {

	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ParseException {
		
		String testDate = "2016-08-16T18:38:29Z";
		Date date = parseDate(testDate);
//		System.out.println(date);
	}
	
	
	
	
	public static Date parseDate(String reviewDate) throws ParseException {
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = parser.parse(reviewDate);
		String date2 = parser.format(date1);
//		String date3 = date2.toString();
//		Date date4 = parser.parse(date2);
		System.out.println(date1);
		System.out.println(date2);
		System.out.println(parser.parse(date2));
//		System.out.println(date3);
//		System.out.println(date4);
		return parser.parse(date2);
	}	
	
	
}

///** add userId column to reviews table */
//private void addUserIdCol(){
//	
//	Connection connection = dao.createConnection();
//	Statement statement = null;
//	String addUserIdCol = "ALTER TABLE reviews ADD userId INTEGER NOT NULL;";
//	try {
//		statement = connection.createStatement();
//		statement.executeUpdate(addUserIdCol);
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}finally{
//		try {
//			statement.close();
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//}









//private void updateUserIdCol(){
//	String p1 = "UPDATE reviews SET userId =";
//	String p2 = " WHERE reviewId = '";
//	String p3 = "';";
//	
//	for(String id: reviewIdNameMap.keySet()){
//		String name = reviewIdNameMap.get(id);
//		Integer userId = userIdMap.get(name);
//		String userIdS = userId.toString();
//		
//		String sql = p1 + userIdS + p2 + id + p3;
//		Connection connection = dao.createConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//			statement.executeUpdate(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				statement.close();
//				connection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		
//	}
//}

