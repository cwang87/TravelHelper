package cs601.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import cs601.dao.DAO;
import cs601.model.ReviewPO;
import cs601.util.Status;
import cs601.util.Tools;

public class ReviewService {

	private static ReviewService reviewService = new ReviewService();
	private static HotelService hotelService;
	
	
	
	private static final String SEARCH_REVIEW = "SELECT * FROM reviews WHERE hotelId = ?;";
	
	private static final String SEARCH_PESONAL_REVIEW = "SELECT * FROM reviews WHERE username = ?;";
	
	private static final String ADD_REVIEW = "INSERT INTO reviews (hotelId, reviewId,username, reviewTitle, reviewText, "
			+ "isRecom, overallRating, userId) VALUE (?,?,?,?,?,?,?,?);";
	
	
	
	
	private ReviewService() {
		hotelService = HotelService.getInstance();
	}
	
	
	
	
	
	
	
	public static ReviewService getInstance() {
		return reviewService;
	}
	
	
	
	
	
	/** given hotelId, return all reviews of it */
	public ArrayList<ReviewPO> searchReviews (String hotelId){
		
		ArrayList<ReviewPO> reviews = new ArrayList<ReviewPO>();
		
		if(hotelService.hasReviewHotelId(hotelId)){
			ResultSet rs = DAO.executeQuery(SEARCH_REVIEW, hotelId);
			try {
				while(rs.next()){
					String reviewId = rs.getString(2);
					String username = rs.getString(3);
					String reviewTitle = rs.getString(4);
					String reviewText = rs.getString(5);
					Date reviewDate = rs.getTimestamp(6);
					boolean isRecom= Tools.intToBool(rs.getInt(7));
					int overallRating = rs.getInt(8);
					int userId = rs.getInt(9);
					
					ReviewPO review = new ReviewPO(hotelId, reviewId, username, reviewTitle, reviewText,
							reviewDate, isRecom, overallRating, userId);
					
					reviews.add(review);
				}
			}catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}finally {
				DAO.close(DAO.getRs(), DAO.getPs(), DAO.getCt());
			}
		}
		
		return reviews;
	}
	
	
	
	/** given hotelId, return all reviews of it */
	public ArrayList<ReviewPO> searchPersonalReviews (String username){
		
		ArrayList<ReviewPO> reviews = new ArrayList<ReviewPO>();
		
		if(hotelService.hasReviewUsername(username)){
			ResultSet rs = DAO.executeQuery(SEARCH_PESONAL_REVIEW, username);
			try {
				while(rs.next()){
					String hotelId = rs.getString(1);
					String reviewId = rs.getString(2);
					String reviewTitle = rs.getString(4);
					String reviewText = rs.getString(5);
					Date reviewDate = rs.getTimestamp(6);
					boolean isRecom= Tools.intToBool(rs.getInt(7));
					int overallRating = rs.getInt(8);
					int userId = rs.getInt(9);
					
					ReviewPO review = new ReviewPO(hotelId, reviewId, username, reviewTitle, reviewText,
							reviewDate, isRecom, overallRating, userId);
					
					reviews.add(review);
				}
			}catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}finally {
				DAO.close(DAO.getRs(), DAO.getPs(), DAO.getCt());
			}
		}
		
		return reviews;
	}
	
	
	
	
	
	
	
	
	
	
	public void addReview (String hotelId, String reviewId, String username, String reviewTitle, String reviewText,
			int isRecom, int rating, int userId){
		
		Connection ct = DAO.getConnection();
		PreparedStatement ps = null;
		try {
			ps = ct.prepareStatement(ADD_REVIEW);
			ps.setString(1, hotelId);
			ps.setString(2, reviewId);
			ps.setString(3, username);
			ps.setString(4, reviewTitle);
			ps.setString(5, reviewText);
			ps.setInt(6, isRecom);
			ps.setInt(7, rating);
			ps.setInt(8, userId);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		} finally {
			try {
				ps.close();
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			
		}	
	}
	
	
	
	
	
	
	
}

	
	
	


