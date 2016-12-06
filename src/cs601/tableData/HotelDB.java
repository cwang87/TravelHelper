package cs601.tableData;

/**
 * A class - represent the "hotels" table in database
 */

public class HotelDB implements Comparable<HotelDB> {

	private String hotelId;
	private String hotelName;
	private AddressDB hotelAddress;
	

	/**
	 * Constructor
	 * 
	 * @param hotelId
	 * @param hotelName
	 * @param city
	 * @param state
	 * @param strAddr
	 * @param country
	 * @param lat
	 * @param lon
	 */
	
	public HotelDB(String hotelId, String hotelName, String city, String state, String strAddr, String country, double lat, double lon) {
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		hotelAddress = new AddressDB(city, state, strAddr, country, lat, lon);
	}
	
	
	
	
	
	
	
	
	
	/** A method to decide the order of hotels stored in the data structure. */
	
	public int compareTo(HotelDB h) {
		return hotelName.compareTo(h.hotelName); 
	}
	
	
	
	
	
	
	
	/**
	 * A method - to return a string representing information about a review.
	 * Format is as follows:
	 * 
	 * HoteName: hotelId
	 * streetAddress
	 * city, state, country	
	 */
	
	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		
		sBuffer.append(hotelName).append(": ").append(hotelId).append("\n");
		sBuffer.append(hotelAddress.getStrAddr()).append("\n");
		sBuffer.append(hotelAddress.getCity()).append(", ")
				.append(hotelAddress.getState()).append(", ")
				.append(hotelAddress.getCountry()).append("\n");
		
		return sBuffer.toString();
	}
	
	
	
	
	
	
	
	/* ----------------------------------------*/

	public String getHotelId() {
		return hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public AddressDB getHotelAddress() {
		return hotelAddress;
	}

	

	
}

