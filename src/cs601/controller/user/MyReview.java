package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.controller.main.BaseServlet;
import cs601.model.HotelPO;
import cs601.model.ReviewPO;
import cs601.tablesHandler.HotelsHandler;
import cs601.tablesHandler.ReviewsHandler;
import cs601.util.Tools;


/**
 * a servlet - handle request of viewing and modifying already written reviews
 */
@SuppressWarnings("serial")
public class MyReview extends BaseServlet {
	
	private static final ReviewsHandler reviewService = ReviewsHandler.getInstance();
	private static final HotelsHandler hotelService = HotelsHandler.getInstance();
	
	
	/**
	 * display all the reviews from a particular user
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("My reviews", response);
		
		PrintWriter out = response.getWriter();

		checkRequestError(request, out);
		String username = "";
		
		if(!checkSession(request)){
			redirect(response, "/user/login");
		}else{
			username = getSessionUsername(request);
			out.println("Hello!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
		}
		
		createTbl(out, username);	
		
		finishResponse(response);
		
	}
	
	

	
	private void createTbl(PrintWriter out, String username){
		
		ArrayList<ReviewPO> reviews = reviewService.searchPersonalReviews(username);
	
		out.println("<h3> My Reviews</h3>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
		out.println("<table>");
		
		//table head
		out.println("<tr>");
		out.println("<th>Hotel Name</th>");
		out.println("<th>Review Title</th>");
		out.println("<th>Review Text</th>");
		out.println("<th>Review Date</th>");
		out.println("<th>Recom</th>");
		out.println("<th>Rating</th>");
		out.println("</tr>");
		
		StringBuffer sb = new StringBuffer();
		
		for(ReviewPO review: reviews){
			
			String hotelId = review.getHotelId();
			HotelPO hotel = hotelService.getHotelPO(hotelId);
			String hotelName = hotel.getHotelName();
			
			String oneReview = addReviewTbl(hotelName, review.getReviewTitle(), review.getReviewText(), 
					Tools.toStringDate(review.getReviewDate()), Tools.bool2yn(review.getIsRecom()), 
					Integer.toString(review.getOverallRating()));	
			sb.append(oneReview);
		}
		out.println(sb.toString());
		out.println("</table>");
		
	}	

	
	
	private String addReviewTbl(String username, String reviewTitle, 
			String reviewText, String reviewDate, String isRecom, String overallRating){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + username + "</td>");
		sb.append("<td>" + reviewTitle + "</td>");
		sb.append("<td width=\"45%\">" + reviewText + "</td>");
		sb.append("<td>" + reviewDate + "</td>");
		sb.append("<td>" + isRecom + "</td>");
		sb.append("<td>" + overallRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}
		
	
	
}
