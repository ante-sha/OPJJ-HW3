package hr.fer.zemris.hw06.shell.parser;

/**
 * Iznimka koja se baca ako pri čitanju podataka iz {@link MyShell}-a se
 * pročitaju ne definirani podaci.
 * 
 * @author Ante Miličević
 *
 */
public class ShellParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor
	 */
	public ShellParserException() {
		super();
	}

	/**
	 * Konstruktor s porukom iznimke
	 * 
	 * @param message poruka iznimke
	 */
	public ShellParserException(String message) {
		super(message);
	}

	/**
	 * Konstruktor s uzrokom iznimke
	 * 
	 * @param cause uzrok iznimke
	 */
	public ShellParserException(Throwable cause) {
		super(cause);
	}

}
