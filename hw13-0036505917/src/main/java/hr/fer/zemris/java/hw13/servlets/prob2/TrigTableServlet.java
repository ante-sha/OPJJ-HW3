package hr.fer.zemris.java.hw13.servlets.prob2;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji ispisuje tablicu sinusa i kosinusa za cjelobrojne stupnjeve u zadanom
 * rasponu. Stranica se generira pomoću /WEB-INF/pages/trig.jsp
 */
@WebServlet("/trigonometric")
public class TrigTableServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String aString = request.getParameter("a");
		String bString = request.getParameter("b");
		int a = 0;
		int b = 360;
		
		try {
			a = Integer.parseInt(aString);
		} catch (NumberFormatException e) {}
		try {
			b = Integer.parseInt(bString);
		} catch (NumberFormatException e) {}
		
		if(a > b) {
			int t = b;
			b = a;
			a = t;
		}
		
		if (b > a + 720) {
			b = a + 720;
		}
		request.setAttribute("a", a);
		request.setAttribute("b", b);
		request.getRequestDispatcher("/WEB-INF/pages/trig.jsp").forward(request, response);
	}

}
