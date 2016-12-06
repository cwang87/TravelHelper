package cs601.tableData;

/**
 * A class - represent the address part of "hotels" table in database
 */
public class AddressDB {

	private String city;
	private String state;
	private String strAddr;
	private String country;
	private double lat;
	private double lon;

	
	
	
	/**
	 * Constructor
	 * 
	 * @param city
	 * @param state
	 * @param strAddr
	 * @param country
	 * @param lat
	 * @param lon
	 */
	public AddressDB(String city, String state, String strAddr, String country, double lat, double lon) {
		super();
		this.city = city;
		this.state = state;
		this.strAddr = strAddr;
		this.country = country;
		this.lat = lat;
		this.lon = lon;
	}

	
	
	
	

	/**
	 * return an address string in the format: streetAddr, city, state, country
	 */
	public String toString(){
		
		StringBuffer sBuffer = new StringBuffer();
		
		sBuffer.append(strAddr).append(",");
		sBuffer.append(city).append(",");
		sBuffer.append(state).append(",");
		sBuffer.append(country);
		
		return sBuffer.toString();
	}
	
	
	
	
	
	

	/*-----------------------------------------getters----------------------------------------*/
	
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
