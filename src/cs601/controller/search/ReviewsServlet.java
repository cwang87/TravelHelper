package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.controller.main.BaseServlet;
import cs601.model.HotelPO;
import cs601.model.ReviewPO;
import cs601.service.HotelService;
import cs601.service.ReviewService;
import cs601.util.Tools;


/** A servlet class - handle clients' request about reviews */

@SuppressWarnings("serial")
public class ReviewsServlet extends BaseServlet {
	
	private static final ReviewService reviewService = ReviewService.getInstance();
	private static final HotelService hotelService = HotelService.getInstance();
	
	
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String hotelId = request.getParameter("hotelId");
		PrintWriter out = response.getWriter();

		checkRequestError(request, out);
		
		prepareRespTbl("Review List", response);
		
		
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		if(pass == null){
			session.setAttribute("pass", "no");
			pass = (String)session.getAttribute("pass");
		}
		
		if(!pass.equals("ok")){
			out.println("Welcome!  ");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/register'}\">Register</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/login'}\">Login</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">Back to Search</button>");
			
		}else{
			String username = (String) session.getAttribute("username");
			out.println("Welcome!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">Back to Search</button>");
		}
		
		createTbl(out, hotelId);		
		finishResponse(response);
		
	}
	
	
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
		out.println("</head>");
		out.println("<body>");
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
					Tools.toStringDate1(review.getReviewDate()), Tools.boolToString(review.getIsRecom()), 
					Integer.toString(review.getOverallRating()));	
			sb.append(oneReview);
		}
		out.println(sb.toString());
		out.println("</table>");
		
	}
	
	
	
	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

}
