package cs601.database;

import cs601.util.Status;
import cs601.sqlHelper.SqlHelper;;

public class DatabaseWriter {
	
	public static void main(String[] args) {
		
		DatabaseHandler handler = DatabaseHandler.getInstance();
		
		/* test database connection before loading database */
		if(SqlHelper.testConnection()){
			
			
			/* create tables for database. */
			Status createUsers = handler.createUsers();
			System.out.println("Create table users: " + createUsers);
			
			Status createHotels = handler.createHotels();
			System.out.println("Create table hotels: " + createHotels);
			
			Status createReviews = handler.createReviews();
			System.out.println("Create table reviews: " + createReviews);
			
			
			
			/* load info into table "hotels" */
			handler.loadHotelsTable();
			System.out.println("Successfully write hotels Table");
			
			handler.loadReviewsTable();
			System.out.println("Successfully write reviews Table");
			
			
			
			/* register users in json files with uniform password "123456" */
			handler.registerZusers();
			System.out.println("Successfully register json users");
			
			
			
			/* add userId column to reviews table */
			handler.addUserIdCol();
			System.out.println("add userId column to reviews");
			
			handler.updateUserId();
			System.out.println("Successfully update userId in reviews");
			
		}
		
	}
	

	
	
	
}
