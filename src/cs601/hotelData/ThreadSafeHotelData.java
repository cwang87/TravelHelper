package cs601.hotelData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import cs601.concurrent.ReentrantReadWriteLock;
import cs601.util.*;

/**
 * Class ThreadSafeHotelData - a data structure that stores information about hotels and hotel reviews. 
 */
public class ThreadSafeHotelData {

	private final HashMap<String, Hotel> hotelMap;
	private final HashMap<String, TreeSet<Review>> reviewMap;
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private List<String> reviewIdList;
	private Set<String> username;

	public ThreadSafeHotelData() {
		hotelMap = new HashMap<>();
		reviewMap = new HashMap<>();
		reviewIdList = new ArrayList<>();
		username = new HashSet<>();
	}
	

	
	
	/*--------------------------------------------add hotel and review---------------------------------------------------*/
	
	/**
	 * Create a Hotel given the parameters, and add it to the hotelMap. Only one thread is allowed to write at a time.
	 */
	public void addHotel(String hotelId, String hotelName, String city, String state, String streetAddress, String country,  double lat,
			double lon) {

		lock.lockWrite();

		try {
			Hotel hotel = new Hotel(hotelId, hotelName, city, state, streetAddress, country, lat, lon);
			hotelMap.put(hotelId, hotel);
		} finally {
			lock.unlockWrite();
		}
	}

	
	
	
	
	/**
	 * Add a new review of a hotel into the reviewMap. 
	 */
	public boolean addReview(String hotelId, String reviewId, int rating, String reviewTitle, String review,
			boolean isRecom, String date, String username) {

		lock.lockWrite();
		try {
			if (!checkInvalidReviewDate(date) || !checkInvalidRating(rating)) {
				return false;
			} else {
				
				Review oneReview = new Review(hotelId, reviewId, rating, reviewTitle, review, isRecom, date, username);
				
				if (reviewMap.containsKey(hotelId)) {
					reviewMap.get(hotelId).add(oneReview);
				} else {
					TreeSet<Review> reviewsPerHotel = new TreeSet<Review>();
					reviewsPerHotel.add(oneReview);
					reviewMap.put(hotelId, reviewsPerHotel);
				}
				return true;
			}
		} catch (java.text.ParseException e) {
				e.printStackTrace();
				return false;
		}finally {
			lock.unlockWrite();
		}
		
	}
	
	
	private boolean checkInvalidReviewDate(String date) {
		// set parse strickly
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);

		Date reviewDate = new Date();
		
		try {
			reviewDate = format.parse(date);
		} catch (java.text.ParseException e) {
			System.out.println(reviewDate.toString() + ": " + e);
			return false;
		}

		return true;
	}
	
	
	
	
	private boolean checkInvalidRating(int rating) {
		if (rating > 5 || rating < 1){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	
	
	
	/*--------------------------------------------get full list from maps---------------------------------------------------*/
	

	/** A method to get an alphabetized list of the ids of all hotels.  */
	
	public List<String> getHotels() {
		lock.lockRead();
		try {
			List<String> hotelList = new ArrayList<String>(hotelMap.keySet());
			Collections.sort(hotelList);
			return hotelList;
		} finally {
			lock.unlockRead();
		}
	}
	
	
	/** A method to get all usernames in reviews map.  */
	
	public Set<String> getUsernames(){
		return username;
	}
	
	
	
	
	/*--------------------------------------------return parameters for SQL---------------------------------------------------*/
	
	
	/** a method to return parameters for loading a hotel information to database. */
	
	public void writeHotel(Connection ct, String sql, String hotelId){
		
		Hotel hotel = hotelMap.get(hotelId);
		
		PreparedStatement ps = null;

		try {
			ps = ct.prepareStatement(sql);
			ps.setString(1, hotelId);
			ps.setString(2, hotel.getHotelName());
			ps.setString(3, hotel.getHotelAddress().getCity());
			ps.setString(4, hotel.getHotelAddress().getState());
			ps.setString(5, hotel.getHotelAddress().getStreetAddress());
			ps.setString(6, hotel.getHotelAddress().getCountry());
			ps.setDouble(7, hotel.getHotelAddress().getLatitude());
			ps.setDouble(8, hotel.getHotelAddress().getLongitude());
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + ": " + e.getMessage());
			}
		}
	}
	
	
	
	
	
	
	/** a method to set parameters for a sql statement which insert reviews into table. */
	
	public void writeReview(Connection ct, String sql, String hotelId) {
		
		if (reviewMap.containsKey(hotelId)) {
			
			TreeSet<Review> reviewSet = reviewMap.get(hotelId);

			try {
				ct.setAutoCommit(false);
				
				for (Review r : reviewSet) {
					if (!reviewIdList.contains(r.getReviewId())) {
						PreparedStatement pStatement = ct.prepareStatement(sql);
						pStatement.setString(1, hotelId);
						pStatement.setString(2, r.getReviewId().trim());
						pStatement.setString(3, r.getUsername().toLowerCase().trim());
						pStatement.setString(4, r.getReviewTitle().trim());
						pStatement.setString(5, r.getReviewText().trim());
						pStatement.setTimestamp(6, Tools.getTimestamp(r.getReviewDate()));
						pStatement.setInt(7, Tools.bool2int(r.getIsRecommended()));
						pStatement.setInt(8, r.getOverallRating());
						pStatement.executeUpdate();
						
						reviewIdList.add(r.getReviewId());
						username.add((r.getUsername()).toLowerCase());
					}
				}
				ct.commit();
			} catch (Exception e) {
				try {
					ct.rollback();
				} catch (SQLException e1) {
					System.out.println(Status.SQL_EXCEPTION + ": " + e1.getMessage());
				}
				System.out.println(e.getMessage());
			}
		}
	}

	
	
	
	
	/*--------------------------------------------to String and other methods---------------------------------------------------*/
	
	
	/** Returns a string representing information about the hotel and all related reviews with the given id. */
	
	public String toString(String hotelId) {

		lock.lockRead();

		try {
			StringBuffer sBuffer = new StringBuffer();

			if (hotelMap.containsKey(hotelId)) {
				sBuffer.append(hotelMap.get(hotelId).toString());
			}

			if (reviewMap.containsKey(hotelId)) {
				TreeSet<Review> onereview = reviewMap.get(hotelId);
				for (Review r : onereview) {
					sBuffer.append(r.toString());
				}
			}
			
			return sBuffer.toString();
			
		} finally {
			lock.unlockRead();
		}
	}
	
	
	
	
	
	
	/** A method to merger the "small" map to the "big" map.  */

	public void mergeReviewMap(ThreadSafeHotelData localData) {
		lock.lockWrite();
		try {
			reviewMap.putAll(localData.reviewMap);
		} finally {
			lock.unlockWrite();
		}
	}
	
	
	
	
	
}


