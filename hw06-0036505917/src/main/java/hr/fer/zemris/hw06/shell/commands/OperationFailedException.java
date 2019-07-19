package hr.fer.zemris.hw06.shell.commands;

/**
 * Iznimka koja označava da je došlo do pogreške u definiranju
 * {@link ShellCommand} naredbe.
 * 
 * @author Ante Miličević
 *
 */
public class OperationFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor
	 */
	public OperationFailedException() {
		super();
	}

	/**
	 * Konstruktor s porukom iznimke
	 * @param message poruka iznimke
	 */
	public OperationFailedException(String message) {
		super(message);
	}

	/**
	 * Konstruktor s uzrokom iznimke
	 * 
	 * @param cause uzrok iznimke
	 */
	public OperationFailedException(Throwable cause) {
		super(cause);
	}

}
