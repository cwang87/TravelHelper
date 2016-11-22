package cs601.sqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs601.util.Status;

/**
 * A class - provide static methods to perform general sql operations on mySQL database, 
 * including: connect database, close database, test database connection, update and query database.
 */

public class SqlHelper {

	private static DBConnector connector = new DBConnector();
	private static Connection ct = null;
	private static PreparedStatement ps = null;
	private static Statement st = null;
	private static ResultSet rs = null;

	
	
	
	/*----------------------------------Methods to create and test a connection --------------------------------*/
	

	/** A method to get connection from dabatase. */
	public static Connection getConnection() {
		
		try {
			ct = connector.getConnection();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		return ct;
	}
	
	
	/** A method used to test connection to database before any further db operation. */
	public static boolean testConnection(){
		return connector.testConnection();
	}
	
	
	
	/*----------------------------------Overload Method to close resourses--------------------------------------*/

	
	
	/** A method - close database connection resources
	 * including PreparedStatement and Connection after an update */
	public static void close(PreparedStatement ps, Connection ct) {
		
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			ps = null;
		}

		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			ct = null;
		}

	}

	/** A method - close database connection resources
	 * including Statement and Connection after an update */
	public static void close(Statement st, Connection ct) {

		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			st = null;
		}

		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			ct = null;
		}

	}
	
	
	/** A method - close database connection resources
	 * including ResultSet, PreparedStatement and Connection after a query */
	public static void close(ResultSet rs, PreparedStatement ps, Connection ct) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			rs = null;
		}

		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			ps = null;
		}

		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			ct = null;
		}

	}

	
	/** A method - close database connection resources
	 * including ResultSet, Statement and Connection after a query*/
	public static void close(ResultSet rs, Statement st, Connection ct) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			rs = null;
		}

		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			st = null;
		}

		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
			ct = null;
		}

	}
	
	
	
	/*----------------------------------Overload Methods to executeUpdate()----------------------------------------------*/
	
	/** A method - to execute one add/update/delete statement */
	
	public static boolean executeUpdate(String sql){
		
		boolean okey = false;
		
		ct = getConnection();
		
		try {
			st = ct.createStatement();
			
			st.executeUpdate(sql);
			
			okey = true;
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}finally{
			close(st, ct);
		}
		
		return okey;
	}
	
	
	
	
	
	
	
	/** A method - to execute one add/update/delete sql with parameters*/
	
	public static boolean executeUpdate(String sql, String[] parameters) {
		
		boolean okey = false;
		
		ct = getConnection();
		
		try{
			ps = ct.prepareStatement(sql);
			
			if (parameters != null ) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			
			ps.executeUpdate();
				
			okey = true;
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		} finally {
			close(ps, ct);
		}
		
		return okey;
	}

	
	
	
	
	
	/*----------------------------------Overload Methods to executeQuery()----------------------------------------------*/
	
	
	
	/** A method - to execute a sql query*/
	
	public static ResultSet executeQuery(String sql){
		
		ct = getConnection();
		
		try {
			st = ct.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
			
		return rs;
	}

	
	
	
	
	
	
	
	
	/** A method - to execute a sql query with only one parameter*/
	
	public static ResultSet executeQuery(String sql, String parameter){
		
		ct = getConnection();
		
		try {
			ps = ct.prepareStatement(sql);
			
			if(parameter != null && !parameter.equals("")){
				ps.setString(1, parameter);
			}
			
			rs = ps.executeQuery();
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		
		return rs;
	}
	
	
	
	

	
	
	
	
	/** A method - to execute a sql query with a parameter array*/
	
	public static ResultSet executeQuery(String sql, String[] parameters){
		
		ct = getConnection();
		
		try {
			ps = ct.prepareStatement(sql);
			
			if(parameters != null){
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i+1, parameters[i]);
				}
			}
			
			rs = ps.executeQuery();
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		
		return rs;
	}
	
	
	
	
	



	
	
	/*----------------------------------getters used to close resourses----------------------------------------------*/
	
	/** get Connection **/
	public static Connection getCt() {
		return ct;
	}

	/** get PreparedStatement **/
	public static PreparedStatement getPs() {
		return ps;
	}

	/** get Statement **/
	public static Statement getSt() {
		return st;
	}

	/** get ResultSet **/
	public static ResultSet getRs() {
		return rs;
	}
	
	
	
	
	
	
	
	
}
