package cs601.controller.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs601.controller.search.*;
import cs601.controller.user.*;


/** A class use Jetty and Servlets to handle the HTTP GET request from client */
public class JettyWebServer {

	public static final int PORT2 = 2050;
	
	/** start the server*/
	public static void main(String[] args) {
		
		Server server = new Server(PORT2);
		
		// search related
		ServletContextHandler contextSearch = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextSearch.setContextPath("/");

		contextSearch.addServlet(HomeServlet.class, "/home");
		contextSearch.addServlet(HotelsServlet.class, "/hotels");
		contextSearch.addServlet(ReviewsServlet.class, "/reviews");
//		contextSearch.addServlet(AttractionsServlet.class, "/attractions");
		
		
		contextSearch.addServlet(RegisterServlet.class, "/user/register");
		contextSearch.addServlet(LoginServlet.class, "/user/login");
		contextSearch.addServlet(LogoutServlet.class, "/user/logout");
		contextSearch.addServlet(AccountServlet.class, "/user/account");
		contextSearch.addServlet(AddReviewServlet.class, "/user/add_review");
		contextSearch.addServlet(MyReview.class, "/user/my_review");
//		contextSearch.addServlet(AccountServlet.class, "user/modify_review");

		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {contextSearch});
		server.setHandler(handlers);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An exception occurred while running the server: " + e.getMessage());
			System.exit(-1);
		}
	}

	
}
