package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.controller.main.BaseServlet;
import cs601.model.HotelPO;
import cs601.service.HotelService;
import cs601.util.Tools;

/**
 * handler to show a full hotel list to user, including hotel addr and average rating
 */

@SuppressWarnings("serial")
public class HotelsServlet extends BaseServlet {

	
	private static final HotelService hotelService = HotelService.getInstance();
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out = response.getWriter();
		checkRequestError(request, out);
		prepareRespTbl("Hotel List", response);
		
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
			
		}else{
			String username = (String) session.getAttribute("username");
			out.println("Welcome!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
		}
		
		createTbl(out);
		
		finishResponse(response);
	}	
	
	
	
	private void createTbl(PrintWriter out){
		out.println("<h3>Hotel List</h3>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<table>");
		
		//table head
		out.println("<tr>");
		out.println("<th>Hotel ID</th>");
		out.println("<th>Hotel Name</th>");
		out.println("<th>Hotel Address</th>");
		out.println("<th>Hotel Ave Rating</th>");
		out.println("</tr>");
		
		List<String> hotelList = hotelService.getHotelList();
		
		StringBuffer sb = new StringBuffer();
		
		for(String hotelId:hotelList){
			if(hotelService.hasReviewHotelId(hotelId)){
				HotelPO hotelPO =  hotelService.getHotelPO(hotelId);
				double aveRating = hotelService.getAvgRating(hotelId);
				String oneHolel = addHotelTbl1(hotelId, hotelPO.getHotelName(), hotelPO.getHotelAddress().toString(), 
						Tools.formatDouble(aveRating));
				sb.append(oneHolel);
			}else{
				HotelPO hotelPO =  hotelService.getHotelPO(hotelId);
				String oneHolel = addHotelTbl2(hotelId, hotelPO.getHotelName(), hotelPO.getHotelAddress().toString(), 
						"no reviews");
				sb.append(oneHolel);
			}
		}
		out.println(sb.toString());
		out.println("</table>");
		
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

	
	
}
