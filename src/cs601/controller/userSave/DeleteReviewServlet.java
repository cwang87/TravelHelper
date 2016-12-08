package cs601.controller.userSave;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tablesHandler.ReviewsHandler;
import cs601.util.Status;

@SuppressWarnings("serial")
public class DeleteReviewServlet extends BaseServlet {
	
	/**
	 * Process GET request: 
	 * If user didn't login but still use the url to access account, user will be redirected to login page.
	 * If already logged in, user will delete the written review.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
				
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/deleteReview.html");
		
		StringWriter writer = new StringWriter();
		if(checkRequestError(request)!=null){
			context.put("errorMessage", checkRequestError(request));
		}else if(checkSession(request)!= null){
		    String reviewId = request.getParameter("reviewId");
		    Status status = ReviewsHandler.getInstance().deleteReview(reviewId);
		    if(status == Status.OK){
		    	context.put("deleteMessage", "Your review has been deleted successfully!");
		    }else{
		    	redirect(response, "/delete_message?error=invalid deletion");
		    }
		}else{
			context.put("reloadParent", "parent.location.reload();");
		}
		
		template.merge(context, writer);
		out.println(writer.toString());
	}

	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
	
	
}
