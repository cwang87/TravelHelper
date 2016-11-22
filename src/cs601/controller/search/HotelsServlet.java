package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.controller.main.BaseServlet;
import cs601.controller.main.JettyWebServer;
import cs601.model.HotelPO;
import cs601.tablesHandler.HotelsHandler;
import cs601.util.Tools;

/**
 * handler to show a full hotel list to user, including hotel addr and average rating
 */

@SuppressWarnings("serial")
public class HotelsServlet extends BaseServlet {

	
	private static final HotelsHandler hotelService = HotelsHandler.getInstance();
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("Hotel List", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		if(!checkSession(request)){
			out.println("Welcome to Hotel Discover Channel!  ");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/register'}\">Register</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/login'}\">Login</button>");
			
		}else{
			String username = getSessionUsername(request);
			out.println("Hello!  " + username);
			out.println("<button type=\"button\" onclick=\"{location.href='/user/account'}\">My Account</button>");
			out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
		}
		
		createTbl(out);
		
		finishResponse(response);
	}	
	
	
	
	private void createTbl(PrintWriter out){
		out.println("<h3>Hotel List</h3>");
		out.println("<style>table, th, td {border: 1px solid black;}</style>");
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
	
	
/*-----------------------------------------------Hotel Table----------------------------------------------------*/
	
	private String addHotelTbl1(String hotelId, String hotelName, 
			String hotelAddr, String aveRating){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + hotelId + "</td>");
		
		sb.append("<td>");
		sb.append("<a href=\"http://localhost:" + JettyWebServer.PORT2 + "/reviews?hotelId=" + hotelId + "\">");
		sb.append(hotelName + "</a></td>");
		
		sb.append("<td>" + hotelAddr + "</td>");
		sb.append("<td>" + aveRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	
	private String addHotelTbl2(String hotelId, String hotelName, 
			String hotelAddr, String aveRating){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + hotelId + "</td>");
		sb.append("<td>" + hotelName + "</td>");
		sb.append("<td>" + hotelAddr + "</td>");
		sb.append("<td>" + aveRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

	
	
}
