package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.forms.EntryForm;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet za umetanje ili uređivanje unosa bloga. Nakon uspješnog umetanja unosa
 * bloga korisnik se redirecta na pregled umetnutog unosa bloga.
 * 
 * @author Ante Miličević
 *
 */
public class ModifyEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//if user is not logged
		if(request.getSession().getAttribute("current.user.id") == null) {
			response.setStatus(401);
			request.setAttribute("err.msg", "You must be logged for this action");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}
		
		String method = request.getAttribute("modify.method").toString();
		
		EntryForm form = new EntryForm();
		
		try {
			if("edit".equals(method)) {
				prepareForEdit(request, response, form);
			}
		} catch (Exception e) {
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			e.printStackTrace();
		}
		
		request.setAttribute("form", form);
		request.getRequestDispatcher("/WEB-INF/pages/blogEdit.jsp").forward(request, response);
	}

	private BlogEntry prepareForEdit(HttpServletRequest request, HttpServletResponse response, EntryForm form) {
		//entry id
		long eid = 0L;
		
		try {
			eid = Long.parseLong(request.getParameter("EID"));
		} catch (NumberFormatException e) {
			response.setStatus(400);
			request.setAttribute("err.msg", "Entry ID is missing");
			throw new RuntimeException("Entry ID is missing");
		}
		
		DAO dao = DAOProvider.getDAO();
		
		BlogEntry entry = dao.getBlogEntry(eid);
		
		if(entry == null) {
			response.setStatus(400);
			request.setAttribute("err.msg", "Entry do not exists");
			throw new RuntimeException("Entry not found");
		}
		
		request.setAttribute("entry", entry);
		
		BlogUser user = dao.getBlogUserById((Long)request.getSession().getAttribute("current.user.id"));
		
		if(entry.getCreator().getId() != user.getId()) {
			response.setStatus(401);
			request.setAttribute("err.msg", "You don't have permission for this action");
			throw new RuntimeException("Unauthorized");
		}
		
		//Edit is called for the first time
		if(request.getParameter("title") == null && request.getParameter("text") == null) {
			form.fillFormFromEntry(entry);
		}
		
		return entry;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//if user is not logged
		if(request.getSession().getAttribute("current.user.id") == null) {
			response.setStatus(401);
			request.setAttribute("err.msg", "You must be logged for this action");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}
		
		String method = request.getAttribute("modify.method").toString();
		
		EntryForm form = new EntryForm();
		form.fillFromRequest(request);
		
		DAO dao = DAOProvider.getDAO();
		
		try {
			if("edit".equals(method)) {
				
				BlogEntry entry = prepareForEdit(request,response, form);
				
				if(!form.hasErrors()) {
					entry.setLastModifiedAt(new Date());
					form.fillEntry(entry);
				}
				
			} else {
				
				BlogEntry entry = new BlogEntry();
				form.fillEntry(entry);
				entry.setCreatedAt(new Date());
				entry.setCreator(dao.getBlogUserById((Long)request.getSession().getAttribute("current.user.id")));
				
				if(!form.hasErrors()) {
					dao.createEntry(entry);
					//redirect to entry preview
					response.sendRedirect(request.getContextPath() + "/servleti/author/" + URLEncoder.encode((String)request.getSession().getAttribute("current.user.nick"), StandardCharsets.UTF_8) + "/" + entry.getId());
					return;
				}
				
			}
			
			request.setAttribute("form", form);
			request.getRequestDispatcher("/WEB-INF/pages/blogEdit.jsp").forward(request, response);
		} catch (Exception e) {
			//if exception is not calculated
			if(request.getAttribute("err.msg") == null) {
				request.setAttribute("err.msg", "Unexpected error");
				response.setStatus(500);
			}
			
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			e.printStackTrace();
		}
	}

}
