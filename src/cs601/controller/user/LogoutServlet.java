package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.controller.main.BaseServlet;


/**
 * Logout servlet: handle request from user to logout from account
 */

@SuppressWarnings("serial")
public class LogoutServlet extends BaseServlet {
	
	
	/**
	 * Process GET Request: invalid session and realize logout
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("Logout", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		// invalid this session
		HttpSession session = request.getSession();
		session.invalidate();
		
		// finished logout
		out.println("<p style=\"font-size: 18pt;\">");
		out.println("You have successfully logged out!");
		out.println("</p>");
		
		out.println("<button type=\"button\" onclick=\"{location.href='/home'}\">Back to Home Page</button>");
		
		finishResponse(response);
		
	}

	

	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
	
	
}
