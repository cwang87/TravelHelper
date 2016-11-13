package cs601.model.PO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class - represent the "reviews" table in database
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
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
		overallRating = rating;
		isRecommended = isRecom;
		// the username is defaulted as anonymous if absent
		if (username.isEmpty())
			this.username = "anonymous";
		else
			this.username = username;
		// parse the date
		reviewDate = parseDate(date);
	}
	

	public Date parseDate(String reviewDate) {
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = parser.parse(reviewDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public String getNewReviewDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM:dd:yy");
		String date = formatter.format(reviewDate);
		return date;
	}
	
	
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

	public String getReview() {
		return "--------------------\nReview by " + username + ": " + Integer.toString(overallRating) + "\n"
				+ reviewTitle + "\n" + reviewText + "\n";
	}

	//getters
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

	//setters
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}


	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}


	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}


	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}


	public void setRecommended(boolean isRecommended) {
		this.isRecommended = isRecommended;
	}


	public void setOverallRating(int overallRating) {
		this.overallRating = overallRating;
	}
	
	

}

