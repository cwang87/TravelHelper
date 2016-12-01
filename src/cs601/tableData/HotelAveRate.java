package cs601.tableData;

/**
 * A class - hotel class with infomation will be shown in the hotel list
 */

public class HotelAveRate implements Comparable<HotelAveRate> {

	private String hotelId;
	private String hotelName;
	private String hotelAddr;
	private String aveRating;
	

	/**
	 * Constructor
	 */
	
	public HotelAveRate(String hotelId, String hotelName, String hotelAddr, String aveRating) {
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.hotelAddr = hotelAddr;
		this.aveRating = aveRating;
	}
	
	
	
	
	
	
	/** A method to decide the order of hotels stored in the data structure. */
	
	public int compareTo(HotelAveRate h) {
		return hotelName.compareTo(h.hotelName); 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* ----------------------------------------*/

	public String getHotelId() {
		return hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}


	public String getHotelAddr() {
		return hotelAddr;
	}


	public String getAveRating() {
		return aveRating;
	}

	

	
}

