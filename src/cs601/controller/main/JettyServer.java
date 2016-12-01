package cs601.controller.main;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs601.controller.search.*;
import cs601.controller.user.*;


/** A class use Jetty and Servlets to handle the HTTP GET request from client */
public class JettyServer {

	public static final int PORT = 2050;
	
	/** main method to start the server*/
	public static void main(String[] args) {
		
		Server server = new Server(PORT);

		
		/* servlet handlers */
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		VelocityEngine velocity = new VelocityEngine();
		velocity.init();
		context.setAttribute("templateEngine", velocity);
		
		context.addServlet(HomeServlet.class, "/home");
		
		context.addServlet(HotelsServlet.class, "/hotels");
		context.addServlet(HotelWiki.class, "/hotelWiki");
		
		
		context.addServlet(ReviewsServlet.class, "/reviews");
//		context.addServlet(AttractionsServlet.class, "/attractions");
		
		
		context.addServlet(RegisterServlet.class, "/register");
		context.addServlet(LoginServlet.class, "/login");
		context.addServlet(LogoutServlet.class, "/logout");
		
		context.addServlet(AccountServlet.class, "/account");
		context.addServlet(AddReviewServlet.class, "/add_review");
		context.addServlet(MyReview.class, "/my_review");
//		context.addServlet(AccountServlet.class, "user/modify_review");

		
//		context.setResourceBase("view");
		
		/* resource handlers */
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setResourceBase("view");
		
		
		
		
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {resource_handler, context});
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
