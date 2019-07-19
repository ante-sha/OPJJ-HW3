package hr.fer.zemris.java.custom.collections;

/**
 * Iznimka koja označava pokušaj izvlačenja elementa sa stoga unatoč tome što je
 * stog prazan.
 * 
 * @author Ante Miličević
 *
 */
public class EmptyStackException extends RuntimeException {

	/** Verzija EmptyStackException-a */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor u kojemu predajemo poruku iznimke.
	 * 
	 * @param message poruka
	 */
	public EmptyStackException(String message) {
		super(message);
	}

	/** Defaultni konstruktor */
	public EmptyStackException() {
		super();
	}
}
