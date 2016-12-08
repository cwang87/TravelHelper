package cs601.controller.main;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs601.controller.map.AttractionsServlet;
import cs601.controller.map.hotelMapServlet;
import cs601.controller.search.*;
import cs601.controller.user.*;
import cs601.controller.userSave.AddReviewServlet;
import cs601.controller.userSave.DeleteReviewServlet;
import cs601.controller.userSave.ExpediaLinkServlet;
import cs601.controller.userSave.ModifyReviewServlet;
import cs601.controller.userSave.MyReviewServlet;
import cs601.controller.userSave.SaveHotelsServlet;


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
		
		
		/* search servlets - account page access related */
		context.addServlet(HomeServlet.class, "/home");
		context.addServlet(HotelsServlet.class, "/hotels");
		context.addServlet(HotelWikiServlet.class, "/hotelWiki");
		context.addServlet(ReviewsServlet.class, "/reviews");
		
		/* map servlets - get info from google map API */
		context.addServlet(hotelMapServlet.class, "/hotelMap");
		context.addServlet(AttractionsServlet.class, "/attractions");
		
		/* user servlets - account page access related */
		context.addServlet(RegisterServlet.class, "/register");
		context.addServlet(LoginServlet.class, "/login");
		context.addServlet(LogoutServlet.class, "/logout");
		context.addServlet(AccountServlet.class, "/account");
		
		/* userSave servlets - manage info saved by user */
		context.addServlet(MyReviewServlet.class, "/my_review");
		context.addServlet(DeleteReviewServlet.class, "/delete_review");
		context.addServlet(ModifyReviewServlet.class, "/modify_review");
		context.addServlet(AddReviewServlet.class, "/add_review");
		context.addServlet(ExpediaLinkServlet.class, "/expediaLink");
		context.addServlet(SaveHotelsServlet.class, "/save_hotels");

		
		
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
