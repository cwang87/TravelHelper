package cs601.controller.map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cs601.modelData.HotelDB;
import cs601.modelData.TouristAttraction;
import cs601.tablesHandler.HotelsHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Class HotelDataBuilder - load hotel information and review information, and
 * add them to the maps of the ThreadSafeHotelData object we build in this
 * class.
 */
public class MapAPIHelper {
	
	private static MapAPIHelper singleton = new MapAPIHelper();

	
	private MapAPIHelper(){}
	
	
	public static MapAPIHelper getInstance() {
		return singleton;
	}
	

	/*-----------------------------------------Fetch Attractions---------------------------------------------------*/
	
	/**
	 * A method to parse json string from google place search API response
	 * @param json string
	 */
	public List<TouristAttraction> getAttractions(String hotelId, String radiusInMiles) {
		
		List<TouristAttraction> attractionList = new ArrayList<>();
		
		//build request string with given hotelId and radius which is in mile
		String request = getAttractionRequest(hotelId, radiusInMiles);
		
		//call get json result to be parsed
		String jsonToParse = callAPI(request, "maps.googleapis.com");
		
		//parse json results
		JSONParser parser = new JSONParser();
		JSONObject jsonObject;

		try {
			jsonObject = (JSONObject) parser.parse(jsonToParse);

			JSONArray results = (JSONArray) jsonObject.get("results");

			for (Object o : results) {
				JSONObject singleResult = (JSONObject) o;
				String attractionId = (String) singleResult.get("id");
				String name = (String) singleResult.get("name");

				Object tempRating = singleResult.get("rating");
				Double rating;
				if (tempRating != null) {
					rating = ((Number) tempRating).doubleValue();
				} else {
					rating = new Double(0);
				}

				String address = (String) singleResult.get("formatted_address");
				
				TouristAttraction attraction = new TouristAttraction(attractionId, name, rating, address);
				
				attractionList.add(attraction);
			}
		} catch (ParseException e) {
			return null;
		}
		
		return attractionList;
	}

	
	
	

	/**
	 * A method to build request string with given hotelId and radius which is in mile
	 * 
	 * @param hotelId
	 * @param radiusInMiles
	 * @return HTTP GET request header String for query about attrations near a
	 *         given hotel within a given radius
	 */
	private String getAttractionRequest(String hotelId, String radiusInMiles) {

		String request = "";
		
		// get location part query string
		HotelDB hotel = HotelsHandler.getInstance().getHotelDB(hotelId);
		double lat = hotel.getLat();
		double lon = hotel.getLon();
		String ci = hotel.getCity();
		String city = ci.replaceAll(" ", "%20");
		String locationQuery = "?query=tourist%20attractions+in+" + city + "&location=" + lat + "," + lon;

		// build full path and query string
		StringBuffer sb = new StringBuffer();
		int radius = Integer.parseInt(radiusInMiles) * 1609;
		sb.append("/maps/api/place/textsearch/json"); // required url
		sb.append(locationQuery); // query & location
		sb.append("&radius=").append(Integer.toString(radius)); // radius
		sb.append("&key=AIzaSyBYgYUCC8EMft37eIsxoct2-qoijhVZ6lo"); // key
		String pathAndQuery = sb.toString();
		
		// get full HTTP GET request header string
		request = "GET " + pathAndQuery + " HTTP/1.1" + System.lineSeparator() // GET
																				// request
				+ "Host: maps.googleapis.com" + System.lineSeparator()
				+ "Connection: close" + System.lineSeparator()
				+ System.lineSeparator();
		
		return request;
	}

	
	
	/*-----------------------------------------embed Google Map---------------------------------------------------*/
	
	
	/**
	 * A method to generate request url string required by google map API
	 * @param hotelId
	 * @return
	 */
	public String getHotelMapRequest(String hotelId){
		
		String url = "https://www.google.com/maps/embed/v1/place?key=AIzaSyB3cbJnQmxutzUgcvhWXAhAPiwYFL9AgzE&q=";
		
		HotelDB hotel = HotelsHandler.getInstance().getHotelDB(hotelId);
		String hotelName = hotel.getHotelName().replaceAll(" ", "%20");
		String city = hotel.getCity().replaceAll(" ", "%20");
		String state = hotel.getState().replaceAll(" ", "%20");
		String zoom = "&zoom=13";
		
		StringBuffer sb = new StringBuffer();
		sb.append(url).append(hotelName).append(",").append(city).append("%20").append(state);
		sb.append(zoom);
		
		return sb.toString();
	}
	
	
	

	
	
	/*-----------------------------------------Call Google API--------------------------------------------*/
	
	/**
	 * A method to call google map API give host and request string, return json result to be parsed
	 * @param google map API request
	 * @param google map API request host
	 * @return json result from google to be parsed (already removed header part)
	 */
	private String callAPI(String request, String host) {

			String jsonToParse = "";
		
			SSLSocket socket = null;
			PrintWriter out = null;
			BufferedReader in = null;
			try {
				SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				socket = (SSLSocket) factory.createSocket(host, 443);
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				out.println(request);
				out.flush();

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line;
				StringBuffer sb = new StringBuffer();
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				
				// remove headers
				String response = sb.toString();
				int bodyIndex = response.indexOf("{");
				jsonToParse = response.substring(bodyIndex);

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					in.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return jsonToParse;
	}
	
	

}
