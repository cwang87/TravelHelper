package cs601.controller.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Home page Servlet to handle requests from home page
 */

@SuppressWarnings("serial")
public class HomeServlet extends BaseServlet {
	
	
	/**
	 * process GET Request: 
	 * If already logged in, user will be redirected to his/her account page;
	 * If not login yet, user will has three choices: register, login and directly view hotels info without logging on.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		prepareResponse("Home Page", response);

		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		if(checkSession(request)){
			redirect(response, "/user/account");
		}else{
			getBody(out); 
		}

		finishResponse(response);
	}


	
	
	
	/* Writes HTML form that shows two textfields and a button to the PrintWriter */
	private void getBody(PrintWriter out) {
		assert out != null;
		
		out.println("<p style=\"font-size: 25pt;\">");
		out.println("Welcome to Hotel Discover Channel!<br><br>");
		out.println("</p>");
		
		out.println("Here you can: ");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/register'}\">Register</button>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/login'}\">Login</button>");
		out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">View Hotels</button>");
		out.println("<p><br><br><br><br><br></p>");
	}
	
	
	
	
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}
	
	
	
	
	
}
