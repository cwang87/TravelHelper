package cs601.controller.user;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import cs601.controller.main.BaseServlet;
import cs601.tablesHandler.UsersHandler;
import cs601.util.Status;


/** A servlet that handles user registration. */

@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {
	
	private static final UsersHandler userService = UsersHandler.getInstance();
	

	
	/**
	 * display form for users to register
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		prepareResponse("Register New User", response);
		
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		displayForm(out); 
		
		finishResponse(response);
	}


	
	
	
	
	/** Get register info from the form submitted by user.
	 * If username or password is invalid, user will be directed back to register page and informed error status.
	 * If both usernam and password are valid, user will register successfully and be directed to account page.
	 * Meanwhile, database will be updated.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		prepareResponse("Register New User", response);

		String newuser = request.getParameter("user");
		String newpass = request.getParameter("pass");
		
		// sanitize user input to avoid XSS attacks:
		String dbuser = StringEscapeUtils.escapeHtml4(newuser).toLowerCase();
		String dbpass = StringEscapeUtils.escapeHtml4(newpass).toLowerCase();
		
		Status status = userService.registerUser(dbuser, dbpass);
		
		if(status == Status.OK) {
			setSession(request, newuser);
			redirect(response, "/user/account");
		}else {
			redirect(response, "/user/register?error=" + status.name());
		}
	}

	
	
	
	
	
	
	
	
	
	/** Writes and HTML form that shows two textfields and a button to the PrintWriter */
	private void displayForm(PrintWriter out) {
		assert out != null;

		out.println("<p style=\"font-size: 18pt;\">");
		out.println("Please register here<br><br>");
		out.println("</p>");
		
		out.println("<form action=\"/user/register\" method=\"post\">"); 
		out.println("<table border=\"0\">");
		out.println("\t<tr>");
		out.println("\t\t<td>Username:</td>");
		out.println("\t\t<td><input type=\"text\" name=\"user\" size=\"30\"></td>");
		out.println("\t</tr>");
		out.println("\t<tr>");
		out.println("\t\t<td>Password:</td>");
		out.println("\t\t<td><input type=\"password\" name=\"pass\" size=\"30\"></td>");
		out.println("</tr>");
		
		out.println("\t<tr>");
		out.println("\t\t<td><input type=\"submit\" value=\"Register\"></td>");
		out.println("\t\t<td><button type=\"button\" onclick=\"{location.href='/home'}\">Back to Home Page</button></td>");
		out.println("</table>");
		out.println("</form>");
	}
}