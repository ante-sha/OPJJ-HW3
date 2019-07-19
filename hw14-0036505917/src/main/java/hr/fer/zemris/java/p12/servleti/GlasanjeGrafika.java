package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet za iscrtavanje kružnog grafa koji se temelji na podacima rezultata
 * ankete.
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafika extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long pollID = 0L;
		try {
			pollID = Long.parseLong(request.getParameter("pollID"));
		} catch (NumberFormatException e) {
			response.sendError(400, "Poll id is missing in url");
			return;
		}

		DAO dao = DAOProvider.getDao();

		try {
			dao.retrievePoll(pollID);
		} catch (DAOException e) {
			response.sendError(404, "There is no poll with id " + pollID);
			return;
		}

		response.setContentType("image/png");

		OutputStream os = response.getOutputStream();

		List<PollOption> options = dao.retrieveOptions(pollID);

		JFreeChart chart = generateChart(request, options);
		int width = 500;
		int height = 500;
		ChartUtilities.writeChartAsPNG(os, chart, width, height);
	}

	/**
	 * Metoda za generiranje kružnog grafa iz podataka ankete
	 * 
	 * @param req zahtjev
	 * @param options opcije ankete
	 * 
	 * @return kružni graf
	 * 
	 * @throws IOException ako učitavanje ankete ne uspije
	 */
	private JFreeChart generateChart(HttpServletRequest req, List<PollOption> options) throws IOException {
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (PollOption option : options) {
			dataset.setValue(option.getName(), option.getVotesCount());
		}

		JFreeChart chart = ChartFactory.createPieChart("Votes", dataset, true, false, false);
		return chart;
	}

}