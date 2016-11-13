package cs601.model.PO;

/**
 * A class - represent the "hotels" table in database
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
	
	
	public int compareTo(Hotel h) {
		return hotelName.compareTo(h.hotelName); 
	}

	
	// getters
	public String getHotelId() {
		return hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public Address getHotelAddress() {
		return hotelAddress;
	}

	
	//setters
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public void setHotelAddress(Address hotelAddress) {
		this.hotelAddress = hotelAddress;
	}
	
	
	
}

