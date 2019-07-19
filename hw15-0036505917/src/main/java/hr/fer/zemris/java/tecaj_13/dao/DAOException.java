package hr.fer.zemris.java.tecaj_13.dao;

/**
 * Iznimka koja signalizira da je došlo do greške pri radu s slojem
 * za perzistenciju podataka.
 * 
 * @author Ante Miličević
 *
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 * 
	 * @param message poruka
	 * @param cause	uzrok
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka
	 */
	public DAOException(String message) {
		super(message);
	}
}