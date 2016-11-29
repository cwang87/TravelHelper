package cs601.hotelData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class for reviews information of a hotel.
 */
public class Review implements Comparable<Review> {

	private String reviewId;
	private String hotelId;
	private String username;
	private String reviewTitle;
	private String reviewText;
	private Date reviewDate;
	private boolean isRecommended;
	private int overallRating;
	
	/**
	 * Constructor (convert the Data type of date from String to Date)
	 */
	public Review(String hotelId, String reviewId, int rating, String reviewTitle, String reviewText, boolean isRecom,
			String date, String username) throws ParseException {
		this.reviewId = reviewId;
		this.hotelId = hotelId;
		overallRating = rating;
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
		isRecommended = isRecom;
		reviewDate = toDateFull(date);
		this.username = formatUsername(username);
	}
	
	
	
	
	//format usernames for constructor
	private String formatUsername(String username){
		if (username.isEmpty()){
			String name = "anonymous";
			return name;
		}else{
			return username;
		}
	}
	
	
	//Date formats for constructor
	private Date toDateFull(String reviewDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = null;
		try {
			date = formatter.parse(reviewDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	
	
	
	
	
	
	
	
	
	/**  A method to decide the order of reviews stored in the data structure. */
	
	@Override
	public int compareTo(Review review) {
		if (reviewDate.compareTo(review.reviewDate) == 0)
			if (username.compareTo(review.username) == 0)
				return reviewId.compareTo(review.reviewId);
			else
				return username.compareTo(review.username);
		else
			return reviewDate.compareTo(review.reviewDate);
	}

	
	
	
	
	
	
	
	
	/**
	 * A method - to return a string representing information about a review.
	 * Format is as follows:
	 * -------------------- 
	 * Review by username: rating
	 * ReviewTitle
	 * ReviewText 
	 * -------------------- 
	 * Review by username: rating
	 * ReviewTitle
	 * ReviewText
	 * ...
	 */
	public String toString() {
		return "--------------------\nReview by " + username + ": " + Integer.toString(overallRating) + "\n"
				+ reviewTitle + "\n" + reviewText + "\n";
	}

	
	/*--------------------------------*/
	public String getReviewId() {
		return reviewId;
	}

	public String getHotelId() {
		return hotelId;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public String getReviewText() {
		return reviewText;
	}

	public String getUsername() {
		return username;
	}

	public int getOverallRating() {
		return overallRating;
	}

	public boolean getIsRecommended() {
		return isRecommended;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	
	

}

