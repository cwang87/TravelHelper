package cs601.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


@SuppressWarnings("serial")
public class HotelInfoServlet extends HttpServlet {

//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
//		// invalid string
//		Boolean fail = false;
//		JSONObject obj = new JSONObject();
//		obj.put("success", fail);
//		obj.put("hotelId", "invalid");
//		String invalid = obj.toJSONString();
//		
//		ThreadSafeHotelData hotelData = (ThreadSafeHotelData)getServletContext().getAttribute("hotelData");
//		PrintWriter writer = null;
//		try {
//			writer = response.getWriter();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		String hotelId = request.getParameter("hotelId");
//		if (hotelId == null) {
//			response.setContentType("application/json");
//			response.setStatus(HttpServletResponse.SC_OK);
//			writer.println(invalid);
//			writer.flush();
//			writer.close();
//		}else if(hotelData.getHotels().contains(hotelId)){
//			response.setContentType("application/json");
//			response.setStatus(HttpServletResponse.SC_OK);
//			writer.println(hotelData.getOneHotelJSON(hotelId));
//			writer.flush();
//			writer.close();
//		}else{
//			response.setContentType("application/json");
//			response.setStatus(HttpServletResponse.SC_OK);
//			writer.println(invalid);
//			writer.flush();
//			writer.close();
//		}
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//			PrintWriter writer = response.getWriter();
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
}
