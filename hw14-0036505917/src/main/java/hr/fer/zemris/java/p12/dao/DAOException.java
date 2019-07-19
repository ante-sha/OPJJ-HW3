package hr.fer.zemris.java.p12.dao;

/**
 * Iznimka koju se izaziva prilikom greške u radu s podacima koje pruža
 * {@link DAO}.
 * 
 * @author Ante Miličević
 *
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 */
	public DAOException() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka iznimke
	 * @param cause uzrok iznimke
	 * @param enableSuppression oznaka za potiskivanje iznimki
	 * @param writableStackTrace oznaka za mogućnost ispisa stack trace-a
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka iznimke
	 * @param cause uzrok iznimke
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message poruka iznimke
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Konstruktor
	 * 
	 * @param cause uzrok iznimke
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}