package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji modelira radnika koji generira početnu stranicu koja sadrži
 * poveznice na sve ostale radnike.
 * 
 * @author Ante Miličević
 *
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getPersistantParameter("bgcolor");

		context.setTemporaryParameter("background", color == null ? "7F7F7F" : color);

		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
