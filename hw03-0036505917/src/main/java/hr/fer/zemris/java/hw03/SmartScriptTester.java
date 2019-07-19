/**
 * Paket u kojemu se nalazi demonstracija korištenja parsera
 */
package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program koji demonstrira rad parsera. Prima stazu do dokumenta kao argument pri pokretanju programa
 * dokument pročita i prosljedi parseru, nakon što se odvije parsiranje pozove se ispis izparsiranog
 * teksta preko vršnog čvora dokumenta.
 * @author Ante Miličević
 *
 */
public class SmartScriptTester {
	/**
	 * Ulazna točka programa
	 * @param args prima se jedan argument koji označava stazu do dokumenta za parsiranje
	 * @throws IOException ako se dogodi pogreška pri čitanju datoteke
	 */
	public static void main(String[] args) throws IOException {
		if(args.length!=1) {
			System.err.println("Put do dokumenta nije dobar!");
			System.exit(1);
		}
		
//		čitanje dokumenta čiji je path predan kao argument programa!
		String docBody = new String(
				 Files.readAllBytes(Paths.get(args[0])),
				 StandardCharsets.UTF_8
				);
		
//		odkomentiraj da vidiš ispis grešaka
//		String docBody = "Greska1 {$for i p 2333333333333333333333333333333333$}{$end$}";
//		String docBody = "Greska2 \\p";
//		String docBody = "Greska3 {$pric $}";
//		String docBody = "Greska4 {$= =$}";
		
		SmartScriptParser parser = null;
		
		try {
			 parser = new SmartScriptParser(docBody);
			 System.out.println(SmartScriptParser.createOriginalDocumentBody(parser.getDocumentNode()));
		} catch(SmartScriptParserException e) {
			 System.out.println("Unable to parse document!");
			 System.out.println(e.getCause().getMessage());
			 System.exit(-1);
		} catch(Exception e) {
			 System.out.println("If this line ever executes, you have failed this class!");
			 System.exit(-1);
		}
			
	}
		
}
