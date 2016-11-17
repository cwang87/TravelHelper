package cs601.database;

import cs601.util.Status;

public class DatabaseWriter {
	
	public static void main(String[] args) {
		
		DatabaseHandler handler = DatabaseHandler.getInstance();
		

		/* to create tables for database. */
		Status createUsers = handler.createUsers();
		System.out.println(createUsers + "Create table users");
		
		Status createHotels = handler.createHotels();
		System.out.println(createHotels + "Create table hotels");
		
		Status createReviews = handler.createReviews();
		System.out.println(createReviews + "Create table reviews");
		
		
		
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
