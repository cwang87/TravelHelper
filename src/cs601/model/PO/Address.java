package cs601.model.PO;

/**
 * A class - represent the address part of "hotels" table in database
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

	//setters
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public void setState(String state) {
		this.state = state;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
	
}
