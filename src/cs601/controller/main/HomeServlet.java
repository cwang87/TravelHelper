package cs601.controller.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet to handle home page
 */

@SuppressWarnings("serial")
public class HomeServlet extends BaseServlet {
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		prepareResponse("Home Page", response);

		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);

		getBody(out); 
		
		finishResponse(response);
	}


	
	
	
	/** Writes and HTML form that shows two textfields and a button to the PrintWriter */
	private void getBody(PrintWriter out) {
		assert out != null;
		
		out.println("<p style=\"font-size: 25pt;\">");
		out.println("Welcome to this wonderful website!<br><br>");
		out.println("</p>");
		
		out.println("Please choose: ");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/register'}\">Register</button>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/login'}\">Login</button>");
		out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">Search</button>");
		out.println("<p><br><br><br><br><br></p>");
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}
	
	
	
	
	
}
