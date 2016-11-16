package cs601.DBsetup;

/**
 * A class for Address of hotels.
 */
public class Address {

	private String streetAddress;
	private String city;
	private String state;
	private String country;
	private double longitude;
	private double latitude;

	public Address(String ci, String st, String str, String c, double lat, double lon) {
		country = c;
		city = ci;
		state = st;
		streetAddress = str;
		longitude = lon;
		latitude = lat;
	}

	//getters
	public String getStreetAddress() {
		return streetAddress;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	
}
