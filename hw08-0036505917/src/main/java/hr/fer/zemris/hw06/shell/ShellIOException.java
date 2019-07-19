package hr.fer.zemris.hw06.shell;

/**
 * Iznimka koju se generira ako se prilikom komunikacije s korisnikom dogodi
 * greška.
 * 
 * @author Ante Miličević
 *
 */
public class ShellIOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor
	 */
	public ShellIOException() {
		super();
	}

	/**
	 * Konstruktor koji prima poruku i uzrok izazivanja iznimke
	 * 
	 * @param message poruka
	 * @param cause uzrok
	 */
	public ShellIOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor koji prima poruku kroz konstruktor
	 * 
	 * @param message poruka iznimke
	 */
	public ShellIOException(String message) {
		super(message);
	}

}
