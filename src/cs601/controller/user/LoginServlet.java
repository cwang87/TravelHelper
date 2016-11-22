package cs601.controller.user;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import cs601.controller.main.BaseServlet;
import cs601.tablesHandler.UsersHandler;
import cs601.util.Status;

/** A servlet that handles user login. */

@SuppressWarnings("serial")
public class LoginServlet extends BaseServlet {
	
	private static final UsersHandler userService = UsersHandler.getInstance();
	
	
	/**
	 * check session to keep users in login status if he/she already login before.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		prepareResponse("Login", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);

		//check session
		if(checkSession(request)){
			redirect(response, "/user/account");
		}else{
			displayForm(out); 
		}
		
		finishResponse(response);
	}

	
	
	
	
	/**
	 * get login info from the form submitted by user, and check whether username and password are the
	 * same with our record in database.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		prepareResponse("Please Login", response);

		String user = request.getParameter("user");
		String userpw = request.getParameter("pw");
		
		String dbuser = StringEscapeUtils.escapeHtml4(user);
		String dbuserpw = StringEscapeUtils.escapeHtml4(userpw);
		
		Status status = userService.loginUser(dbuser, dbuserpw);
		
		if(status == Status.OK) {
			
			setSession(request, user);
			
			redirect(response, "/user/account");
		}
		else {
			redirect(response, "/user/login?error=" + status.name());
		}
	}

	
	
	
	
	
	
	
	
	
	/** Writes and HTML form that shows two textfields and a button to the PrintWriter */
	private void displayForm(PrintWriter out) {
		assert out != null;
		
		out.println("<p style=\"font-size: 18pt;\">");
		out.println("Please use your username and password to login<br><br>");
		out.println("</p>");

		out.println("<form action=\"/user/login\" method=\"post\">"); 
		out.println("<table border=\"0\">");
		out.println("\t<tr>");
		out.println("\t\t<td>Usename:</td>");
		out.println("\t\t<td><input type=\"text\" name=\"user\" size=\"30\"></td>");
		out.println("\t</tr>");
		out.println("\t<tr>");
		out.println("\t\t<td>Password:</td>");
		out.println("\t\t<td><input type=\"password\" name=\"pw\" size=\"30\"></td>");
		out.println("</tr>");
		
		out.println("\t<tr>");
		out.println("\t\t<td><input type=\"submit\" value=\"Login\"></td>");
		out.println("\t\t<td><button type=\"button\" onclick=\"{location.href='/home'}\">Back to Home Page</button></td>");
		out.println("</table>");
		out.println("</form>");
	
	}
}