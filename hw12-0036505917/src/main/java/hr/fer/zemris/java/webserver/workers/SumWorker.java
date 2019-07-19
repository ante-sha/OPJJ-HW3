package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji modelira radnika koji zbraja dva broja koji su zadani u url-u
 * zahtjeva ili pretpostavlja da su oni generičkih vrijednosti ako se tamo ne
 * nalaze. Uz rezultat se ispisuje jedna od dvije slike iz /webroot/images
 * ovisno o parnosti zbroja.
 * Generiranje htmla se odvija pomoću skripte calc.smscr
 * 
 * @author Ante Miličević
 *
 */
public class SumWorker implements IWebWorker {
	/**
	 * Generička vrijednost parametra a
	 */
	private static final int defaultA = 1;
	/**
	 * Generička vrijednost parametra b
	 */
	private static final int defaultB = 2;

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = 0;
		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch (Exception e) {
			a = defaultA;
		}

		int b = 0;
		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch (Exception e) {
			b = defaultB;
		}
		int sum = a + b;

		context.setTemporaryParameter("zbroj", Integer.toString(sum));

		context.setTemporaryParameter("varA", Integer.toString(a));
		context.setTemporaryParameter("varB", Integer.toString(b));

		context.setTemporaryParameter("imgName", sum % 2 == 1 ? "brick.jpg" : "gunpowder.jpg");

		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}

}
