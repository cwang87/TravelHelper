package cs601.controller.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.modelData.HotelAveRate;
import cs601.tablesHandler.HotelsHandler;

/**
 * Hotels Servlet: handle requests of viewing full hotel list, and hotel info,
 * including hotelId, hotelName, hotel address and average rating of the hotel based on all reviews received so far
 */

@SuppressWarnings("serial")
public class HotelsServlet extends BaseServlet {

	/**
	 * Process GET request: display a full list of hotels with information including:
	 * hotelId, hotelName, hotel address and average rating of the hotel based on all reviews received so far.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template header = null;
		Template body = velocity.getTemplate("view/hotelList.html");
		StringWriter writer = new StringWriter();
		
		
		/* setup header */
		if(checkSession(request)!= null){
			header = velocity.getTemplate("view/header_user.html");
			context.put("username", checkSession(request));
		}else{
			header = velocity.getTemplate("view/header_all.html");
		}
		
		/* setup body */
		String hotelName = request.getParameter("hotelName");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		
		if(hotelName.isEmpty() && city.isEmpty() && state.isEmpty()){
			//Full hotelList
			List<HotelAveRate> hotelList = HotelsHandler.getInstance().getHotelsFull();
			String geoLocations = getGeoLocations(hotelList);
			context.put("hotelList", hotelList);
			context.put("geoLocations", geoLocations);
		}else if(hotelName.isEmpty() && !city.isEmpty() && !state.isEmpty()){
			//hotelList given a specific city/state
			List<HotelAveRate> hotelList = HotelsHandler.getInstance().getHotelsByCity(city, state);
			String geoLocations = getGeoLocations(hotelList);
			context.put("hotelList", hotelList);	
			context.put("geoLocations", geoLocations);
		}else if(!hotelName.isEmpty() && city.isEmpty() && state.isEmpty()){
			//display hotels whose name contains given string
			List<HotelAveRate> hotelList = HotelsHandler.getInstance().getHotelsByPartialName(hotelName);
			String geoLocations = getGeoLocations(hotelList);
			if(hotelList.size() == 1){
				redirect(response, "/hotelWiki?hotelId="+hotelList.get(0).getHotelId());
			}else{
				context.put("hotelList", hotelList);
				context.put("geoLocations", geoLocations);
			}	
		}else{
			redirect(response, "/home?error=INVALID_SEARCH");
		}
		
		header.merge(context, writer);
		body.merge(context, writer);
		out.println(writer.toString());
		
	}	
	
	
	/**
	 * A method to generate geolocation part of the request url to display multiple markers in map.
	 * The format is required by google map API.
	 * @param hotelList
	 * @return
	 */
	private String getGeoLocations(List<HotelAveRate> hotelList){
		StringBuilder sb = new StringBuilder();
		
		sb.append("[ ");
		
		for(HotelAveRate hotel : hotelList){
			sb.append("{lat: ").append(hotel.getLat()).append(", lng: ").append(hotel.getLon()).append("},");
		}
		
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(" ]");
		
		return sb.toString();
	}
	
	
	
	
	/** process POST Request: request will be resent to doGet(); */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doGet(request, response);
	}

	
	
}
