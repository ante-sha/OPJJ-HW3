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
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet za prihvaćanje glasa u anketi. Nakon obrade zahtjeva poziv se
 * prosljeđuje servletu GlasanjeRezultatiServlet.
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		long optionID = 0L;
		try {
			optionID = Long.parseLong(req.getParameter("optionID"));
		} catch (NumberFormatException e) {
			res.sendError(400,"Option id is missing in url");
			return;
		}
		
		DAO dao = DAOProvider.getDao();
		
		PollOption option = null;
		try {
			option = dao.retrieveOption(optionID);
		} catch (DAOException e) {
			res.sendError(404,"Option with id " + optionID + " do not exists");
			return;
		}
		
		dao.voteUpForOption(option.getOptionId());
		
		res.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + option.getPollId());
	}

}