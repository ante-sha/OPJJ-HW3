/**
 * Paket u koji sadrži sve razrede koje koristi {@link SmartScriptLexer}
 */
package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Iznimka koja označava grešku u prilikom rada sa {@link SmartScriptLexer}-om
 * @author Ante Miličević
 *
 */
public class SmartScriptLexerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**Defaultni konstruktor*/
	public SmartScriptLexerException() {
		super();
	}

	/**Konstruktor s porukom razloga iznimke*/
	public SmartScriptLexerException(String message) {
		super(message);
	}

}
