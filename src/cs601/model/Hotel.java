package cs601.model;

/**
 * A class - represent the "hotels" table in database
 */

public class Hotel implements Comparable<Hotel> {

	private String hotelId;
	private String hotelName;
	private Address hotelAddress;
	

	public Hotel(String id, String hName, String city, String state, String streetAddress, String country) {
		hotelId = id;
		hotelName = hName;
		hotelAddress = new Address(city, state, streetAddress, country);
	}
	
	
	
	
	
	
	
	
	
	/** A method to decide the order of hotels stored in the data structure. */
	
	public int compareTo(Hotel h) {
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
		sBuffer.append(hotelAddress.getStreetAddress()).append("\n");
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

	public Address getHotelAddress() {
		return hotelAddress;
	}

	
//	/* ----------------------------------------*/
//	 
//	public void setHotelId(String hotelId) {
//		this.hotelId = hotelId;
//	}
//
//	public void setHotelName(String hotelName) {
//		this.hotelName = hotelName;
//	}
//
//	public void setHotelAddress(Address hotelAddress) {
//		this.hotelAddress = hotelAddress;
//	}
//	
	
	
}

