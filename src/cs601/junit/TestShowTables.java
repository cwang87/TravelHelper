package cs601.junit;

import java.io.FileNotFoundException;
import java.io.IOException;

import cs601.dao.DBConnector;

public class TestShowTables {

	public static void main(String[] args) {
		DBConnector connector;
		try {
			connector = new DBConnector();
			connector.testConnection();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
