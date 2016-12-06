package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tableData.ReviewHotelName;
import cs601.tablesHandler.ReviewsHandler;


/**
 * MyReview servlet: handle request of viewing reviews written by the user.
 */
@SuppressWarnings("serial")
public class MyReviewServlet extends BaseServlet {
	
	private static final ReviewsHandler reviewsHandler = ReviewsHandler.getInstance();
	
	
	/**
	 * Process GET request: display a list of reviews written by the user
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
				
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/myReview.html");
		
		if(checkSession(request)!= null){
		    String username = checkSession(request);
		    ArrayList<ReviewHotelName> reviewList = reviewsHandler.getReviewsUserName(username);
		    if(reviewsHandler.hasReviewUsername(username)){
		    	context.put("reviewList", reviewList);
		    }else{
		    	context.put("noReviewMessage", "You haven't written any reviews yet!");
		    }
		}else{
			context.put("reloadParent", "parent.location.reload();");
		}
		
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
	}
		
	
		
	
	

	
//	private void createTbl(PrintWriter out, String username){
//		
//		ArrayList<ReviewDB> reviews = reviewsHandler.searchPersonalReviews(username);
//	
//		out.println("<h3> My Reviews</h3>");
//		out.println("<style>table, th, td {border: 1px solid black;}</style>");
//		out.println("<table>");
//		
//		//table head
//		out.println("<tr>");
//		out.println("<th>Hotel Name</th>");
//		out.println("<th>Review Title</th>");
//		out.println("<th>Review Text</th>");
//		out.println("<th>Review Date</th>");
//		out.println("<th>Recom</th>");
//		out.println("<th>Rating</th>");
//		out.println("</tr>");
//		
//		StringBuffer sb = new StringBuffer();
//		
//		for(ReviewDB review: reviews){
//			
//			String hotelId = review.getHotelId();
//			HotelPO hotel = hotelsHandler.getHotelPO(hotelId);
//			String hotelName = hotel.getHotelName();
//			
//			String oneReview = addReviewTbl(hotelName, review.getReviewTitle(), review.getReviewText(), 
//					Tools.toStringDate(review.getReviewDate()), Tools.bool2yn(review.getIsRecom()), 
//					Integer.toString(review.getOverallRating()));	
//			sb.append(oneReview);
//		}
//		out.println(sb.toString());
//		out.println("</table>");
//		
//	}	
//
//	
//	
//	private String addReviewTbl(String username, String reviewTitle, 
//			String reviewText, String reviewDate, String isRecom, String overallRating){
//		
//		StringBuffer sb = new StringBuffer();
//		
//		sb.append("<tr>");
//		sb.append("<td>" + username + "</td>");
//		sb.append("<td>" + reviewTitle + "</td>");
//		sb.append("<td width=\"45%\">" + reviewText + "</td>");
//		sb.append("<td>" + reviewDate + "</td>");
//		sb.append("<td>" + isRecom + "</td>");
//		sb.append("<td>" + overallRating + "</td>");
//		sb.append("</tr>");
//		
//		return sb.toString();
//		
//	}
//	
//	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}
		
	
	
}
