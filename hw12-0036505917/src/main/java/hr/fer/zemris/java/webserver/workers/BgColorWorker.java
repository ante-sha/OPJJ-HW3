package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji implementira radnika koji mijenja pozadinsku boju početne
 * stranice.
 * 
 * @author Ante Miličević
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		String color = context.getParameter("bgcolor");
		if (color != null && color.matches("[0-9|A-F|a-f]{6}")) {
			context.setPersistantParameter("bgcolor", color);
			context.write(generateLinkForMessage("Link to updated page"));
		} else {
			context.write(generateLinkForMessage("Link to unchanged page"));
		}
	}

	/**
	 * Metoda koja generira html sa linkom na početnu stranicu gdje će naziv linka
	 * biti linkName
	 * 
	 * @param linkName tekst zapisan u linku
	 * 
	 * @return html kod linka
	 */
	private String generateLinkForMessage(String linkName) {
		return "<html><head><title>Update</title></head>" + "<body>" + "<a href=\"/index2.html\" align=\"center\">"
				+ linkName + "</a>" + "<hr></body></html>";
	}

}
