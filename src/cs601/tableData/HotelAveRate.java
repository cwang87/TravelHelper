package cs601.tableData;

/**
 * A class - hotel class with infomation will be shown in the hotel list
 */

public class HotelAveRate implements Comparable<HotelAveRate> {

	private String hotelId;
	private String hotelName;
	private String hotelAddr;
	private String aveRating;
	private String lat;
	private String lon;
	

	/**
	 * Constructor
	 */
	
	public HotelAveRate(String hotelId, String hotelName, String hotelAddr, String aveRating, String lat, String lon) {
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.hotelAddr = hotelAddr;
		this.aveRating = aveRating;
		this.lat = lat;
		this.lon = lon;
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

	public String getLat() {
		return lat;
	}
	
	public String getLon() {
		return lon;
	}

	
}

