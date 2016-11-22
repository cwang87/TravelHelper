package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.controller.main.BaseServlet;


/**
 * A handler to guide user to manage account, including add review, display all
 * reviews the user has written and can also modify this written reviews.
 */

@SuppressWarnings("serial")
public class AccountServlet extends BaseServlet {
	
	
	/**
	 * when user request to access to their account, check if they have login already.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		prepareResponse("Account Management", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		//check session
		if(checkSession(request)){
			getBody(out, getSessionUsername(request));
		}else{
			redirect(response, "/user/login");
		}
		
		finishResponse(response);
		
	}
	
	

	
	
	private void getBody(PrintWriter out, String username){
		
		out.println("<p style=\"font-size: 18pt;\">");
		out.println("<h2>Hello, " + username  + "!<br><br></h2>");
		out.println("</p>");
		
		out.println("Safely logout<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">Logout</button>");
		out.println("<p><br></p>");
		
		out.println("View hotels, reviews and attractions<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">View Hotels</button>");
		out.println("<p><br></p>");
		
		out.println("Add new reviews<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/add_review'}\">Add reviews</button>");
		out.println("<p><br></p>");
		
		out.println("View and modify my reviews<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/my_review?username=" + username +"'}\">My Reviews</button>");
		out.println("<p><br></p>");
		
	}
	
	
	
	
	
	/**
	 * when user request to access to their account, check if they have login already.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
}
