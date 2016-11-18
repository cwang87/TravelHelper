package cs601.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.controller.main.BaseServlet;


/**
 * A handler to guide user to manage account, including add review, display all
 * reviews the user has written and can also modify this written reviews.
 */

@SuppressWarnings("serial")
public class AccountServlet extends BaseServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		if(pass == null){
			session.setAttribute("pass", "no");
			pass = (String)session.getAttribute("pass");
		}
		
		prepareResponse("Account Management", response);
		PrintWriter out = response.getWriter();
		
		checkRequestError(request, out);
		
		if(pass.equals("ok")){
			String username = (String) session.getAttribute("username");
			getBody(out, username);
		}else{
			redirect(response, "/user/login");
		}
		
		finishResponse(response);
		
	}
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
		
		
		
		
	}

	
	
	private void getBody(PrintWriter out, String username){
		
		out.println("<p style=\"font-size: 18pt;\">");
		out.println("<h3>Hello, " + username  + "!<br></h3>");
		out.println("</p>");
		
		out.println("Safely logout<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/logout'}\">logout</button>");
		out.println("<p><br></p>");
		
		out.println("Search hotels, reviews and attractions here<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/hotels'}\">search</button>");
		out.println("<p><br></p>");
		
		out.println("I have a new review<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/add_review'}\">add review</button>");
		out.println("<p><br></p>");
		
		out.println("I want to modify my reviews<br>");
		out.println("<button type=\"button\" onclick=\"{location.href='/user/my_review?username=" + username +"'}\">my reviews</button>");
		out.println("<p><br></p>");
		
//		out.println("My reviews:");
		
		
		
		
		
	}
	
	
	
	
	
	
}
