package cs601.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import cs601.dao.DAO;
import cs601.model.Review;

public class ReviewService {

	private static ReviewService singleton = new ReviewService();
	
	private DAO dao;
	
	
	private static final String SEARCH_REVIEW = "SELECT * FROM reviews WHERE hotelId = ?";
	private static final String ADD_REVIEW = "INSERT INTO reviews (hotelId, username, "
			+ "reviewTitle, reviewText, reviewDate, isRecom, overallRating) VALUE (?,?,?,?,?,?,?,?)";
	
	
	public ReviewService() {
		dao = new DAO();
	}
	
	
	public static ReviewService getInstance() {
		return singleton;
	}
	
	
	
	public ArrayList<Review> searchReviews (String hotelId){
		
		ResultSet rSet = dao.executeQuery(SEARCH_REVIEW, hotelId);
		
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		try {
			while(rSet.next()){
				String reviewId = rSet.getString("reviewId");
				String username = rSet.getString("username");
				String title = rSet.getString("reviewTitle");
				String text = rSet.getString("reviewText");
				Date date = rSet.getDate("reviewDate");
				Boolean isRecom= rSet.getBoolean("isRecom");
				int rating = rSet.getInt("overallRating");
				
				Review r = new Review(hotelId, reviewId, rating, title, text, isRecom, date.toString(), username);
				
				reviews.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public void addReview (Review review){
		
		String hotelId = review.getHotelId();
		String reviewId = review.getReviewId();
		String username = review.getUsername();
		String title = review.getReviewTitle();
		String text = review.getReviewText();
		Date date = review.getReviewDate();
		Boolean isRecom= review.getIsRecom();
		int rating = review.getOverallRating();
		
		Connection ct = dao.createConnection();
		PreparedStatement ps;
		try {
			ps = ct.prepareStatement(ADD_REVIEW);
			ps.setString(1, hotelId);
			ps.setString(2, reviewId);
			ps.setString(3, username);
			ps.setString(4, title);
			ps.setString(5, text);
			ps.setDate(6, (java.sql.Date) date);
			ps.setBoolean(7, isRecom);
			ps.setInt(8, rating);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	

}
