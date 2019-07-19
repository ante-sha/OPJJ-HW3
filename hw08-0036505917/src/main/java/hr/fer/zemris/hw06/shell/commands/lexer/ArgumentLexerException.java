package hr.fer.zemris.hw06.shell.commands.lexer;

/**
 * Razred koji modelira iznimku koja se baca kad dođe do pogreške prilikom
 * lekseriranja argumenata pomoću {@link ArgumentLexer}-a.
 * 
 * @author Ante Miličević
 *
 */
public class ArgumentLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor
	 */
	public ArgumentLexerException() {
		super();
	}

	/**
	 * Konstruktor s porukom iznimke
	 * @param message poruka iznimke
	 */
	public ArgumentLexerException(String message) {
		super(message);
	}

	/**
	 * Konstruktor s uzrokom iznimke
	 * @param cause uzrok iznimke
	 */
	public ArgumentLexerException(Throwable cause) {
		super(cause);
	}

}
