package hr.fer.zemris.java.hw13.servlets.prob1;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji postavlja atribut sjednice koji označava boju pozadine ako je
 * boja u dobrom formatu
 */
@WebServlet("/setcolor")
public class SetColorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String s = (String) request.getParameter("color");

		if (s != null && s.matches("[0-9|a-f|A-F]{6}")) {
			request.getSession().setAttribute("pickedBgColor", s);
		}

		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

}
