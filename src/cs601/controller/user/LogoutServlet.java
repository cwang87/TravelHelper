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
	
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// invalid this session
		HttpSession session = request.getSession();
		session.invalidate();
		
		PrintWriter out = response.getWriter();
		prepareResponse("Logout", response);
		
		String error = request.getParameter("error");
		if(error != null) {
			String errorMessage = getStatusMessage(error);
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}else{
			// finished logout
			out.println("<p style=\"font-size: 18pt;\">");
			out.println("You have successfully logged out!");
			out.println("</p>");
		}
		
		out.println("<button type=\"button\" onclick=\"{location.href='/home'}\">Back to home page</button>");
		
		finishResponse(response);
		
	}

	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
	
	
}
