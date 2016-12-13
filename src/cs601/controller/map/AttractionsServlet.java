package cs601.controller.map;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.modelData.TouristAttraction;


/** A class to do request from users to search attractions near a particular hotel within given radius */
@SuppressWarnings("serial")
public class AttractionsServlet extends BaseServlet{
	
	
	/** Process Get request: display the search view for users to search attractions near the given hotel */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/attractions.html");
		
		String hotelId = request.getParameter("hotelId");
		
		if(hotelId == null || hotelId.isEmpty()){
			context.put("message", "Please select a hotel first before search nearby tourist attractions.");
			context.put("displaySearchBar", "none");
			context.put("displayTable", "none");
		}else{
			context.put("message", "Please input a radius within which you would like to search tourist attractions.");
			context.put("displayTable", "none");
		}
		
		
		Cookie cookie = new Cookie("attractionSearch_hotelId", hotelId);
		response.addCookie(cookie);
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}
	
	
	
	
	/** Process POST request: display nearby attractions within the radius submit by user */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/attractions.html");
		
		String radiusInMiles = request.getParameter("radius");
		Map<String, String> cookies = getCookieMap(request);
		String hotelId = cookies.get("attractionSearch_hotelId");
		
		if(hotelId == null || radiusInMiles.isEmpty()){
			context.put("message", "Please input a radius for searching!");
			context.put("displayTable", "none");
		}else{
			List<TouristAttraction> attractionList = MapAPIHelper.getInstance().getAttractions(hotelId, radiusInMiles);
			context.put("attractionList", attractionList);
		}
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}

}
