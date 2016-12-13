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
import cs601.modelData.HotelAveRate;
import cs601.tablesHandler.HotelsHandler;


/** A class - given a hotelId (which is selected by user), 
 * display hotel info of this hotel, including:
 * hotelname, address, average rating, link to expedia, reviews tab, map tab and attraction search tab
 */

@SuppressWarnings("serial")
public class HotelWikiServlet extends BaseServlet {
	
	/**
	 * Process Get request: given a hotelId, display hotel info of this hotel, including:
	 * hotelname, address, average rating, link to expedia, reviews tab, map tab and attraction search tab
	 * @param request
	 * @param response
	 * @throws IOException
	 */
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
		HotelAveRate hotel = HotelsHandler.getInstance().getHotelAveRate(hotelId);
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
