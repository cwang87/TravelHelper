package cs601.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cs601.util.Status;

/**
 * A class to provide basic operations towards database, including: connect
 * database, close database, do CRUD to database (query and update)
 */

public class BaseDAO {

	private static Connection ct = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;

	private static DBConnector connector;

	/** Default constructor */
	public BaseDAO() {
		try {
			connector = new DBConnector();
		} catch (FileNotFoundException e) {
			System.out.println(Status.MISSING_CONFIG + e.getMessage());
		} catch (IOException e) {
			System.out.println(Status.MISSING_VALUES + e.getMessage());
		}
	}

	/** Constructor with parameters */
	public BaseDAO(String configPath) {
		try {
			connector = new DBConnector(configPath);
		} catch (FileNotFoundException e) {
			System.out.println(Status.MISSING_CONFIG + e.getMessage());
		} catch (IOException e) {
			System.out.println(Status.MISSING_VALUES + e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * A method - attempts to connect to database using default configuration.
	 * 
	 * @return database connection
	 */
	public Connection createConnection() {
		try {
			ct = connector.getConnection();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}
		return ct;
	}
	
	
	
	
	
	
	

	/**
	 * A method - to close resources after database operation
	 * 
	 * @param ResultSet
	 * @param PreparedStatement
	 * @param Connection
	 */
	public void close(ResultSet rs, PreparedStatement ps, Connection ct) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			rs = null;
		}

		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			ps = null;
		}

		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			ct = null;
		}

	}

	
	
	
	
	
	
	
	
	
	
	/**
	 * A method - to execute one add/update/delete sql
	 * 
	 * @param sql
	 * @param parameters
	 */
	public void executeUpdate(String sql, String[] parameters) {
		try {
			ct = connector.getConnection();

			ps = ct.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION);
		} finally {
			close(rs, ps, ct);
		}
	}

	/**
	 * A method - to execute multiple add/update/delete sql at a time
	 * 
	 * @param sql
	 * @param parameters
	 */
	public void executeUpdate(String[] sql, String[][] parameters) {
		try {
			ct = connector.getConnection();
			// prevent sql from executing one by one
			ct.setAutoCommit(false);

			for (int i = 0; i < sql.length; i++) {
				if (parameters[i] != null) {
					ps = ct.prepareStatement(sql[i]);
					for (int j = 0; j < parameters[i].length; j++) {
						ps.setString(j + 1, parameters[i][j]);
					}
					ps.executeUpdate();
				}
			}

			ct.commit();
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION);
			try {
				ct.rollback();
			} catch (SQLException e1) {
				System.out.println(Status.SQL_EXCEPTION);
			}
		} finally {
			close(rs, ps, ct);
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * A method - to execute a query and return the result of the query
	 * @param sql
	 * @param parameters
	 * @return query result table
	 */
	public ResultSet executeQuery(String sql, String[] parameters){
		try {
			ct= connector.getConnection();
			ps=ct.prepareStatement(sql);
			if(parameters != null && !parameters.equals("")){
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i+1, parameters[i]);
				}
			}
		} catch (Exception e) {
			System.out.println(Status.SQL_EXCEPTION);
		} finally {
			close(rs, ps, ct);
		}
		return rs;
	}

	/**
	 * A method - to execute a query and return the result of the query
	 * @param sql
	 * @param only 1 parameter
	 * @return query result table
	 */
	public ResultSet executeQuery(String sql, String parameter){
		try {
			ct= connector.getConnection();
			ps=ct.prepareStatement(sql);
			if(parameter != null && !parameter.equals("")){
				ps.setString(1, parameter);
			}
		} catch (Exception e) {
			System.out.println(Status.SQL_EXCEPTION);
		} finally {
			close(rs, ps, ct);
		}
		return rs;
	}
	
	
	public boolean testConnection(){
		return connector.testConnection();
	}
	
}
