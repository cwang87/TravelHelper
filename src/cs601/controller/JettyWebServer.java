package cs601.controller;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;


/** A class use Jetty and Servlets to handle the HTTP GET request from client */
public class JettyWebServer {

	public static final int PORT2 = 2050;
	
	/** start the server*/
	public static void main(String[] args) {
		
		Server server = new Server(PORT2);
		
		//user related
		ServletContextHandler contextUser = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextUser.setContextPath("/user");
		
		contextUser.addServlet(RegistrationServlet.class, "/register");
		contextUser.addServlet(LoginServlet.class, "/login");
		contextUser.addServlet(LogoutServlet.class, "/logout");
//		contextUser.addServlet(ReviewServlet.class, "/account");
		contextUser.addServlet(ProfileServlet.class, "/profile");
		contextUser.addServlet(AddReviewServlet.class, "/review-mgmt/addReview");
		contextUser.addServlet(ModifyReviewServlet.class, "/review-mgmt/modifyReview");
		
		//search related
		ServletContextHandler contextSearch = new ServletContextHandler();
		contextUser.setContextPath("/");
		
		contextSearch.addServlet(HotelInfoServlet.class, "/");
		contextSearch.addServlet(ReviewServlet.class, "/Hotels");
		contextSearch.addServlet(ReviewServlet.class, "/Reviews");
		contextSearch.addServlet(ReviewServlet.class, "/Attractions");

		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextUser, contextSearch });
		server.setHandler(handlers);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An exception occurred while running the server. " + e.getMessage());
			System.exit(-1);
		}
	}

	
}
