package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.controller.main.BaseServlet;
import cs601.model.HotelPO;
import cs601.service.HotelService;
import cs601.service.ReviewService;
import cs601.service.UserService;
import cs601.util.Tools;

@SuppressWarnings("serial")
public class AddReviewServlet extends BaseServlet {
	
	private static final ReviewService reviewService = ReviewService.getInstance();
	private static final HotelService hotelService = HotelService.getInstance();
	private static final UserService userService = UserService.getInstance();
	
	String hotelId = null;
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out = response.getWriter();
		checkRequestError(request, out);
		
		hotelId = request.getParameter("hotelId");

		//check session
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		if(pass == null){
			session.setAttribute("pass", "no");
			pass = (String)session.getAttribute("pass");
		}
		
		
		if(!pass.equals("ok")){
			redirect(response, "/user/login");
			
		}else if(hotelId == null){
			String username = (String) session.getAttribute("username");
			prepareRespTbl("Write Review", response);
			out.println("Welcome!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			
			hotelPickTbl(out);
			
		}else{
			String username = (String) session.getAttribute("username");
			prepareRespTbl("Write Review", response);
			out.println("Welcome!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			
			reviewSubmitForm(out,hotelId);
		}
		finishResponse(response);
		
	}
	
	
	
	private void hotelPickTbl(PrintWriter out){
		out.println("<h3>Choose one hotel to write a review</h3>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<table>");
		
		//table head
		out.println("<tr>");
		out.println("<th>Hotel ID</th>");
		out.println("<th>Hotel Name</th>");
		out.println("<th>Hotel Address</th>");
		out.println("</tr>");
		
		List<String> hotelList = hotelService.getHotelList();
		
		for(String hotelId:hotelList){
			HotelPO hotelPO =  hotelService.getHotelPO(hotelId);
			addHotelTbl3(out, hotelId, hotelPO.getHotelName(), hotelPO.getHotelAddress().toString());
		}
		out.println("</table>");
		
	}
	
	
	private void reviewSubmitForm(PrintWriter out, String hotelId){
		
		
		HotelPO hotel =  hotelService.getHotelPO(hotelId);
		
		out.println("<p style=\"font-size: 16pt; font-style: italic; font-color:green；\">" + hotel.getHotelName() +"</p>");
		
		out.println("<form action=\"/user/add_review\" method=\"post\">"); 
		out.println("<table border=\"0\">");
		
//		out.println("<tr>");
//		out.println("<td>Hotel Id: </td>");
//		out.println("<td><input type=\"radio\" name=\"hotelId\" value=\""+ hotelId +"\">"+ hotelId + "</td>");
//		out.println("</tr>");
//		
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
		
		
//		
	}
	
	
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out = response.getWriter();
		checkRequestError(request, out);

		// check session
		HttpSession session = request.getSession();
		String pass = (String) session.getAttribute("pass");
		if (pass == null) {
			session.setAttribute("pass", "no");
			pass = (String) session.getAttribute("pass");
		}

		if (!pass.equals("ok")) {
			redirect(response, "/user/login");

		} else {
			String username = (String) session.getAttribute("username");

			prepareRespTbl("Submit Review", response);
			out.println("Welcome!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");

			// add new review to database

			String reviewTitle = request.getParameter("title");
			String reviewText = request.getParameter("text");

			int isRecom = Tools.YNToInt(request.getParameter("recom"));

			int rating = Integer.parseInt(request.getParameter("rating"));

			// get reviewId
			Random random = new Random(System.currentTimeMillis());
			String dateId = getDate2().substring(0, 13);
			int salt = random.nextInt(888);
			String reviewId = dateId + Integer.toString(salt);

			
			
			int userId = userService.getUserId(username);

			reviewService.addReview(hotelId, reviewId, username.toLowerCase(), reviewTitle, reviewText, isRecom, rating,
					userId);
			
			out.println("<p><br>Add review successfully!</p>");
			
			finishResponse(response);

		}
		
		
	}
		
		
		
		
		

}
