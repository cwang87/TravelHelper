package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs601.controller.main.BaseServlet;


/**
 * Account servlet: handle request of accessing user's account.
 * In the account page, users can logout, view hotels, add review, view and modify reviews written by user.
 */

@SuppressWarnings("serial")
public class AccountServlet extends BaseServlet {
	
	
	/**
	 * Process GET request: 
	 * If user didn't login but still use the url to access account, user will be redirected to login page.
	 * If already logged in, user will access this account page.
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
	
	
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
}
