package cs601.tableData;

import java.util.Date;

/**
 * A class - represent the one row record of table "reviews" in database
 */
public class ReviewPO implements Comparable<ReviewPO> {

	private String hotelId;
	private String reviewId;
	private String username;
	private String reviewTitle;
	private String reviewText;
	private Date reviewDate;
	private boolean isRecom;
	private int overallRating;
	private int userId;
	
	
	/** Constructor (convert the Data type of date from String to Date)
	 * 
	 * @param hotelId
	 * @param reviewId
	 * @param username
	 * @param reviewTitle
	 * @param reviewText
	 * @param reviewDate
	 * @param isRecom
	 * @param overallRating
	 * @param userId
	 */
	public ReviewPO(String hotelId, String reviewId, String username, String reviewTitle, String reviewText, Date reviewDate, 
			boolean isRecom, int overallRating, int userId ) {
		
		this.hotelId = hotelId;
		this.reviewId = reviewId;
		this.username = username;
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
		this.reviewDate = reviewDate;
		this.isRecom = isRecom;
		this.overallRating = overallRating;
		this.userId = userId;
	}

	
	
	
	
	
	
	
	
	
	/** A method to decide the order of reviews stored in the data structure. */
	@Override
	public int compareTo(ReviewPO review) {
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

	
	
	
	
	
	/* ----------------------------------------*/

	public String getHotelId() {
		return hotelId;
	}


	public String getReviewId() {
		return reviewId;
	}


	public String getUsername() {
		return username;
	}


	public String getReviewTitle() {
		return reviewTitle;
	}


	public String getReviewText() {
		return reviewText;
	}


	public Date getReviewDate() {
		return reviewDate;
	}


	public boolean getIsRecom() {
		return isRecom;
	}


	public int getOverallRating() {
		return overallRating;
	}


	public int getUserId() {
		return userId;
	}
	




}

