package cs601.controller.userSave;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cs601.controller.main.BaseServlet;
import cs601.tableData.ReviewDB;
import cs601.tablesHandler.ReviewsHandler;
import cs601.util.Status;
import cs601.util.Tools;

@SuppressWarnings("serial")
public class ModifyReviewServlet extends BaseServlet {

	/**
	 * Process GET request: If user didn't login but still use the url to access
	 * account, user will be redirected to login page. If already logged in,
	 * user will be able to modify a written review.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();

		VelocityEngine velocity = getVelocityEngine(request);
		VelocityContext context = new VelocityContext();
		Template template = velocity.getTemplate("view/modifyReview.html");

		StringWriter writer = new StringWriter();
		if (checkRequestError(request) != null) {
			context.put("errorMessage", checkRequestError(request));
		} else if (checkSession(request) == null) {
			redirect(response, "/login");
		} else {
			String reviewId = request.getParameter("reviewId");
			String hotelName = request.getParameter("hotelName");
			if(reviewId != null && !reviewId.isEmpty() && hotelName != null && !hotelName.isEmpty()){
				ReviewDB review = ReviewsHandler.getInstance().getReviewByReviewId(reviewId);
				String isRecom = review.getIsRecom();
				String overallRating = review.getOverallRating();
				context.put("checked" + isRecom, "checked");
				context.put("checked" + overallRating, "checked");
				context.put("hotelName", hotelName);
				context.put("review", review);
			}else{
				redirect(response, "/my_review");
			}
		}

		template.merge(context, writer);
		out.println(writer.toString());
	}

	/** process POST Request: update modified review in reviews; */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();

		if (checkRequestError(request) != null) {
			out.println("Invalid request!");
		} else if (checkSession(request) == null) {
			redirect(response, "/modify_review");
		} else {
			String reviewId = request.getParameter("reviewId");
			String reviewTitle = request.getParameter("reviewTitle");
			String reviewText = request.getParameter("reviewText");
			int isRecom = Tools.yn2int(request.getParameter("recom"));
			int overallRating = Integer.parseInt(request.getParameter("overallRating"));
			Status status = ReviewsHandler.getInstance().updateReview(reviewTitle, reviewText, isRecom, overallRating,
					reviewId);
			if (status == Status.OK) {
				out.println("<p style=\"font-size:150%; font-family: fantasy; color: green;\">Modified review has been saved successfully!</p>");
			} else {
				out.println("<p style=\"font-size:150%; font-family: fantasy; color: red;\">Fail to save modified review!</p>");
			}

		}

	}

}
