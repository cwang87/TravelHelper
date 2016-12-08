package cs601.controller.userSave;

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
import cs601.tableData.HotelSimple;
import cs601.tablesHandler.SavedHotelsHandler;

@SuppressWarnings("serial")
public class SaveHotelsServlet extends BaseServlet{
	
	/** for login users, display a list of hotels that they already saved! */
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		
		Template template = velocity.getTemplate("view/savedHotels.html");
		
		String username = checkSession(request);
		
		if(username == null){
			redirect(response, "login");
		}else{
			List<HotelSimple> hotelList = SavedHotelsHandler.getInstance().getSavedHotels(username);
			if(hotelList == null || hotelList.isEmpty()){
				context.put("message", "You haven't save any hotels yet!");
				context.put("displayTable", "none");
			}else{
				context.put("message", "Here's your hotel list:");
				context.put("hotelList", hotelList);
			}
			
		}
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}
	
	
	
	
	
	

	/** process POST Request: add user saved hotel info into database */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		String hotelId = request.getParameter("hotelId");
		String username = checkSession(request);
		
		String clearList = request.getParameter("clearList");
		
		if(hotelId != null && clearList == null){
			if(username == null){
				out.println("To save this hotel, please login first!");
			}else if (SavedHotelsHandler.getInstance().checkSaveExisting(username, hotelId)){
				out.println("This hotel was saved before!");			
			}else{
				SavedHotelsHandler.getInstance().saveHotel(username, hotelId);
				out.println("Now, this hotel is in your list!");
			}
		}else if(clearList.equals("YES") && username != null){
			SavedHotelsHandler.getInstance().deleteSavedList(username);
			out.println("<p style=\"font-size:150%; font-family:fantasy; color:green;\">");
			out.println("Hotel list has been cleared successfully!");
			out.println("</p>");
		}else{
			redirect(response, "/login");
		}
	}
	

}
