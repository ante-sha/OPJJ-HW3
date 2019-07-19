/**
 * Paket u kojem se nalaze razredi koji su rezultat rješavanja
 * 2. zadatka 3. domaće zadaće.
 */
package hr.fer.zemris.java.hw03.prob1;

/**
 * Iznimka koja označava pogrešku u formatu teksta unesenog u lexer.
 * @author Ante Miličević
 *
 */
public class LexerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**Defaultni konstruktor*/
	public LexerException() {}

	/**Konstruktor s razlogom bacanja iznimke*/
	public LexerException(String message) {
		super(message);
	}

}
