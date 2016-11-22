package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.controller.main.BaseServlet;
import cs601.model.HotelPO;
import cs601.tablesHandler.HotelsHandler;
import cs601.tablesHandler.ReviewsHandler;
import cs601.tablesHandler.UsersHandler;
import cs601.util.Tools;


/**
 * A servlet - handle "add review" request
 */
@SuppressWarnings("serial")
public class AddReviewServlet extends BaseServlet {
	
	private static final ReviewsHandler reviewsHandler = ReviewsHandler.getInstance();
	private static final HotelsHandler hotelsHandler = HotelsHandler.getInstance();
	private static final UsersHandler usersHandler = UsersHandler.getInstance();
	
	String hotelId = null;
	
	
	/** display a full list of hotels for user to choose and add a review*/
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("Add reviews", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		//check session
		if(!checkSession(request)){
			redirect(response, "/user/login");
		}
		
		String username = getSessionUsername(request);
		hotelId = request.getParameter("hotelId");
		
		if(hotelId == null){
			
			out.println("Hello!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			hotelListTbl(out);
			
		}else{
			out.println("Hello!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			displayForm(out,hotelId);
		}
		
		finishResponse(response);
		
	}
	
	
	

	
	/** get review info from request and add review info into database.*/
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("Submit Review", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);

		// check session
		if(!checkSession(request)){
			redirect(response, "/user/login");
		}
		
		String username = getSessionUsername(request);
		out.println("Hello!  " + username);
		out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");

		// get parameters
		String reviewTitle = request.getParameter("title");
		String reviewText = request.getParameter("text");
		int isRecom = Tools.yn2int(request.getParameter("recom"));
		int rating = Integer.parseInt(request.getParameter("rating"));
		int userId = usersHandler.getUserId(username);

		// generate reviewId
		String reviewId = Tools.getUniqueId(username);

		reviewsHandler.addReview(hotelId, reviewId, username.toLowerCase(), reviewTitle, reviewText, isRecom, rating,
				userId);

		out.println("<p><br>Successfully added one review!</p>");			

		finishResponse(response);
	}
		
	/*-----------------------------Display hotel list for user to choose-------------------------------*/
	
	private void hotelListTbl(PrintWriter out){
		out.println("<h3>Please choose the hotel to be add a review</h3>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
		out.println("<table>");
		
		//table head
		out.println("<tr>");
		out.println("<th>Hotel ID</th>");
		out.println("<th>Hotel Name</th>");
		out.println("<th>Hotel Address</th>");
		out.println("</tr>");
		
		//get table js string
		String table = hotelsHandler.getHotels_Select();
		
		out.println(table);
		out.println("</table>");
		
	}
		
	
	
	
	
	
	
	/*-----------------------------Display form for user to add review----------------------------------*/
	
	private void displayForm(PrintWriter out, String hotelId){
		
		HotelPO hotel =  hotelsHandler.getHotelPO(hotelId);
		
		out.println("<p style=\"font-size: 16pt; font-style: italic; font-color:green；\">" + hotel.getHotelName() +"</p>");
		
		out.println("<form action=\"/user/add_review\" method=\"post\">"); 
		out.println("<table border=\"0\">");
				
		out.println("<tr>");
		out.println("<td>Recommend: </td>");
		out.println("<td><input type=\"radio\" name=\"recom\" value=\"YES\">Yes");
		out.println("<input type=\"radio\" name=\"recom\" value=\"NO\">No</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td>Overall Rating: </td>");
		out.println("<td><input type=\"radio\" name=\"rating\" value=\"5\">5");
		out.println("<input type=\"radio\" name=\"rating\" value=\"4\">4");
		out.println("<input type=\"radio\" name=\"rating\" value=\"3\">3");
		out.println("<input type=\"radio\" name=\"rating\" value=\"2\">2");
		out.println("<input type=\"radio\" name=\"rating\" value=\"1\">1</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td>Title:</td>");
		out.println("<td><input type=\"text\" name=\"title\" size=\"100\"></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td valign=\"top\">Review: </td>");
		out.println("<td><textarea name=\"text\" cols=\"99\" rows=\"10\">your valuable reviews here!</textarea></td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("<p><input type=\"submit\" value=\"Submit\"></p>");
		out.println("</form>");
		
	}
	
	
	
	
		

}
