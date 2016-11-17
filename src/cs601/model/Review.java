package cs601.model;

import java.util.Date;

/**
 * A class - represent the one row record of table "reviews" in database
 */
public class Review implements Comparable<Review> {

	private String hotelId;
	private String reviewId;
	private String username;
	private String reviewTitle;
	private String reviewText;
	private Date reviewDate;
	private boolean isRecom;
	private int overallRating;
	private int userId;
	
	
	/** Constructor (convert the Data type of date from String to Date) */
	public Review(String hotelId, String reviewId, String username, String reviewTitle, String reviewText, Date reviewDate, 
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
	
	
//	/* ----------------------------------------*/
//
//	public void setHotelId(String hotelId) {
//		this.hotelId = hotelId;
//	}
//
//
//	public void setReviewId(String reviewId) {
//		this.reviewId = reviewId;
//	}
//
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//
//	public void setReviewTitle(String reviewTitle) {
//		this.reviewTitle = reviewTitle;
//	}
//
//
//	public void setReviewText(String reviewText) {
//		this.reviewText = reviewText;
//	}
//
//
//	public void setReviewDate(Date reviewDate) {
//		this.reviewDate = reviewDate;
//	}
//
//
//	public void setRecom(boolean isRecom) {
//		this.isRecom = isRecom;
//	}
//
//
//	public void setOverallRating(int overallRating) {
//		this.overallRating = overallRating;
//	}
//
//
//	public void setUserId(int userId) {
//		this.userId = userId;
//	}




}

