package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za preusmjeravanje zahtjeva koji stižu na stranicu index.jsp.
 * 
 * @author Ante Miličević
 */
@WebServlet("/index.jsp")
public class RedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/servleti/main");
	}

}
