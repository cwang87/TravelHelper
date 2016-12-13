package cs601.controller.userSave;

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
import cs601.modelData.ReviewHotelName;
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
			redirect(response, "/login");
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
