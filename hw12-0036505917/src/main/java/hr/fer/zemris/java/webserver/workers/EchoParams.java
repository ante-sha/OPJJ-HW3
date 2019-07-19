package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji ispisuje tablicu parametara predanih u url-u zahtjeva
 * 
 * @author Ante Miličević
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		
		context.write("<html><head><title>EchoParams</title><style>\n" + 
				"   table, th, td {" + 
				"    border: 2px solid black;" + 
				"   }" + 
				"  </style><head><body>");
		context.write(
				"<table><thead>" + 
				" <tr><th>Naziv</th><th>Vrijednost</th></tr>" + 
				" </thead>\n" + 
				"<tbody>"
				);
		for(String paramName : context.getParameterNames()) {
			String value = context.getParameter(paramName);
			
			context.write("<tr><td>" + paramName +  "</td><td>" + value + "</td></tr>");
		}
		
		context.write(
				"</tbody>" + 
				"</table>" + 
				"</body>" + 
				"</html>"
				);
		
	}

}
