package hr.fer.zemris.java.hw13.servlets.prob7;

import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.readBands;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za pružanje informacija o mogućim opcijama za glasanje u anketi.
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<Band> bands = readBands(req);

		req.setAttribute("bands", bands);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, res);
	}

}
