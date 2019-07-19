package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.forms.CommentForm;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

/**
 * Servlet koji omogućava pregled određenog blog unosa te podnošenje
 * zahtjeva za komentiranje istog. Ako je ulogirani user vlasnik unosa
 * bloga njemu je omogućeno podnošenje zahtjeva za uređivanje bloga.
 * 
 * @author Ante Miličević
 *
 */
public class BlogEntryPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String eidParam = request.getAttribute("blog.entry.id").toString();
		//entry id
		Long eid = 0L;
		try {
			eid = Long.parseLong(eidParam);
		} catch (NumberFormatException e) {
			response.setStatus(400);
			request.setAttribute("err.msg", "EID is missing");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}

		DAO dao = DAOProvider.getDAO();

		BlogEntry entry = dao.getBlogEntry(eid);

		if (entry == null) {
			response.setStatus(404);
			request.setAttribute("err.msg", "Entry do not exists");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}

		if (entry.getCreator().getId().equals(request.getSession().getAttribute("current.user.id"))) {
			request.setAttribute("editable", true);
		}

		request.setAttribute("entry", entry);

		request.getRequestDispatcher("/WEB-INF/pages/blogEntryPage.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String eidParam = request.getAttribute("blog.entry.id").toString();
		//entry id
		Long eid = 0L;
		try {
			eid = Long.parseLong(eidParam);
		} catch (NumberFormatException e) {
			response.setStatus(400);
			request.setAttribute("err.msg", "EID is missing");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}

		DAO dao = DAOProvider.getDAO();

		BlogEntry entry = dao.getBlogEntry(eid);
		
		request.setAttribute("entry", entry);

		if (entry == null) {
			response.setStatus(404);
			request.setAttribute("err.msg", "Entry do not exists");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}
		
		if (entry.getCreator().getId().equals(request.getSession().getAttribute("current.user.id"))) {
			request.setAttribute("editable", true);
		}

		CommentForm form = new CommentForm();
		form.fillFromRequest(request);

		BlogComment comment = new BlogComment();
		form.fillComment(comment);

		if(form.hasErrors()) {
			request.setAttribute("form", form);
			request.getRequestDispatcher("/WEB-INF/pages/blogEntryPage.jsp").forward(request, response);
			return;
		}
		
		comment.setBlogEntry(entry);
		comment.setPostedOn(new Date());

		dao.createComment(comment);

		//disable refresh submit request
		response.sendRedirect(
				request.getContextPath() + "/servleti/author/" + entry.getCreator().getNick() + "/" + entry.getId());
	}

}
