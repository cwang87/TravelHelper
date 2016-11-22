package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.controller.main.BaseServlet;
import cs601.tablesHandler.HotelsHandler;

/**
 * handler to show a full hotel list to user, including hotel addr and average rating
 */

@SuppressWarnings("serial")
public class HotelsServlet extends BaseServlet {

	
	private static final HotelsHandler hotelsHandler = HotelsHandler.getInstance();
	
	
	/**
	 * display a full list of hotels with average rating when receive request
	 */
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
		
		//get table js string
		String table = hotelsHandler.getHotels_Avg();
		
		out.println(table);
		out.println("</table>");
		
	}

	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

	
	
}
