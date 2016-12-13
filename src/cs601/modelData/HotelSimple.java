package cs601.modelData;

/**
 * A class - represent the a simple "hotels" only hotelId and hotelName
 */

public class HotelSimple {

	private String hotelId;
	private String hotelName;
	

	/**
	 * Constructor
	 * 
	 * @param hotelId
	 * @param hotelName
	 */
	
	public HotelSimple(String hotelId, String hotelName) {
		this.hotelId = hotelId;
		this.hotelName = hotelName;
	}
	
	

	public String getHotelId() {
		return hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	

	
}

