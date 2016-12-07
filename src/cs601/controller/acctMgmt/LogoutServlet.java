package cs601.controller.acctMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tablesHandler.UsersHandler;
import cs601.util.Status;
import cs601.util.Tools;


/**
 * Logout servlet: handle request from user to logout from account
 */

@SuppressWarnings("serial")
public class LogoutServlet extends BaseServlet {
	
	
	/**
	 * Process GET Request: invalid session and realize logout
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/logout.html");
		
		String username = checkSession(request);
		
		
		if(checkRequestError(request)!=null){
			context.put("errorMessage", checkRequestError(request));
		}else if (username != null && updateVisitDate(username)){
			request.getSession().invalidate();
			context.put("logoutMessage", "Sucessfully logged out!");
		}else{
			redirect(response, "/account");
		}
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		out.println(writer.toString());
	}

	

	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
	
	
	/**
	 * update users visit datetime in users table
	 * @param request
	 * @param response
	 */
	private Boolean updateVisitDate(String username){
		boolean update = false;
		Date visitDateTime = Tools.getDateTime();
		Status status = UsersHandler.getInstance().updateVisitDate(Tools.toTimestamp(visitDateTime), username);
		if(status == Status.OK){
			update = true;
		}
		
		return update;
	}
	
	
	
	
}
