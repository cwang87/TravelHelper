package cs601.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.service.UserService;

@SuppressWarnings("serial")
public class LogoutServlet extends BaseServlet {
	
	private static final UserService userService = UserService.getInstance();
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out = response.getWriter();
		
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		prepareResponse("You are successfully logged out", response);
		finishResponse(response);
		
	}

	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		this.doGet(request, response);
	}
}
