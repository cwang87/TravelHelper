package cs601.hotelapp;

/**
 * A class for hotels information.
 */

public class Hotel implements Comparable<Hotel> {

	private String hotelId;
	private String hotelName;
	private Address hotelAddress;

	public Hotel(String id, String hName, String city, String state, String streetAddress, String country, double lat,
			double lon) {
		hotelId = id;
		hotelName = hName;
		hotelAddress = new Address(city, state, streetAddress, country, lat, lon);
	}
	
	
	
	
	
	
	
	/**
	 * A method - to return a string representing information about a review.
	 * Format is as follows:
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
	
	
	
	
	
	public int compareTo(Hotel h) {
		return hotelName.compareTo(h.hotelName); 
	}

	
	
	
	/* ----------------------------------*/
	public String getHotelId() {
		return hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public Address getHotelAddress() {
		return hotelAddress;
	}

	
	
}

