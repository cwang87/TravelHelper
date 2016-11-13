package cs601.controller;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;

import cs601.service.UserService;
import cs601.util.Status;

/** A servlet that handles user login. */

@SuppressWarnings("serial")
public class LoginServlet extends BaseServlet {
	
	private static final UserService userService = UserService.getInstance();
	
	
	

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		prepareResponse("Please Login", response);
		PrintWriter out = response.getWriter();
		
		String error = request.getParameter("error");
		if(error != null) {
			String errorMessage = getStatusMessage(error);
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}

		displayForm(out); 
		finishResponse(response);
	}

	
	
	
	
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		prepareResponse("Please Login", response);

		String existUser = request.getParameter("user");
		String userpw = request.getParameter("pw");
		
		existUser = StringEscapeUtils.escapeHtml4(existUser);
		userpw = StringEscapeUtils.escapeHtml4(userpw);
		
		Status status = userService.authLogin(existUser, userpw);
		
		if(status == Status.OK) {
			response.getWriter().println("Login Successfully!.");
			
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(24*60*60);
			session.setAttribute("username", existUser);
			session.setAttribute("pass", "ok");
			
			String url = "/account" + status.name();
			url = response.encodeRedirectURL(url);
			response.sendRedirect("url");
		}
		else {
			String url = "/login?error=" + status.name();
			url = response.encodeRedirectURL(url);
			response.sendRedirect(url); 
		}
	}

	
	
	
	
	
	
	
	
	
	/** Writes and HTML form that shows two textfields and a button to the PrintWriter */
	private void displayForm(PrintWriter out) {
		assert out != null;

		out.println("<form action=\"/login\" method=\"post\">"); 
		out.println("<table border=\"0\">");
		out.println("\t<tr>");
		out.println("\t\t<td>Usename:</td>");
		out.println("\t\t<td><input type=\"text\" name=\"user\" size=\"30\"></td>");
		out.println("\t</tr>");
		out.println("\t<tr>");
		out.println("\t\t<td>Password:</td>");
		out.println("\t\t<td><input type=\"password\" name=\"pw\" size=\"30\"></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p><input type=\"submit\" value=\"Login\"></p>");
		out.println("</form>");
	}
}