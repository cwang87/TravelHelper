package cs601.controller.search;

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
import cs601.tableData.ReviewDB;
import cs601.tablesHandler.ReviewsHandler;


/**
 * Reviews Servlet: a servlet handle requests of viewing all reviews about a particular hotel.
 */

@SuppressWarnings("serial")
public class ReviewsServlet extends BaseServlet {
	
	private static final ReviewsHandler reviewsHandler = ReviewsHandler.getInstance();
	
	
	/** Process GET request: display a full list of all reviews about the requested hotel*/
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/reviews.html");
		
		
		// get hotelId and check if this hotel has reviews
		String hotelId = request.getParameter("hotelId");
		
		if(!reviewsHandler.hasReviewHotelId(hotelId)){
			context.put("noReviewMessage", "This hotel has no reviews!");
		
		}else{
			ArrayList<ReviewDB> reviewList = reviewsHandler.getHotelReviews(hotelId);
			context.put("reviewList", reviewList);
		}
		
		
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}
	
	
	
	
	
//	/* write the review info about the hotel into html table */
//	private void createTbl(PrintWriter out, String hotelId){
//		
//	
//		
//		//table title
//		out.println("<h3>" + hotelName + "</h3>");
//		out.println("<p>" + "Average Rating: " + aveRating + "</p>");
//		out.println("<p>" + "Hotel Address: " + hotelAddr + "</p>");
//		out.println("<style>table, th, td {border: 1px solid black;}</style>");
//		out.println("<table>");
//		
//		//table head
//		out.println("<tr>");
//		out.println("<th>Username/th>");
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
//			String oneReview = addReviewTbl(review.getUsername(), review.getReviewTitle(), review.getReviewText(), 
//					Tools.toStringDate(review.getReviewDate()), Tools.bool2yn(review.getIsRecom()), 
//					Integer.toString(review.getOverallRating()));	
//			sb.append(oneReview);
//		}
//		out.println(sb.toString());
//		out.println("</table>");
//		
//	}
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
	
	
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

}
