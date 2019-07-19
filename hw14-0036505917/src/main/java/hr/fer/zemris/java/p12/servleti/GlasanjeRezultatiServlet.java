package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet za pružanje informacija o rezultatima ankete.
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long pollID = 0L;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
		} catch (NumberFormatException e) {
			res.sendError(400, "Poll id is missing in url");
			return;
		}
		
		DAO dao = DAOProvider.getDao();
		
		Poll poll = null;
		try {
			poll = dao.retrievePoll(pollID);
		} catch (DAOException e) {
			res.sendError(404, "There is no poll with id " + pollID);
			return;
		}
		
		req.setAttribute("dao.poll", poll);
		
		List<PollOption> options = dao.retrieveOptions(pollID);
		
		req.setAttribute("dao.options", options);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, res);
	}

}