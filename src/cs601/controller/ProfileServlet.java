package cs601.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ProfileServlet extends BaseServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		HttpSession session = request.getSession(false);
		if(session.getAttribute("pass") == "ok"){
			String name = (String) session.getAttribute("username");
			prepareResponse("Hello, " + name  + "Welcome to profile", response);
		}else{
			prepareResponse("Please login first", response);
			
			String url = "/login";
			url = response.encodeRedirectURL(url);
			response.sendRedirect(url); 
			
		}
		
	}
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}

}
