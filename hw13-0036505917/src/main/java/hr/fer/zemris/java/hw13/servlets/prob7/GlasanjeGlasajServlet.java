package hr.fer.zemris.java.hw13.servlets.prob7;

import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.readVotesFromFile;
import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.writeVotesInFile;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za prihvaćanje glasa u anketi. Nakon obrade zahtjeva poziv se
 * prosljeđuje servletu GlasanjeRezultatiServlet.
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		synchronized (GlasanjeUtil.resultsMonitor) {
			Map<Long, Long> data = readData(req);

			writeVotesInFile(req, data);
		}

		res.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Metoda za čitanje rezultata ankete. Ako datoteka rezultati koji se generiraju
	 * su oblika idBenda:0.
	 * 
	 * @param req  zahtjev
	 * @return rezultati ankete oblika idBenda:brojGlasova
	 * 
	 * @throws IOException ako čitanje datoteke ne uspije
	 */
	private Map<Long, Long> readData(HttpServletRequest req) throws IOException {
		Map<Long, Long> votes = null;
		votes = readVotesFromFile(req);
		
		votes.compute(Long.parseLong(req.getParameter("id")), (id, old) -> {
			if (old == null) {
				old = 0L;
			}
			return old + 1;
		});

		return votes;
	}

}
