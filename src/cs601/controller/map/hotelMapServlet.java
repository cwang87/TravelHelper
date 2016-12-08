package cs601.controller.map;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;

/** A class to handle request of showing google map for this hotel */

@SuppressWarnings("serial")
public class hotelMapServlet extends BaseServlet{
	
	/** Process Get request: display embed google map for this hotel */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/hotelMap.html");
		
		String hotelId = request.getParameter("hotelId");
		
		if(hotelId == null || hotelId.isEmpty()){
			context.put("message", "Please select a hotel to display!");
		}else{
			String url = MapAPIHelper.getInstance().getHotelMapRequest(hotelId);
			context.put("url", url);
		}
		
		
		
		
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
	}
	

}
