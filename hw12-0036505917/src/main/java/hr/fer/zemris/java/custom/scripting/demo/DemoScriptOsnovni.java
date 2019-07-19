package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Demonstracijski primjer rada skripte osnovni.smscr
 * 
 * @author Ante Miličević
 *
 */
public class DemoScriptOsnovni {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		String documentBody = readFromDisk("osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RequestContext.RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}

	/**
	 * Metoda za čitanje skripte naziva scriptName koja se nalazi u /webroot/scripts
	 * 
	 * @param scriptName naziv skripte
	 * 
	 * @return sadržaj skripte
	 */
	public static String readFromDisk(String scriptName) {
		try {
			return Files.readString(Paths.get("./webroot/scripts", scriptName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
