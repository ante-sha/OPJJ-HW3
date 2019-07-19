/**
 * Paket u kojem se nalazi parser i iznimka koja se baca ako dođe do greške pri parsiranju.
 */
package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Iznimka koja označava pogrešku pri parsiranju.
 * @author Ante Miličević
 *
 */
public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**Defaultni konstruktor*/
	public SmartScriptParserException() {
		super();
	}

	/**Konstruktor koji definira poruku iznimke*/
	public SmartScriptParserException(String message) {
		super(message);
	}
	
	/**
	 * Konstruktor koji definira poruku i razlog bacanja iznimke.
	 * @param message poruka iznimke
	 * @param cause iznimka koja je prouzročila ovu
	 */
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
