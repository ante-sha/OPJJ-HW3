package hr.fer.zemris.java.gui.layouts;

/**
 * Iznimka koja označava da nije ispoštovano jedno od sljedećih pravila
 * {@link CalcLayout}-a:<br>
 * -moguće vrijednosti retka su [1,5], a stupca [1,7]<br>
 * -ako je odabran prvi redak stupac ne smije biti iz intervala [2,5]<br>
 * -na jednom mjestu se može nalaziti samo jedna komponenta
 * 
 * @author Ante Miličević
 *
 */
public class CalcLayoutException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 */
	public CalcLayoutException() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param message objašnjenje iznimke
	 */
	public CalcLayoutException(String message) {
		super(message);
	}

}
