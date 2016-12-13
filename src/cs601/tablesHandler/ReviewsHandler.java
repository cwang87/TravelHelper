package cs601.tablesHandler;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import cs601.modelData.ReviewDB;
import cs601.modelData.ReviewHotelName;
import cs601.sqlHelper.SqlHelper;
import cs601.util.Status;
import cs601.util.Tools;


/**
 * A class - contains methods to manipulate data from reviews table in mySql. 
 */

public class ReviewsHandler {

	private static ReviewsHandler reviewsHandler = new ReviewsHandler();
	
	
	private static final String ADD_REVIEW = "INSERT INTO reviews (hotelId, reviewId,username, reviewTitle, reviewText, "
			+ "isRecom, overallRating, userId) VALUE (?,?,?,?,?,?,?,?);";
	
	private static final String DELETE_REVIEW = "DELETE FROM reviews WHERE reviewId = ?;";
	
	private static final String SEARCH_USER_REVIEW_USERNAME = "SELECT reviews.hotelId, reviewId, username, reviewTitle, "
			+ "reviewText, reviewDate, isRecom, overallRating, userId, likeCount, hotels.hotelName "
			+ "FROM reviews LEFT OUTER JOIN hotels "
			+ "ON reviews.hotelId = hotels.hotelId "
			+ "WHERE reviews.username = ?;";
	
	private static final String UPDATE_REVIEW = "UPDATE reviews SET reviewTitle = ? , reviewText = ? , isRecom = ? , "
			+ "overallRating = ? WHERE reviewId = ? ;";
	
	
//	private static final String SEARCH_PESONAL_REVIEW = "SELECT * FROM reviews WHERE username = ?;";
	
	private static final String SEARCH_REVIEW_REVIEWID = "SELECT * FROM reviews WHERE reviewId = ?;";
	
	private static final String SEARCH_REVIEW_HOTELID = "SELECT * FROM reviews WHERE hotelId = ?;";
	
	private static final String SEARCH_REVIEW_HOTELID_SORTBYDATE = "SELECT * FROM reviews WHERE hotelId=? "
			+ "ORDER BY reviewDate DESC;";
	
	private static final String SEARCH_REVIEW_HOTELID_SORTBYRATING = "SELECT * FROM reviews WHERE hotelId=? "
			+ "ORDER BY overallRating DESC;";
	
	private static final String HAS_REVIEW_HOTELID = "SELECT hotelId FROM reviews WHERE hotelId = ?;";
	
	private static final String HAS_REVIEW_USERNAME = "SELECT username FROM reviews WHERE username = ?;";
	
	
	private ReviewsHandler() {
	}
	
	
	
	/*-----------------------------------------get Singleton Instance------------------------------------------*/
	
	/** Gets the single instance of the database handler. */
	public static ReviewsHandler getInstance() {
		return reviewsHandler;
	}
	
	
	/*---------------------------------------------Get ReviewLists-------------------------------------------*/
	
	/** Get all reveiws of a particular hotel ordered by likecount desc as a default order. */
	public ArrayList<ReviewDB> getHotelReviews(String hotelId){
		ArrayList<ReviewDB> reviews = new ArrayList<>();
		
		if(hasReviewHotelId(hotelId)){
			
			ResultSet rs = SqlHelper.executeQuery(SEARCH_REVIEW_HOTELID, hotelId);
			
			reviews = parseReview(rs);
			
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return reviews;
	}
	
	
	/** Get all reveiws of a particular hotel ordered by reviewDate. */
	public ArrayList<ReviewDB> getHotelReviewsSortByDate(String hotelId){
		ArrayList<ReviewDB> reviews = new ArrayList<>();
		
		if(hasReviewHotelId(hotelId)){
			
			ResultSet rs = SqlHelper.executeQuery(SEARCH_REVIEW_HOTELID_SORTBYDATE, hotelId);
			
			reviews = parseReview(rs);
			
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return reviews;
	}
	
	
	/** Get all reveiws of a particular hotel ordered by overallRating. */
	public ArrayList<ReviewDB> getHotelReviewsSortByRating(String hotelId){
		ArrayList<ReviewDB> reviews = new ArrayList<>();
		
		if(hasReviewHotelId(hotelId)){
			
			ResultSet rs = SqlHelper.executeQuery(SEARCH_REVIEW_HOTELID_SORTBYRATING, hotelId);
			
			reviews = parseReview(rs);
			
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return reviews;
	}
	
	
	
	
	private ArrayList<ReviewDB> parseReview(ResultSet rs){
		ArrayList<ReviewDB> reviews = new ArrayList<>();
		try {
			while(rs.next()){
				String hotelId = rs.getString(1);
				String reviewId = rs.getString(2);
				String username = rs.getString(3);
				String reviewTitle = rs.getString(4);
				String reviewText = rs.getString(5);
				Date reviewDate = rs.getTimestamp(6);
				boolean isRecom= Tools.int2bool(rs.getInt(7));
				int overallRating = rs.getInt(8);
				int userId = rs.getInt(9);
				int likeCount = rs.getInt(10);
				
				ReviewDB review = new ReviewDB(hotelId, reviewId, username, reviewTitle, reviewText,
						Tools.toStringDate(reviewDate), Tools.bool2yn(isRecom), Integer.toString(overallRating), 
						Integer.toString(userId), Integer.toString(likeCount));
				
				reviews.add(review);
			}
		}catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}
		
		return reviews;
	}
	
	
	
	
	
	/** given a username, return all reviews writen by this user */
	public ArrayList<ReviewHotelName> getReviewsUserName (String username){
		
		ArrayList<ReviewHotelName> reviews = new ArrayList<>();
		
		if(hasReviewUsername(username)){
			ResultSet rs = SqlHelper.executeQuery(SEARCH_USER_REVIEW_USERNAME, username);
			try {
				while(rs.next()){
					String hotelId = rs.getString(1);
					String reviewId = rs.getString(2);
					String reviewTitle = rs.getString(4);
					String reviewText = rs.getString(5);
					Date reviewDate = rs.getTimestamp(6);
					boolean isRecom= Tools.int2bool(rs.getInt(7));
					int overallRating = rs.getInt(8);
					int userId = rs.getInt(9);
					int likeCount = rs.getInt(10);
					String hotelName = rs.getString(11);
					
					ReviewHotelName review = new ReviewHotelName(hotelId, reviewId, username, reviewTitle, reviewText,
							Tools.toStringDate(reviewDate), Tools.bool2yn(isRecom), Integer.toString(overallRating), 
							Integer.toString(userId), Integer.toString(likeCount), hotelName);
					
					reviews.add(review);
				}
			}catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}finally {
				SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
			}
		}
		
		return reviews;
	}
	
	
	
	
	
	/*-----------------------------------------add / delete / update review----------------------------------------------*/
	
	/** update review */
	public Status updateReview(String reviewTitle, String reviewText, int isRecom, int overallRating, String reviewId){
		Status status = Status.ERROR;
		
		Connection ct = SqlHelper.getConnection();
		PreparedStatement ps = null;
		
		try {
			ps = ct.prepareStatement(UPDATE_REVIEW);
			ps.setString(1, reviewTitle);
			ps.setString(2, reviewText);
			ps.setInt(3, isRecom);
			ps.setInt(4, overallRating);
			ps.setString(5, reviewId);
			ps.executeUpdate();
			status = Status.OK;
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			status = Status.SQL_EXCEPTION;
		} finally {
			try {
				ps.close();
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			
		}
		return status;
	}
	
	
	
	
	/** insert a new review into databas */
	public Status addReview (String hotelId, String reviewId, String username, String reviewTitle, String reviewText,
			int isRecom, int rating, int userId){
		
		Status status = Status.ERROR;
		Connection ct = SqlHelper.getConnection();
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
			
			status = Status.OK;
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			status = Status.SQL_EXCEPTION;
		} finally {
			try {
				ps.close();
				ct.close();
			} catch (SQLException e) {
				System.out.println(Status.SQL_EXCEPTION + e.getMessage());
			}
			
		}
		
		return status;
	}
	
	
	/**delete a review */
	public Status deleteReview(String reviewId){
		Status status = Status.ERROR;
		String[] parameters = {reviewId};
		boolean delete = SqlHelper.executeUpdate(DELETE_REVIEW, parameters);
		if(delete){
			status = Status.OK;
		}
		return status;
	}
	
	

	/*-------------------------------------------------query about reviews---------------------------------------------------*/
	
	/** get review record by given reviewId*/
	public ReviewDB getReviewByReviewId(String reviewId){
		ReviewDB review = null;
		
		ResultSet rs = SqlHelper.executeQuery(SEARCH_REVIEW_REVIEWID, reviewId);
		try {
			while(rs.next()){
				String hotelId = rs.getString(1);
				String username = rs.getString(3);
				String reviewTitle = rs.getString(4);
				String reviewText = rs.getString(5);
				Date reviewDate = rs.getTimestamp(6);
				boolean isRecom= Tools.int2bool(rs.getInt(7));
				int overallRating = rs.getInt(8);
				int userId = rs.getInt(9);
				int likeCount = rs.getInt(10);
				
				review = new ReviewDB(hotelId, reviewId, username, reviewTitle, reviewText,
						Tools.toStringDate(reviewDate), Tools.bool2yn(isRecom), Integer.toString(overallRating),
						Integer.toString(userId), Integer.toString(likeCount));
				
			}
		}catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return review;
	}
	
	
	
	
	/** check if a given hotel has reviews */
	public boolean hasReviewHotelId(String hotelId){
		boolean hasReview = false;
		
		ResultSet rs = SqlHelper.executeQuery(HAS_REVIEW_HOTELID, hotelId);
		
		try {
			while(rs.next()){
				hasReview = true;
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hasReview;
	}
	
	
	
	
	
	
	
	/** check if a given username has reviews */
	public boolean hasReviewUsername(String username){
		boolean hasReview = false;
		
		ResultSet rs = SqlHelper.executeQuery(HAS_REVIEW_USERNAME, username);
		
		try {
			while(rs.next()){
				hasReview = true;
			}
		} catch (SQLException e) {
			System.out.println(Status.SQL_EXCEPTION + e.getMessage());
		}finally {
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPs(), SqlHelper.getCt());
		}
		
		return hasReview;
	}
	
	
	
	
	
	
	
	
}

	
	
	


