package hr.fer.zemris.java.custom.scripting.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Demonstracijski primjer rada skripte brojPoziva.smscr
 * 
 * @author Ante Miličević
 *
 */
public class DemoScriptBrojPoziva {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		String documentBody = DemoScriptOsnovni.readFromDisk("brojPoziva.smscr");
		
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RequestContext.RCCookie> cookies = new ArrayList<>();
		
		persistentParameters.put("brojPoziva", "3");
		
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		
		System.out.println("Vrijednost u mapi: " + rc.getPersistantParameter("brojPoziva"));
	}
}
