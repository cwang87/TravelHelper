package cs601.controller.user;

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

@SuppressWarnings("serial")
public class MyReview extends BaseServlet {
	
	
	private static final ReviewService reviewService = ReviewService.getInstance();
	private static final HotelService hotelService = HotelService.getInstance();
//	private static final UserService userService = UserService.getInstance();
	
	
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String username = request.getParameter("username");
		PrintWriter out = response.getWriter();

		checkRequestError(request, out);
		
		prepareRespTbl("My Review List", response);
		
		
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		if(pass == null){
			session.setAttribute("pass", "no");
			pass = (String)session.getAttribute("pass");
		}
		
		if(!pass.equals("ok")){
			redirect(response, "/user/login");
			
		}else{
			out.println("Welcome!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">Search</button>");
		}
		
		createTbl(out, username);	
		
		finishResponse(response);
		
	}
	
	

	
	private void createTbl(PrintWriter out, String username){
		
		ArrayList<ReviewPO> reviews = reviewService.searchPersonalReviews(username);
		
	
		out.println("<h3> My Reviews</h3>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<table>");
		
		//table head
		out.println("<tr>");
		out.println("<th>Hotel Name/th>");
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
					Tools.toStringDate1(review.getReviewDate()), Tools.boolToString(review.getIsRecom()), 
					Integer.toString(review.getOverallRating()));	
			sb.append(oneReview);
		}
		out.println(sb.toString());
		out.println("</table>");
		
	}	

}
