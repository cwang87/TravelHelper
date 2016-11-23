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
	
	/** main method to start the server*/
	public static void main(String[] args) {
		
		Server server = new Server(PORT2);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		
		
		
		context.addServlet(HomeServlet.class, "/home");
		context.addServlet(HotelsServlet.class, "/hotels");
		context.addServlet(ReviewsServlet.class, "/reviews");
//		context.addServlet(AttractionsServlet.class, "/attractions");
		
		
		context.addServlet(RegisterServlet.class, "/user/register");
		context.addServlet(LoginServlet.class, "/user/login");
		context.addServlet(LogoutServlet.class, "/user/logout");
		context.addServlet(AccountServlet.class, "/user/account");
		context.addServlet(AddReviewServlet.class, "/user/add_review");
		context.addServlet(MyReview.class, "/user/my_review");
//		context.addServlet(AccountServlet.class, "user/modify_review");

		
		
		
		
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {context});
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
