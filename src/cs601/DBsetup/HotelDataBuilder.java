package cs601.DBsetup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class HotelDataBuilder - load hotel information and review information, and
 * add them to the maps of the ThreadSafeHotelData object we build in this
 * class. This builder is multithreaded.
 */
public class HotelDataBuilder {

	private ThreadSafeHotelData hotelData;
	
	private WorkQueue workQueue;
	private volatile int numTasks;
	
	/** default constructor*/
	public HotelDataBuilder(ThreadSafeHotelData data) {
		hotelData = data;
		workQueue = new WorkQueue();
	}

	public HotelDataBuilder(ThreadSafeHotelData data, WorkQueue q) {
		hotelData = data;
		workQueue = q;
		numTasks = 0;
	}


	/**
	 * Read the json file with information about the hotels (id, name, address,
	 * etc) and load it into hotelMap.
	 */
	
	public void loadHotelInfo(String jsonFilename) {

		JSONParser parser = new JSONParser();

		try {
			JSONObject tempObject = (JSONObject) parser.parse(new FileReader(jsonFilename));
			JSONArray jsonArray = (JSONArray) tempObject.get("sr");

			for (Object o : jsonArray) {
				JSONObject singleHotel = (JSONObject) o;

				String hotelId = (String) singleHotel.get("id");
				String hotelName = (String) singleHotel.get("f");
				String streetAddress = (String) singleHotel.get("ad");
				String city = (String) singleHotel.get("ci");
				String state = (String) singleHotel.get("pr");
				String country = (String) singleHotel.get("c");
				JSONObject ll = (JSONObject) singleHotel.get("ll");
				String lon = (String) ll.get("lng");
				String lat = (String) ll.get("lat");

				hotelData.addHotel(hotelId, hotelName, city, state, streetAddress, country, Double.parseDouble(lat),
						Double.parseDouble(lon));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load reviews for all the hotels into the reviewMap.
	 */

	public synchronized void loadReviews(Path path) {
		directoryProcess(path);
		shutdown();
	}
	
	
	/**
	 * Traverse a given directory recursively to find all the json files with
	 * reviews and create a thread to work on it.
	 */
	private synchronized void directoryProcess(Path path){
		try {
			DirectoryStream<Path> pathsList = Files.newDirectoryStream(path);
			
			for (Path p : pathsList) {
				if (!Files.isDirectory(p) && p.toString().contains(".json")) {
					workQueue.execute(new JSONWorker(p.toString()));
				} else if(Files.isDirectory(p)){
					directoryProcess(p);
				} else {
					p.toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * A nestes class, a class implements Runnable, parses a JSON file 
	 * and merge its local "small" map to the "big" map through calling mergeReviewMap() method.
	 */
	public class JSONWorker implements Runnable{
		private String jsonFileName;
		private ThreadSafeHotelData localData = new ThreadSafeHotelData();
		
		public JSONWorker(String jName){
			jsonFileName = jName;
			incrementTasks();
		}
		
		@Override
		public void run(){	
				loadOneJson(localData, jsonFileName);
				hotelData.mergeReviewMap(localData);
				decrementTasks();
		}
	}
	
	
	private synchronized void loadOneJson(ThreadSafeHotelData localData, String jsonFileName){
		JSONParser parser = new JSONParser();
		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) parser.parse(new FileReader(jsonFileName));
		

		JSONObject reviewDetails = (JSONObject) jsonObject.get("reviewDetails");
		JSONObject reviewCollection = (JSONObject) reviewDetails.get("reviewCollection");
		JSONArray reviewArray = (JSONArray) reviewCollection.get("review");

		for (Object o : reviewArray) {
			JSONObject singleReview = (JSONObject) o;
			String hotelId = (String) singleReview.get("hotelId");
			String reviewId = (String) singleReview.get("reviewId");
			String reviewTitle = (String) singleReview.get("title");
			String reviewText = (String) singleReview.get("reviewText");
			String username = (String) singleReview.get("userNickname");
			Long overallRating = (Long) singleReview.get("ratingOverall");
			String recommendation = (String) singleReview.get("isRecommended");
			String reviewDate = (String) singleReview.get("reviewSubmissionTime");
			localData.addReview(hotelId, reviewId, (int) (long) overallRating, reviewTitle, reviewText,
					getBoolean(recommendation), reviewDate, username);
		}
		
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Boolean getBoolean(String isRecomm){
		if(isRecomm.equals("YES")){
			return true;
		}else{
			return false;
		}
	}
	

	/** Increment the number of tasks */
	public synchronized void incrementTasks() {
		numTasks++;
	}

	/** Decrement the number of tasks. Call notifyAll() if no pending work left.*/
	public synchronized void decrementTasks() {
		numTasks--;
		if (numTasks <= 0)
			notifyAll();
	}

	/** Wait for all pending work to finish */
	public synchronized void waitUntilFinished() {
		while (numTasks > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/** Wait until there is no pending work, then shutdown the queue */
	public synchronized void shutdown() {
		waitUntilFinished();
		workQueue.shutdown();
	}
	
	
	
	
	
	

}

