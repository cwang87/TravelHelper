package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tableData.HotelAveRate;
import cs601.tablesHandler.HotelsHandler;
import cs601.tablesHandler.ReviewsHandler;
import cs601.tablesHandler.UsersHandler;
import cs601.util.Status;
import cs601.util.Tools;


/**
 * Add review servlet: handle request from user to add a review about a paticular hotel
 */
@SuppressWarnings("serial")
public class AddReviewServlet extends BaseServlet {
	
	
	/** Process GET Resquest: to add a review about a particular hotel
	 * If user already logged in, a full list of hotels will be displayed to user to choose.
	 * If user already logged in and chosen hotel to write review about, a review form will be displayed
	 * 		 to user to input review info. After user submitting the form, doPost will process this new request.
	 * If user hasn't logged in yet, the user will be redirected to login page
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/addReview.html");
		
		String hotelId = request.getParameter("hotelId");
		String searchName = request.getParameter("searchName");
		
		if (checkRequestError(request) != null) {
			context.put("errorMessage", checkRequestError(request));
		} else if (checkSession(request) == null) {
			redirect(response, "/login");
		} else if (hotelId != null) {
			String hotelName = HotelsHandler.getInstance().getHotelName(hotelId);
			context.put("hintMessage", "Selected Hotel:");
			context.put("placeholder", "placeholder=\"" + hotelName +"\"");
			context.put("disabled", "disabled");
			context.put("displaySearch", "none");
			context.put("postHotelId", hotelId);
		} else if(searchName != null){
			List<HotelAveRate> hotelList = HotelsHandler.getInstance().getHotelsByPartialName(searchName);
			context.put("hintMessage", "Please select hotel:");
			context.put("hotelList", hotelList);
			context.put("displayForm", "none");
			context.put("placeholder", "placeholder=\"Hotel Name\"");
		} else {
			context.put("hintMessage", "Please find your hotel first:");
			context.put("displaySearch", "none");
			context.put("displayForm", "none");
			context.put("placeholder", "placeholder=\"Hotel Name\"");
		}

		
		
		StringWriter writer = new StringWriter();
		
		template.merge(context, writer);
		out.println(writer.toString());
		
	}
	
		

	
	/** Process  POST Request: receive form information submitted by user. 
	 * Parse requeset to get parameters and insert these info into database.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		String username = checkSession(request);
		String hotelId = request.getParameter("postHotelId");
		String reviewTitle = request.getParameter("reviewTitle");
		String reviewText = request.getParameter("reviewText");
		String isRecomS = request.getParameter("recom");
		String ratingS = request.getParameter("overallRating");
		
		if(!hotelId.isEmpty() && !username.isEmpty() && !reviewTitle.isEmpty() && !reviewText.isEmpty() && !isRecomS.isEmpty()
				&& !ratingS.isEmpty() ){
			int isRecom = Tools.yn2int(isRecomS);
			int rating = Integer.parseInt(ratingS);
			int userId = UsersHandler.getInstance().getUserId(username);
			String reviewId = Tools.getUniqueId(username);
			ReviewsHandler.getInstance().addReview(hotelId, reviewId, username.toLowerCase(), reviewTitle, reviewText, 
					isRecom, rating, userId);
			out.println("<p style=\"font-size:150%; font-family: fantasy; color: green;\">Successfully added one review!</p>");	
		}else{
			redirect(response, "/add_review?error=" + Status.INVALID_NEWREVIEW);
		}

		

				

	}
		
	
		

}
