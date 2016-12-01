package cs601.controller.main;

import java.io.IOException;
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

import org.apache.velocity.app.VelocityEngine;

import cs601.util.Status;

/** A Servlet that contains methods frequently used in servlets */

@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {
	


	
	
	/** check if the request contains error, if there's an error, return the error message */
	protected String checkRequestError(HttpServletRequest request) {
		String error = request.getParameter("error");
		if (error != null) {
			String errorMessage = getStatusMessage(error);
			return errorMessage;
		}else{
			return null;
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
	
	
	
	
	
	
	
	
	/** get VelocityEngine from servlet */
	protected VelocityEngine getVelocityEngine(HttpServletRequest request){
		return (VelocityEngine)request.getServletContext().getAttribute("templateEngine");
	}
	
	
	
	/*------------------------------------------------Session-----------------------------------------------------*/
	
	
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
	
	
	
	
	
	
	/** check the value of attribute named "pass" in session
	 * 
	 * @return username if the value is "ok", else return null.
	 */
	protected String checkSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		if(pass != null && pass.equals("ok")){
			return (String)session.getAttribute("username");
		}
		return null;
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
	
	
	
	
	
	
	/*---------------------------------------------Cookies for last visit-----------------------------------------------*/
	

	/**
	 * Returns the current date and time in a short format.
	 *
	 * @return current date and time
	 */
	public static String getDate() {
		String format = "yyyy-MM-dd hh:mm a";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(Calendar.getInstance().getTime());
	}
	
	
	
	
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

	
	
	
	
}