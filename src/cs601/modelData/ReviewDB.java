package cs601.modelData;


/**
 * A class - represent the one row record of table "reviews" in database
 */
public class ReviewDB implements Comparable<ReviewDB> {

	private String hotelId;
	private String reviewId;
	private String username;
	private String reviewTitle;
	private String reviewText;
	private String reviewDate;
	private String isRecom;
	private String overallRating;
	private String userId;
	private String likeCount;
	
	
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
	 * @param likeCount
	 */
	public ReviewDB(String hotelId, String reviewId, String username, String reviewTitle, String reviewText, String reviewDate, 
			String isRecom, String overallRating, String userId, String likeCount) {
		
		this.hotelId = hotelId;
		this.reviewId = reviewId;
		this.username = username;
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
		this.reviewDate = reviewDate;
		this.isRecom = isRecom;
		this.overallRating = overallRating;
		this.userId = userId;
		this.likeCount = likeCount;
	}


	
	
	
	
	
	
	
	/** A method to decide the order of reviews stored in the data structure. */
	@Override
	public int compareTo(ReviewDB review) {
		if (reviewDate.compareTo(review.reviewDate) == 0)
			if (username.compareTo(review.username) == 0)
				return reviewId.compareTo(review.reviewId);
			else
				return username.compareTo(review.username);
		else
			return reviewDate.compareTo(review.reviewDate);
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


	public String getReviewDate() {
		return reviewDate;
	}


	public String getIsRecom() {
		return isRecom;
	}


	public String getOverallRating() {
		return overallRating;
	}


	public String getUserId() {
		return userId;
	}


	public String getLikeCount() {
		return likeCount;
	}
	








}

