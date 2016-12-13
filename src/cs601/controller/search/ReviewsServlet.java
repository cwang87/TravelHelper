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
import cs601.modelData.ReviewDB;
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
		
		
		// get hotelId sortType and check if this hotel has reviews
		String hotelId = request.getParameter("hotelId");
		String sortType = request.getParameter("sortType");
		
		if(!reviewsHandler.hasReviewHotelId(hotelId)){
			context.put("noReviewMessage", "This hotel has no reviews!");
			context.put("display", "none");
		
		}else if(sortType == null || sortType.isEmpty()){
			ArrayList<ReviewDB> reviewList = reviewsHandler.getHotelReviews(hotelId);
			context.put("reviewList", reviewList);
			context.put("hotelId", hotelId);
		}else if(sortType.equals("reviewDate")){
			ArrayList<ReviewDB> reviewList = reviewsHandler.getHotelReviewsSortByDate(hotelId);
			context.put("reviewList", reviewList);
			context.put("hotelId", hotelId);
		}else if(sortType.equals("overallRating")){
			ArrayList<ReviewDB> reviewList = reviewsHandler.getHotelReviewsSortByRating(hotelId);
			context.put("reviewList", reviewList);
			context.put("hotelId", hotelId);
		}else{
			context.put("noReviewMessage", "Not valid sorting!");
			context.put("display", "none");
		}
		
		
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}
	
	
	
	
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

}
