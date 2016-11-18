package cs601.controller.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.util.Status;

/** A Servlet that provides base functionality to all servlets */

@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {
	
	
	
	/*-----------------------------------------------Headers----------------------------------------------------*/

	/** prepare a title-type response, not finish response yet */
	protected void prepareResponse(String title, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();

			writer.println("<!DOCTYPE html>");
			writer.println("<html>");
			writer.println("<head>");
			writer.println("\t<title>" + title + "</title>");
			writer.println("</head>");
			writer.println("<body>");
		} catch (IOException ex) {
			System.out.println("IOException while preparing the response: " + ex);
			return;
		}
	}
	
	
	/** prepare a header with title when we need to write script */
	protected void prepareRespTbl(String title, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();

			writer.println("<!DOCTYPE html>");
			writer.println("<html>");
			writer.println("<head>");
			writer.println("\t<title>" + title + "</title>");

		} catch (IOException ex) {
			System.out.println("IOException while preparing the response: " + ex);
			return;
		}
	}
	
	
	/*-----------------------------------------------Footers----------------------------------------------------*/

	/** finish response with date message displayed */
	protected void finishResponse(HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();

			writer.println();
			writer.println("<p style=\"font-size: 10pt; font-style: italic;\">");
			writer.println("Last updated at " + getDate());
			writer.println("</p>");

			writer.println("</body>");
			writer.println("</html>");

			writer.flush();

			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException ex) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	
	/** get current date */
	protected String getDate() {
		String format = "hh:mm a 'on' EEE, MMM dd, yyyy";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}
	
	/*-----------------------------------------------Hotel Table----------------------------------------------------*/
	
	protected String addHotelTbl1(String hotelId, String hotelName, 
			String hotelAddr, String aveRating){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + hotelId + "</td>");
		
		sb.append("<td>");
		sb.append("<a href=\"http://localhost:" + JettyWebServer.PORT2 + "/reviews?hotelId=" + hotelId + "\">");
		sb.append(hotelName + "</a></td>");
		
		sb.append("<td>" + hotelAddr + "</td>");
		sb.append("<td>" + aveRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	
	protected String addHotelTbl2(String hotelId, String hotelName, 
			String hotelAddr, String aveRating){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + hotelId + "</td>");
		sb.append("<td>" + hotelName + "</td>");
		sb.append("<td>" + hotelAddr + "</td>");
		sb.append("<td>" + aveRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	protected void addHotelTbl3(PrintWriter out, String hotelId, String hotelName, 
			String hotelAddr){
		
		out.println();
		
		out.println("<tr>");
		out.println("<td>" + hotelId + "</td>");
		
		out.println("<td>");
		out.println("<a href=\"http://localhost:" + JettyWebServer.PORT2 + "/user/add_review?hotelId=" + hotelId + "\">");
		out.println(hotelName + "</a></td>");
		
		out.println("<td>" + hotelAddr + "</td>");
		out.println("</tr>");
		
		
	}
	
	
	
	
	protected String addReviewTbl(String username, String reviewTitle, 
			String reviewText, String reviewDate, String isRecom, String overallRating){
		
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + username + "</td>");
		sb.append("<td>" + reviewTitle + "</td>");
		sb.append("<td width=\"45%\">" + reviewText + "</td>");
		sb.append("<td>" + reviewDate + "</td>");
		sb.append("<td>" + isRecom + "</td>");
		sb.append("<td>" + overallRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	protected String addReviewTbl2(String hotelName, String reviewTitle, 
			String reviewText, String reviewDate, String isRecom, String overallRating){
		
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<td>" + hotelName + "</td>");
		sb.append("<td>" + reviewTitle + "</td>");
		sb.append("<td width=\"45%\">" + reviewText + "</td>");
		sb.append("<td width=\"10%\">" + reviewDate + "</td>");
		sb.append("<td width=\"5%\">" + isRecom + "</td>");
		sb.append("<td width=\"5%\">" + overallRating + "</td>");
		sb.append("</tr>");
		
		return sb.toString();
		
	}
	
	

	
	
	
	
	/*-------------------------------------handle http request and response--------------------------------*/
	
	/** check if the request contains error, if there's an error, print it out */
	protected void checkRequestError(HttpServletRequest request, PrintWriter out) {
		String error = request.getParameter("error");
		if (error != null) {
			String errorMessage = getStatusMessage(error);
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}
	}
	
	
	/** redirect to a new url */
	protected void redirect(HttpServletResponse response, String url) throws IOException {
		String encodeURL = response.encodeRedirectURL(url);
		response.sendRedirect(encodeURL); 
	}
	
	
	/** set session for 1 day */
	protected void setSession(HttpServletRequest request, String username) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(24*60*60);
		session.setAttribute("username", username);
		session.setAttribute("pass", "ok");
	}
	

	
	
	/*---------------------------------------------Cookies--------------------------------------------------*/
	
	
	
	/** Return a cookie map from the cookies in the request */
	protected Map<String, String> getCookieMap(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();

		//getCookies() - Returns an array containing all of the Cookie objects the client sent with this request.
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}
		return map;
	}
	

	
	
	
	/** Clear cookies */
	protected void clearCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return;
		}

		for (Cookie cookie : cookies) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	
	
	
	
	/** Clear a particular cookie - */
	protected void clearCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	
	
	
	
	
	
	
	/** get status message with errorName */
	protected String getStatusMessage(String errorName) {
		Status status = null;

		try {
			status = Status.valueOf(errorName);
		} catch (Exception ex) {
			status = Status.ERROR;
		}
		return status.toString();
	}
	
	
	
	
	
	/** get status message with error code */
	protected String getStatusMessage(int code) {
		Status status = null;

		try {
			status = Status.values()[code];
		} catch (Exception ex) {
			status = Status.ERROR;
		}

		return status.toString();
	}
	
	
	
	
}