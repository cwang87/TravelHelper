package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.controller.main.BaseServlet;


/**
 * Servlet to handle login out
 */

@SuppressWarnings("serial")
public class LogoutServlet extends BaseServlet {
	
	
	/**
	 * invalid session and let users logout safely
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

	

	
	/**
	 * invalid session and let users logout safely
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
	
	
}
