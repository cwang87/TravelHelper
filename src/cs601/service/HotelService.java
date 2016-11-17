package cs601.service;

import cs601.dao.DAO;

public class HotelService {

	
private static HotelService singleton = new HotelService();
	
	private DAO dao;
	
	
	private static final String SEARCH_HOTELLIST = "SELECT * FROM reviews WHERE hotelId = ?";
	private static final String ADD_REVIEW = "INSERT INTO reviews (hotelId, username, "
			+ "reviewTitle, reviewText, reviewDate, isRecom, overallRating) VALUE (?,?,?,?,?,?,?,?)";
	
	
	public HotelService() {
		dao = new DAO();
	}
	
	
	public static HotelService getInstance() {
		return singleton;
	}
	
	
	
	
	
	
	
	
	
}
