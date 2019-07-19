package hr.fer.zemris.java.hw13.servlets.prob7;

import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.readBands;
import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.readVotesFromFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za pružanje informacija o rezultatima ankete.
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<Band> bands = readBands(req);
		req.setAttribute("bands", bands);
		
		Map<Long, Long> votes = null;
		synchronized (GlasanjeUtil.resultsMonitor) {
			votes = readVotesFromFile(req);
			req.setAttribute("votes", votes);
		}
		

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, res);
	}

}
