package cs601.controller.search;

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
 * Reviews Servlet: a servlet handle requests of viewing all reviews about a particular hotel.
 */

@SuppressWarnings("serial")
public class ReviewsServlet extends BaseServlet {
	
	private static final ReviewsHandler reviewService = ReviewsHandler.getInstance();
	private static final HotelsHandler hotelService = HotelsHandler.getInstance();
	
	
	
	/** Process GET request: display a full list of all reviews about the requested hotel*/
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("Review List", response);
		
		PrintWriter out = response.getWriter();

		checkRequestError(request, out);
		
		String hotelId = request.getParameter("hotelId");
		
		
		if(!checkSession(request)){
			out.println("Welcome to Hotel Discover Channel!  ");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/register'}\">Register</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/login'}\">Login</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">Back to Search</button>");
			
		}else{
			String username = getSessionUsername(request);
			out.println("Hello!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">Back to Hotels</button>");
		}
		
		createTbl(out, hotelId);	
		
		finishResponse(response);
		
	}
	
	
	
	/* write the review info about the hotel into html table */
	private void createTbl(PrintWriter out, String hotelId){
		
		ArrayList<ReviewPO> reviews = reviewService.searchReviews(hotelId);
		HotelPO hotel = hotelService.getHotelPO(hotelId);
		String hotelName = hotel.getHotelName();
		String hotelAddr = hotel.getHotelAddress().toString();
		String aveRating = Double.toString(hotelService.getAvgRating(hotelId));
		
		//table title
		out.println("<h3>" + hotelName + "</h3>");
		out.println("<p>" + "Average Rating: " + aveRating + "</p>");
		out.println("<p>" + "Hotel Address: " + hotelAddr + "</p>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
		out.println("<table>");
		
		//table head
		out.println("<tr>");
		out.println("<th>Username/th>");
		out.println("<th>Review Title</th>");
		out.println("<th>Review Text</th>");
		out.println("<th>Review Date</th>");
		out.println("<th>Recom</th>");
		out.println("<th>Rating</th>");
		out.println("</tr>");
		
		StringBuffer sb = new StringBuffer();
		
		for(ReviewPO review: reviews){
			
			String oneReview = addReviewTbl(review.getUsername(), review.getReviewTitle(), review.getReviewText(), 
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
	
	
	
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

}
