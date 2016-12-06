package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tableData.HotelAveRate;
import cs601.tablesHandler.HotelsHandler;

@SuppressWarnings("serial")
public class HotelWikiServlet extends BaseServlet {
	
	private static final HotelsHandler hotelsHandler = HotelsHandler.getInstance();
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template header = null;
		
		//setup header navbar
		if(checkSession(request)!= null){
			header = velocity.getTemplate("view/header_user.html");
			context.put("username", checkSession(request));
		}else{
			header = velocity.getTemplate("view/header_all.html");
		}
		
		
		//get hotelInfo
		String hotelId = request.getParameter("hotelId");
		HotelAveRate hotel = hotelsHandler.getHotelAveRate(hotelId);
		Template body = velocity.getTemplate("view/hotelWiki.html");
		context.put("hotel", hotel);
		context.put("hotelId", hotelId);
		
		
		StringWriter writer = new StringWriter();
		header.merge(context, writer);
		body.merge(context, writer);

		out.println(writer.toString());
		
		
	}

	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}
	
	
	
	

}
