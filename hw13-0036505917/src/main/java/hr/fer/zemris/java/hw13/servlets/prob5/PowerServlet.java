package hr.fer.zemris.java.hw13.servlets.prob5;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet koji generira xsl dokument koji je formiran preko 3 predana parametra
 * u urlu zahtjeva. "a" definira početni broj na svakoj stranici dokumenta, "b"
 * definira zadnji broj, "n" definira broj stranica. Na svakoj stranici uz
 * brojeve [a,b] ispisuje se i i^p gdje je "i" trenutni broj između "a" i "b", a
 * p trenutna stranica.
 */
@WebServlet("/powers")
public class PowerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int a = Integer.parseInt(request.getParameter("a"));
			int b = Integer.parseInt(request.getParameter("b"));
			int n = Integer.parseInt(request.getParameter("n"));

			checkRange(a, -100, 100);
			checkRange(b, -100, 100);
			checkRange(n, 1, 5);

			request.setAttribute("a", a);
			request.setAttribute("b", b);
			request.setAttribute("n", n);

			generateXLSTable(a < b ? a : b, b > a ? b : a, n, request, response);
			return;

		} catch (NumberFormatException ex) {
			request.setAttribute("errorMessage", "Parameters are in invalid format");
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorMessage", "Parameters are not in valid range");
		}

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		request.getRequestDispatcher("/WEB-INF/pages/powerError.jsp").forward(request, response);
		;
	}

	/**
	 * Metoda za generiranje xls tablice
	 * 
	 * @param a        početni indeks
	 * @param b        završni indeks
	 * @param n        broj stranica
	 * @param request  zahtjev
	 * @param response odgovor
	 * @throws IOException ako pisanje prema korisniku ne uspije
	 */
	private void generateXLSTable(int a, int b, int n, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();

		for (int i = 0; i < n; i++) {
			HSSFSheet sheet = hwb.createSheet((i + 1) + ". sheet");

			for (int j = a; j <= b; j++) {
				long num = (int) Math.pow(j, i + 1);
				HSSFRow row = sheet.createRow(j - a);
				row.createCell(0).setCellValue(j);
				row.createCell(1).setCellValue(num);
			}
		}

		response.setHeader("Content-Type", "application/vnd.ms-excel; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		hwb.write(response.getOutputStream());
		hwb.close();
	}

	/**
	 * Metoda koja provjerava raspon
	 * 
	 * @param a broj
	 * @param i minimum
	 * @param j maksimum
	 * 
	 * @throws IllegalArgumentException ako raspon nije zadovoljen
	 */
	private void checkRange(int a, int i, int j) {
		if (a < i || a > j) {
			throw new IllegalArgumentException();
		}
	}

}
