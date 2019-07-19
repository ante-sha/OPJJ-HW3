/**
 * Paket koji sadrži razrede koji su
 * rezultat rješavanja prvi zadatak 3. domaće zadaće.
 */
package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koji nam služi za generičku obradu razreda {@link Collection}.
 * @author Ante Miličević
 *
 */
public interface Processor {
	/**
	 * Default metoda koju nikad nećemo koristiti direktno iz razreda Processor
	 * nego iz njegovih izvedenih razreda.
	 * @param value vrijednost koju obrađujemo.
	 */
	void process(Object value);
}
