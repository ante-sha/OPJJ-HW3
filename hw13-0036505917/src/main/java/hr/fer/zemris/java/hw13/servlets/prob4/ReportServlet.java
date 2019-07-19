package hr.fer.zemris.java.hw13.servlets.prob4;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet implementation class ReportServlet
 */
@WebServlet("/reportImage")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/png");
		
		OutputStream os = response.getOutputStream();
		
		JFreeChart chart = generateChart();
		int width = 500;
		int height = 350;
		ChartUtilities.writeChartAsPNG(os, chart, width, height);
	}

	private JFreeChart generateChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		dataset.setValue("Linux", 29);
		dataset.setValue("Mac", 20);
		dataset.setValue("Windows", 51);
		
		JFreeChart chart = ChartFactory.createPieChart("OS distibution", dataset, true,false,false);
		return chart;
	}

}
