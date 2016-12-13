package cs601.modelData;

/**
 * A class of a tourist attraction, including: attractionId, name, address and rating.
 */
public class TouristAttraction {
	private String attractionId;
	private String attractionName;
	private String attractionAddress;
	private double attractionRating;
	
	/**
	 * constructor
	 */

	public TouristAttraction(String id, String name,double rating, String address){
		attractionId = id;
		attractionName = name;
		attractionAddress = address;
		attractionRating = rating;
				
	}

	public String getAttractionId() {
		return attractionId;
	}

	public String getAttractionName() {
		return attractionName;
	}

	public double getAttractionRating() {
		return attractionRating;
	}
	
	public String getAttractionAddress() {
		return attractionAddress;
	}
	
	public String toString(){
		String s = attractionName + "; " + attractionAddress + "\n\n";
		return s;
		
	}


}
