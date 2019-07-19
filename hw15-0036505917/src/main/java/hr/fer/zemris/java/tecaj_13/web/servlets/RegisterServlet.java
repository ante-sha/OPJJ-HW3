package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.forms.RegisterForm;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet koji omogućuje registraciju korisnika preko {@link RegisterForm} formulara.
 * 
 * @author Ante Miličević
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RegisterForm form = new RegisterForm();
		
		form.fillFromRequest(request);
		
		request.setAttribute("form", form);
		
		if(form.hasErrors()) {
			request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
			return;
		}
		
		DAO dao = DAOProvider.getDAO();
		
		BlogUser dbUser = dao.getBlogUserByNick(form.getNick());
		
		if(dbUser != null) {
			form.setError("nick", "Nick already exist");
			request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
			return;
		}
		
		BlogUser user = new BlogUser();
		
		form.fillUser(user);
		
		dao.createUser(user);
		
		//login user
		request.getSession().setAttribute("current.user.id", user.getId());
		request.getSession().setAttribute("current.user.fn", user.getFirstName());
		request.getSession().setAttribute("current.user.ln", user.getLastName());
		request.getSession().setAttribute("current.user.email", user.getEmail());
		request.getSession().setAttribute("current.user.nick", user.getNick());
		
		response.sendRedirect(request.getContextPath() + "/servleti/main");
	}

}
