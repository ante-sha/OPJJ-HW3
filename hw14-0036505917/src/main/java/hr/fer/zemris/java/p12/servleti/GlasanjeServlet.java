package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Servlet za pružanje informacija o mogućim opcijama za glasanje u anketi.
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		DAO dao = DAOProvider.getDao();
		
		Long pollID = 0L;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
		} catch (NumberFormatException e) {
			res.sendError(400, "Poll id is missing in url");
			return;
		}
		
		try {
			req.setAttribute("dao.poll", dao.retrievePoll(pollID));
		} catch (DAOException e) {
			res.sendError(404, "There is no poll with id " + pollID);
			return;
		}
		
		req.setAttribute("dao.options", dao.retrieveOptions(pollID));
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, res);
	}

}
