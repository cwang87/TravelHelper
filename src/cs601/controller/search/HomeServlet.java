package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;


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

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template header = null;
		Template body = velocity.getTemplate("view/home.html");;
		
		if(checkSession(request)!= null && checkRequestError(request) == null){
			header = velocity.getTemplate("view/header_user.html");
			context.put("username", checkSession(request));
		}else if(checkSession(request) == null && checkRequestError(request) == null){
			header = velocity.getTemplate("view/header_all.html");
		}else if(checkSession(request)!= null && checkRequestError(request) != null){
			header = velocity.getTemplate("view/header_user.html");
			context.put("errorMessage", checkRequestError(request));
		}else{
			header = velocity.getTemplate("view/header_all.html");
			context.put("errorMessage", checkRequestError(request));
		}
		
		StringWriter writer = new StringWriter();
		header.merge(context, writer);
		body.merge(context, writer);

		out.println(writer.toString());
		
		
	}

		
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}
	
	
	
	
	
}
