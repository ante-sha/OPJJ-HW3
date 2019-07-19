package hr.fer.zemris.java.hw13.servlets.prob7;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.*;

/**
 * Servlet za iscrtavanje kružnog grafa koji se temelji na podacima
 * rezultata ankete.
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafika extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/png");
		
		OutputStream os = response.getOutputStream();
		
		JFreeChart chart = generateChart(request);
		int width = 500;
		int height = 500;
		ChartUtilities.writeChartAsPNG(os, chart, width, height);
	}

	/**
	 * Metoda za generiranje kružnog grafa iz podataka ankete
	 * 
	 * @param req zahtjev
	 * @return kružni graf
	 * 
	 * @throws IOException ako učitavanje ankete ne uspije
	 */
	private JFreeChart generateChart(HttpServletRequest req) throws IOException {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		Map<Long, Long> votes = null;
		synchronized (GlasanjeUtil.resultsMonitor) {
			votes = readVotesFromFile(req);
		}
		List<Band> bands = readBands(req);
		
		for(Band band : bands) {
			dataset.setValue(band.getName(), votes.get(band.getID()));
		}
		
		JFreeChart chart = ChartFactory.createPieChart("Votes", dataset, true,false,false);
		return chart;
	}

}
