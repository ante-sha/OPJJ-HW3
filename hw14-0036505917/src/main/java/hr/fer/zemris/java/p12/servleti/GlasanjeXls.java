package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet za generiranje xls dokumenta iz rezultata ankete
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXls extends HttpServlet {
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
		// check if poll exists
		try {
			dao.retrievePoll(pollID);
		} catch (DAOException e) {
			res.sendError(404, "There is no poll with id " + pollID);
			return;
		}

		List<PollOption> options = dao.retrieveOptions(pollID);

		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("Voting results");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Option");
		row.createCell(1).setCellValue("Votes");

		int i = 1;
		for (PollOption option : options) {
			row = sheet.createRow(i++);
			row.createCell(0).setCellValue(option.getName());
			row.createCell(1).setCellValue(option.getVotesCount());
		}

		res.setHeader("Content-Type", "application/vnd.ms-excel; charset=utf-8");
		res.setHeader("Content-Disposition", "attachment; filename=\"tablicaGlasova.xls\"");
		hwb.write(res.getOutputStream());
		hwb.close();
	}

}