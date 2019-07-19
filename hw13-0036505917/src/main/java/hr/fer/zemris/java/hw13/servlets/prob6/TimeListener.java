package hr.fer.zemris.java.hw13.servlets.prob6;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Promatrač koji prilikom paljenja servera zapisuje trenutno vrijeme u memoriju
 * servera.
 */
@WebListener
public class TimeListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		arg0.getServletContext().removeAttribute("startUpTime");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		arg0.getServletContext().setAttribute("startUpTime", System.currentTimeMillis());
	}

}
