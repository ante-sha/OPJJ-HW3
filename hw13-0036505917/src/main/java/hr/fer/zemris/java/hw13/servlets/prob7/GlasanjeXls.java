package hr.fer.zemris.java.hw13.servlets.prob7;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import static hr.fer.zemris.java.hw13.servlets.prob7.GlasanjeUtil.*;

/**
 * Servlet za generiranje xls dokumenta iz rezultata ankete
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXls extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();
		Map<Long, Long> votes = null;
		synchronized (GlasanjeUtil.resultsMonitor) {
			votes = readVotesFromFile(req);
		}
		List<Band> bands = readBands(req);
		bands.sort((b1, b2) -> Long.compare(b1.getID(), b2.getID()));

		HSSFSheet sheet = hwb.createSheet("Voting results");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Band");
		row.createCell(1).setCellValue("Votes");

		int i = 1;
		for (Band band : bands) {
			row = sheet.createRow(i++);
			row.createCell(0).setCellValue(band.getName());
			row.createCell(1).setCellValue(votes.get(band.getID()));
		}

		res.setHeader("Content-Type", "application/vnd.ms-excel; charset=utf-8");
		res.setHeader("Content-Disposition", "attachment; filename=\"tablicaGlasova.xls\"");
		hwb.write(res.getOutputStream());
		hwb.close();
	}

}
