package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.forms.LoginForm;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet početne stranice, post metodom dobiva zahtjeve za logiranje dok get
 * metodom samo ispisuje generičku stranicu.
 * 
 * @author Ante Miličević
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DAO dao = DAOProvider.getDAO();

		List<BlogUser> users = dao.getBlogUsers();

		request.setAttribute("users", users);

		request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LoginForm form = new LoginForm();
		form.fillFromRequest(request);

		request.setAttribute("form", form);

		DAO dao = DAOProvider.getDAO();

		List<BlogUser> users = dao.getBlogUsers();

		request.setAttribute("users", users);

		if (form.hasErrors()) {
			request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
			return;
		}

		BlogUser user = new BlogUser();
		form.fillUser(user);

		BlogUser dbUser = dao.getBlogUserByNick(user.getNick());
		if (dbUser == null) {
			form.setError("nick", "Invalid username or password");
			request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
			return;
		}

		if (!dbUser.getPasswordHash().equals(user.getPasswordHash())) {
			form.setError("nick", "Invalid username or password");
			request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
			return;
		}

		//login user
		request.getSession().setAttribute("current.user.id", dbUser.getId());
		request.getSession().setAttribute("current.user.fn", dbUser.getFirstName());
		request.getSession().setAttribute("current.user.ln", dbUser.getLastName());
		request.getSession().setAttribute("current.user.email", dbUser.getEmail());
		request.getSession().setAttribute("current.user.nick", dbUser.getNick());
		response.sendRedirect(request.getContextPath() + "/servleti/main");
	}

}
