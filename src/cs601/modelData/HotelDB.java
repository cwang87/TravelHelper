package cs601.modelData;

/**
 * A class represent info from hotels table in mySql database 
 */

public class HotelDB implements Comparable<HotelDB> {
	
	private String hotelId;
	private String hotelName;
	private String city;
	private String state;
	private String strAddr;
	private String country;
	private double lat;
	private double lon;
	

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param hName
	 * @param city
	 * @param state
	 * @param streetAddress
	 * @param country
	 * @param lat
	 * @param lon
	 */
	
	public HotelDB(String hotelId, String hotelName, String city, String state, String strAddr, String country,
			double lat, double lon) {
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.city = city;
		this.state = state;
		this.strAddr = strAddr;
		this.country = country;
		this.lat = lat;
		this.lon = lon;
	}
	
	
	
	
	
	/** A method to decide the order of hotels stored in the data structure. */
	
	public int compareTo(HotelDB h) {
		return hotelName.compareTo(h.hotelName); 
	}




	/* ---------------getters-------------------------*/

	public String getHotelId() {
		return hotelId;
	}





	public String getHotelName() {
		return hotelName;
	}





	public String getCity() {
		return city;
	}





	public String getState() {
		return state;
	}





	public String getStrAddr() {
		return strAddr;
	}





	public String getCountry() {
		return country;
	}





	public double getLat() {
		return lat;
	}





	public double getLon() {
		return lon;
	}
	
	
	
	
	

	




}
