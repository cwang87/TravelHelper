package cs601.controller.userSave;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.modelData.HotelSimple;
import cs601.tablesHandler.HotelsHandler;

@SuppressWarnings("serial")
public class ExpediaLinkServlet extends BaseServlet{
	
	/** Process GET Resquest:
	 * 	use cookies to save user's expedia history, user can view and clear their history
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		String hotelId = request.getParameter("hotelId");
		String username = checkSession(request);
		
		if(username == null){
			username = "user";
		}
		
		if(hotelId == null || hotelId.isEmpty()){
			hotelId = "";
		}
		
		if(hotelId.isEmpty()){
			displayHistory(request, out, username);
		}else{
			//receive request to save the expedia link in cookie when user click on it
			String cookieName = username + "_expediaHistory";
			String newCookie = buildCookie(hotelId, cookieName, request);
			Cookie cookie = new Cookie(cookieName, newCookie);
			cookie.setMaxAge(30 * 24 * 60 * 60);
			response.addCookie(cookie);
		}
	
	}
	
	
	
	/** show user's expedia history*/
	private void displayHistory(HttpServletRequest request, PrintWriter out, String username){
		
		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		
		Template header = null;
		Template template = velocity.getTemplate("view/expediaHistory.html");
		
		
		/* setup header */
		if(username.equals("user")){
			header = velocity.getTemplate("view/header_all.html");
		}else{
			header = velocity.getTemplate("view/header_user.html");
			context.put("username", username);
		}
		
		/* get history list */
		ArrayList<HotelSimple> visitList = new ArrayList<>();
		
		Map<String, String> cookies = getCookieMap(request);
		String expediaHistory = null;
		if(cookies != null){
			expediaHistory = cookies.get(username + "_expediaHistory");
		}
		
		if(expediaHistory == null || expediaHistory.isEmpty()){
			context.put("message", "You have no Expedia history!");
			context.put("displayTable", "none");
		}else {
			String[] hotelIdList = expediaHistory.split(",");
			for(String id : hotelIdList){
				HotelSimple hotel = new HotelSimple(id, HotelsHandler.getInstance().getHotelName(id));
				visitList.add(hotel);
			}
			context.put("visitList", visitList);
			context.put("message", "Here's your Expedia history:");
		}
		
		
		StringWriter writer = new StringWriter();
		
		header.merge(context, writer);
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}
	
	

	/** build new cookie that store expedia visit history */
	private String buildCookie(String hotelId, String cookieName, HttpServletRequest request){
		
		String expediaHistory = null;
		
		Map<String, String> cookies = getCookieMap(request);
		expediaHistory = cookies.get(cookieName);
		
		if(expediaHistory == null || expediaHistory.isEmpty()){
			return hotelId;
		}
		
		String[] srcArray = expediaHistory.split(",");
		List<String> srcList = Arrays.asList(srcArray);
		LinkedList<String> list = new LinkedList<String>(srcList);
		
		if (list.contains(hotelId)) {
			list.remove(hotelId);
			list.addFirst(hotelId);
		} else {
			list.addFirst(hotelId);
		}
		
		StringBuffer sb = new StringBuffer();
		
		for (String s : list) {
			sb.append(s).append(",");
		}
		//delete the last "," in the string
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/** process POST Request: clear user's Expeidia visit history; */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		

		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		
		Template header = null;
		Template template = velocity.getTemplate("view/expediaClear.html");
		
		
		//format username and cookieName
		String username = checkSession(request);
		if (username == null) {
			username = "user";
		}
		String cookieName = username + "_expediaHistory";
		
		
		// setup header 
		if(username.equals("user")){
			header = velocity.getTemplate("view/header_all.html");
		}else{
			header = velocity.getTemplate("view/header_user.html");
			context.put("username", username);
		}
		
		
		//check clear parameter
		String clearHistory = request.getParameter("clearHistory");
		
		if(clearHistory.equals("YES")){
			Map<String, String> cookies = getCookieMap(request);
			
			String expediaHistory = cookies.get(cookieName);
			if(expediaHistory == null || expediaHistory.isEmpty()){
				context.put("clearMessage", "You have no history to clear!");
			}else{
				clearCookie(cookieName, request, response);
				context.put("clearMessage", "You have cleared all Expeidia History!");
			}
			
		}else{
			redirect(response, "/expediaLink");
		}
		
		
		StringWriter writer = new StringWriter();
		
		header.merge(context, writer);
		template.merge(context, writer);
		out.println(writer.toString());
		
		
	}
	
	
	
	
	
	
	
	
}
