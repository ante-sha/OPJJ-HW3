package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet za posluživanje zahtjeva listanja unosa bloga jednog korisnika.
 * 
 * @author Ante Miličević
 */
public class BlogEntries extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nick = request.getAttribute("blog.nick").toString();
		
		request.setAttribute("nick", nick);
		
		if(nick.equals(request.getSession().getAttribute("current.user.nick"))) {
			request.setAttribute("showAdd", true);
		}
		
		DAO dao = DAOProvider.getDAO();
		
		BlogUser user = dao.getBlogUserByNick(nick);
		
		if(user == null) {
			response.setStatus(400);
			request.setAttribute("err.msg", "User with nick " + nick + " do not exists");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}
		
		request.setAttribute("entries", user.getEntries());
		
		request.getRequestDispatcher("/WEB-INF/pages/blogEntries.jsp").forward(request, response);;
	}

}
