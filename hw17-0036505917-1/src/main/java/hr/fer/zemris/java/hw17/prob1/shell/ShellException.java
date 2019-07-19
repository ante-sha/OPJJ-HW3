package hr.fer.zemris.java.hw17.prob1.shell;

/**
 * Iznimka koja se izaziva prilikom greške pri radu s ljuskom.
 * 
 * @author Ante Miličević
 *
 */
public class ShellException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 */
	public ShellException() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka iznimke
	 * @param cause uzrok
	 * @param enableSuppression zastavica za dozvolu potiskivanja
	 * @param writableStackTrace zastavica za dozvolu ispisa stack tracea
	 */
	public ShellException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka
	 * @param cause uzrok
	 */
	public ShellException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka
	 */
	public ShellException(String message) {
		super(message);
	}

	/**
	 * Konstruktor
	 * 
	 * @param cause uzrok
	 */
	public ShellException(Throwable cause) {
		super(cause);
	}
}
