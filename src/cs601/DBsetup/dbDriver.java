package cs601.DBsetup;


public class dbDriver {
	
	
	
	/**
	 * driver class to setup database
	 */
	
	
	public static void main(String[] args) {
		
		//load hotel data to maps
		setupTables db = new setupTables();
		System.out.println("finished creating tables");
		
//		//create hotels and reviews tables, load data from maps to tables in database
//		db.loadMaps2DB();
//		
//		//create users table, and set the start number for userId
//		modifyTables dbModify = new modifyTables();
//		
//		dbModify.modifyTables();
//		
	}
	
	
	
	
	
	
}
