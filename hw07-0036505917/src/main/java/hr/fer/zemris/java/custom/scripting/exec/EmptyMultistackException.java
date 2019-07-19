package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Razred koji modelira iznimku koja se baca ako dođe do pokušaja dohvaćanja
 * elemenata stoga unutar jednog od stogova iz {@link ObjectMultistack} koji je
 * prazan.
 * 
 * @author Ante Miličević
 *
 */
public class EmptyMultistackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor
	 */
	public EmptyMultistackException() {
		super();
	}

	/**
	 * Konstuktor s porukom iznimke
	 * 
	 * @param message poruka iznimke
	 */
	public EmptyMultistackException(String message) {
		super(message);
	}
}
