package hr.fer.zemris.java.hw05.db;

/**
 * Sučelje koje modelira operator usporedbe nad dva {@link String} objekta.
 * 
 * @author Ante Miličević
 *
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Metoda koja uspoređuje dva {@link String} objekta i vraća rezultat usporedbe
	 * 
	 * @param value1 prvi operand
	 * @param value2 drugi operand
	 * @return true ako je uvjet usporedbe zadovoljen, false inače
	 */
	public boolean satisfied(String value1, String value2);

}
