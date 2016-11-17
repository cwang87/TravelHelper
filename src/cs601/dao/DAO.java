package cs601.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs601.util.Status;

/**
 * A class to provide basic operations towards database, 
 * including: connect database, close database, do CRUD to database (query and update)
 */

public class DAO {

	private static DBConnector connector = new DBConnector();
	private static Connection ct = null;
	private static PreparedStatement ps = null;
	private static Statement st = null;
	private static ResultSet rs = null;

	
	
	
	/*----------------------------------Methods to create and test a connection ------------------------------------------*/
	

	/** A method to get connection. */
	public static Connection getConnection() {
		
		try {
			ct = connector.getConnection();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		return ct;
	}
	
	
	/** A method to test connection. */
	public static boolean testConnection(){
		return connector.testConnection();
	}
	
	
	
	/*----------------------------------Methods to close resourses----------------------------------------------*/

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
	
	
	
	
	
	
	/*----------------------------------Methods to executeUpdate()----------------------------------------------*/
	
	/** A method - to execute one add/update/delete statement without parameters */
	
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
	
	
	/** A method - to execute one add/update/delete sql	 */
	
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

	
	
	/** A method - to execute multiple add/update/delete sql at a time. */

	public static boolean executeUpdate(String[] sql, String[][] parameters) {
		
		boolean okey = false;
		
		ct = getConnection();
		
		try {
			// prevent sql from executing one by one
			ct.setAutoCommit(false);

			for (int i = 0; i < sql.length; i++) {
				if (parameters[i] != null) {
					ps = ct.prepareStatement(sql[i]);
					for (int j = 0; j < parameters[i].length; j++) {
						ps.setString(j + 1, parameters[i][j]);
					}
					ps.executeUpdate();
					ps.close();
				}
			}

			ct.commit();
			
			okey = true;
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION);
			try {
				ct.rollback();
			} catch (SQLException e1) {
				System.out.println(Status.SQL_EXCEPTION);
			}
		} finally {
			try {
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
		}
		
		return okey;
	}
	
	
	
	
	
	
	/*----------------------------------Methods to executeQuery()----------------------------------------------*/
	
	
	
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

	
	
	
	public static ResultSet executeQuery(String sql, String parameter){
		
		ct = getConnection();
		
		try {
			if(parameter != null && !parameter.equals("")){
				ps.setString(1, parameter);
			}
			
			rs = ps.executeQuery();
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
		
		return rs;
	}
	
	
	
	public static ResultSet executeQuery(String sql){
		
		ct = getConnection();
		
		try {
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}
			
		return rs;
	}



	
	
	/*----------------------------------getters to close resourses----------------------------------------------*/
	
	public static Connection getCt() {
		return ct;
	}


	public static PreparedStatement getPs() {
		return ps;
	}


	public static Statement getSt() {
		return st;
	}


	public static ResultSet getRs() {
		return rs;
	}
	
	
	
}
