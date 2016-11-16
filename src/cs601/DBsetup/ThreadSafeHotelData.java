package cs601.DBsetup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import cs601.util.*;

/**
 * Class ThreadSafeHotelData - a data structure that stores information about hotels and hotel reviews. 
 */
public class ThreadSafeHotelData {

	private final HashMap<String, Hotel> hotelMap;
	private final HashMap<String, TreeSet<Review>> reviewMap;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private List<String> reviewIdList;

	public ThreadSafeHotelData() {
		hotelMap = new HashMap<>();
		reviewMap = new HashMap<>();
		reviewIdList = new ArrayList<>();
	}
	

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
	
	
	
	
	
	
	
	
	
	/** a method to return parameters for loading a hotel information to database. */
	
	public String[] getHotelParas(String hotelId){
		String[] hotelParas = new String[6];
		
		Hotel hotel = hotelMap.get(hotelId);
		hotelParas[0] = hotelId;
		hotelParas[1] = hotel.getHotelName();
		hotelParas[2] = hotel.getHotelAddress().getCity();
		hotelParas[3] = hotel.getHotelAddress().getState();
		hotelParas[4] = hotel.getHotelAddress().getStreetAddress();
		hotelParas[5] = hotel.getHotelAddress().getCountry();
		
		return hotelParas;
	}
	
	
	
	
	
	
	/** a method to set parameters for a sql statement which insert reviews into table. */
	
	public void insertReviews(Connection connection, String sql, String hotelId) {
		
		if (reviewMap.containsKey(hotelId)) {
			TreeSet<Review> reviewSet = reviewMap.get(hotelId);

			try {
				connection.setAutoCommit(false);
				for (Review r : reviewSet) {
					if (!reviewIdList.contains(r.getReviewId())) {
						PreparedStatement pStatement = connection.prepareStatement(sql);
						pStatement.setString(1, hotelId);
						pStatement.setString(2, r.getReviewId());
						pStatement.setString(3, r.getUsername());
						pStatement.setString(4, r.getReviewTitle());
						pStatement.setString(5, r.getReviewText());
						pStatement.setTimestamp(6, Tools.getTimestamp(r.getReviewDate()));
						pStatement.setInt(7, Tools.toSQLBoolean(r.getIsRecommended()));
						pStatement.setInt(8, r.getOverallRating());
						pStatement.executeUpdate();
						reviewIdList.add(r.getReviewId());
					}
				}
				connection.commit();
			} catch (Exception e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
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
	
	
	
	
	
	
}


