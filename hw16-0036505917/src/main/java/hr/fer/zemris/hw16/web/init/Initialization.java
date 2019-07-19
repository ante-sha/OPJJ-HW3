package hr.fer.zemris.hw16.web.init;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.zemris.hw16.data.DataStorage;

/**
 * Promatrač kojega se pokreče prilikom podizanja servera radi inicijalizacije
 * razreda odgovornog za dohvat podataka o slikama.
 * 
 * @author Ante Miličević
 *
 */
@WebListener
public class Initialization implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String filePathToDescriptor = arg0.getServletContext().getRealPath("/WEB-INF/opisnik.txt");
		Path pathToDescriptor = Paths.get(filePathToDescriptor);
		
		String filePathToImages = arg0.getServletContext().getRealPath("/WEB-INF/slike");
		Path pathToImages = Paths.get(filePathToImages);
		
		String filePathToThumbs = arg0.getServletContext().getRealPath("/WEB-INF/thumbnails");
		Path pathToThumbs = Paths.get(filePathToThumbs);
		
		try {
			DataStorage.initializePaths(pathToDescriptor, pathToImages, pathToThumbs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
