package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Usmjernik zahtjeva koji započinju sadržavaju stazu "/servleti/author/*".
 * Zahtjev se tumači po obliku i broju parametara predanih u stazi. Nakon
 * tumačenja parametri se spremaju u kontekst zahtjeva i prosljeđuju se na
 * obradu odgovornom servletu.
 * 
 * @author Ante Miličević
 */
@WebServlet(urlPatterns = { "/servleti/author/*" })
public class AuthorDispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		chooseServlet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		chooseServlet(request, response);
	}

	/**
	 * Metoda odgovorna za prosljeđivanje zahtjeva
	 * 
	 * @param request  http zahtjev
	 * @param response http odgovor
	 * 
	 * @throws ServletException ako posluživanje zahtjeva ne uspije
	 * @throws IOException      ako prilikom čitanja zahtjeva dođe do greške
	 */
	private void chooseServlet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] params = request.getPathInfo().split("/");

		if (params.length == 0 || params.length == 1) {
			//not enough parameters
			response.setStatus(404);
			request.setAttribute("err.msg", "Page not exists");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("blog.nick", params[1]);
		if (params.length == 2) {
			//just user nick
			new BlogEntries().service(request, response);
			return;
		}
		if (params.length == 3 && (params[2].equals("new") || params[2].equals("edit"))) {
			//request for editing or adding blog
			request.setAttribute("modify.method", params[2]);
			new ModifyEntryServlet().service(request, response);
			return;
		}
		if (params.length == 3) {
			//request for blog entry preview
			request.setAttribute("blog.entry.id", params[2]);
			new BlogEntryPage().service(request, response);
			return;
		}

		//not supported request
		response.setStatus(404);
		request.setAttribute("err.msg", "Page not exists");
		request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
		return;
	}

}
