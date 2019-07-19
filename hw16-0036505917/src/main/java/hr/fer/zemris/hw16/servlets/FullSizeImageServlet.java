package hr.fer.zemris.hw16.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.hw16.data.DataStorage;

/**
 * Servlet odgovoran za dohvat slike čije se ime nalazi u stazi zahtjeva.
 * 
 * @author Ante Miličević
 */
@WebServlet("/image/*")
public class FullSizeImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		if(pathInfo.length != 2 || !pathInfo[0].equals("")) {
			response.sendError(404, "Page don't exists");
			return;
		}
		String imageName = pathInfo[1];
		if(!DataStorage.imageExists(imageName)) {
			response.sendError(404, "Resource don't exists");
			return;
		}
		
		BufferedImage image = DataStorage.getFullImageForName(imageName);
		response.setContentType("image/jpg");
		ImageIO.write(image, "jpg", response.getOutputStream());
	}
}
