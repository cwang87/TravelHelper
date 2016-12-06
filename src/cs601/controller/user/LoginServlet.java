package cs601.controller.user;


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

/** A Login Servelet: handle request of logging account with input username and password */

@SuppressWarnings("serial")
public class LoginServlet extends BaseServlet {
	
	
	/**
	 * Process GET Request: 
	 * If user already logged in, user will be directed to account page without inputing username and pw again.
	 * If user didn't login before, user will be displayed a form to input username and password to be verified.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = null;
		
		String username = checkSession(request);
		
		if(checkRequestError(request)!=null){
			template = velocity.getTemplate("view/login.html");
			context.put("errorMessage", checkRequestError(request));
		}else if(username != null){
			template = velocity.getTemplate("view/account.html");
			context.put("username", username);
		}else{
			template = velocity.getTemplate("view/login.html");
		}
		
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		out.println(writer.toString());
		
	}

	
	
	
	
	/**
	 * Process POST Request:
	 * Parse the request and get username and password submitted by user.
	 * Compare username and hashed password with users table to verify consistance. 
	 * If not consistant, user will be required to input username and password again.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String user = request.getParameter("username");
		String userpw = request.getParameter("password");
		
		String dbuser = StringEscapeUtils.escapeHtml4(user);
		String dbuserpw = StringEscapeUtils.escapeHtml4(userpw);
		
		Status status = UsersHandler.getInstance().loginUser(dbuser, dbuserpw);
		
		if(status == Status.OK) {
			
			setSession(request, user);
			
			redirect(response, "/account");
		}
		else {
			redirect(response, "/login?error=" + status.name());
		}
	}

	
	
	
	
	
}