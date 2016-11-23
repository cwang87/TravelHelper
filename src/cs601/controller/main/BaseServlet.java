package cs601.controller.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs601.util.Status;
import cs601.util.Tools;

/** A Servlet that contains methods frequently used in servlets */

@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {
	
	
	
	/*------------------------------------------Prepare Response <body></body>---------------------------------------*/

	/** prepare a title-type response, not finish response yet 
	 * 
	 * @param title of the html
	 * @param response
	 */
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
	
	
	
	
	
	/** finish response with date message displayed */
	protected void finishResponse(HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();

			writer.println();
			writer.println("<p style=\"font-size: 10pt; font-style: italic;\">");
			writer.println("Last updated at " + Tools.getDate());
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
	
	

	
	/*---------------------------------------actions during request, response----------------------------------------*/
	
	/** check if the request contains error, if there's an error, write it to response */
	protected void checkRequestError(HttpServletRequest request, PrintWriter out) {
		String error = request.getParameter("error");
		if (error != null) {
			String errorMessage = getStatusMessage(error);
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}
	}
	

	
	/** redirect user to the given url
	 * 
	 * @param response
	 * @param url user will be directed to
	 * @throws IOException
	 */
	protected void redirect(HttpServletResponse response, String url) throws IOException {
		String encodeURL = response.encodeRedirectURL(url);
		response.sendRedirect(encodeURL); 
	}
	
	
	/*---------------------------------------------session--------------------------------------------------------*/
	
	/** check the value of attribute named "pass" in session
	 * 
	 * @return true if the value is "ok", else return false.
	 */
	protected boolean checkSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		if(pass != null && pass.equals("ok")){
			return true;
		}
		return false;
	}
	
	
	/** get the value of attribute named "username" in session
	 * 
	 * @return value of "username" in session
	 */
	protected String getSessionUsername(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		return username;
	}
	
	
	
	/** set session attributes:
	 * 	username - the given username
	 * 	pass - "ok", to record the user already login
	 * 	set session idle iterval - 1 hour
	 * 
	 * @param request
	 * @param the username of user logged in
	 */
	protected void setSession(HttpServletRequest request, String username) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60*60);
		session.setAttribute("username", username);
		session.setAttribute("pass", "ok");
	}
	

	
	
	/*------------------------------------------------Cookies--------------------------------------------------*/
	
	
	
	/** Return a cookie map from the cookies in the request
	 * 
	 * @return a cookie map
	 */
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

	
	
	
	
	/** Clear a particular cookie */
	protected void clearCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	
	
	
	
	
	/*---------------------------------------------get status message-----------------------------------------------*/
	
	/** get status message with errorName
	 * 
	 * @param errorName
	 * @return error message
	 */
	protected String getStatusMessage(String errorName) {
		Status status = null;

		try {
			status = Status.valueOf(errorName);
		} catch (Exception ex) {
			status = Status.ERROR;
		}
		return status.toString();
	}
	
	
	
	
	
	/** get status message with error code
	 * 
	 * @param error code
	 * @return error message
	 */
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