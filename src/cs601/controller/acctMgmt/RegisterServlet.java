package cs601.controller.acctMgmt;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tablesHandler.UsersHandler;
import cs601.util.Status;


/** Register servlet: handles user registration request. */

@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {
	
	private static final UsersHandler userService = UsersHandler.getInstance();
	

	
	/**
	 * Process GET Request: Display form for users to input username and password to create account
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
				
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/register.html");
		
		
		if(checkRequestError(request)!=null){
			context.put("errorMessage", checkRequestError(request));
		}
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		out.println(writer.toString());
		
	}


	
	
	
	
	/** Get register info from the form submitted by user.
	 * If username or password is invalid, user will be directed back to register page and informed error status.
	 * If both usernam and password are valid, user will register successfully and be directed to account page.
	 * Meanwhile, database will be updated.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String newuser = request.getParameter("username");
		String newpass = request.getParameter("password");
		
		// sanitize user input to avoid XSS attacks:
		String dbuser = StringEscapeUtils.escapeHtml4(newuser).toLowerCase();
		String dbpass = StringEscapeUtils.escapeHtml4(newpass).toLowerCase();
		
		Status status = userService.registerUser(dbuser, dbpass);
		
		if(status == Status.OK) {
			setSession(request, newuser);
			redirect(response, "/account");
		}else {
			redirect(response, "/register?error=" + status.name());
		}
	}

	
	
}