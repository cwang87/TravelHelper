package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;


/**
 * Account servlet: handle request of accessing user's account.
 * In the account page, users can logout, view hotels, add review, view and modify reviews written by user.
 */

@SuppressWarnings("serial")
public class AccountServlet extends BaseServlet {
	
	
	/**
	 * Process GET request: 
	 * If user didn't login but still use the url to access account, user will be redirected to login page.
	 * If already logged in, user will access this account page.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
				
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		
		StringWriter writer = new StringWriter();
		
		
		if(checkRequestError(request)!=null){
		    Template template = velocity.getTemplate("view/error.html");
			context.put("errorMessage", checkRequestError(request));
			template.merge(context, writer);
		}else if(checkSession(request)!= null){
		    Template template = velocity.getTemplate("view/account.html");
		    String username = checkSession(request);
		    context.put("username", username);
		    context.put("lastVisitMessage", getLastVisitMessage(request, username.toLowerCase()));
		    template.merge(context, writer);
		}else{
			redirect(response, "/login");
		}
		out.println(writer.toString());
	}
	
	
	/** get last visit message to be displayed to users */
	private String getLastVisitMessage(HttpServletRequest request, String username) {
		String lastVisitMessage = "";
		Map<String, String> cookies = getCookieMap(request);
		String visitDateLast = cookies.get(username + "_visitDateLast");
		if(visitDateLast == null){
			lastVisitMessage = "You have never been to this webpage before!\n" + "Thank you for visiting.";
		}else{
			lastVisitMessage = "Your last visit was on " + visitDateLast;
		}
		return lastVisitMessage;
	}
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
}
