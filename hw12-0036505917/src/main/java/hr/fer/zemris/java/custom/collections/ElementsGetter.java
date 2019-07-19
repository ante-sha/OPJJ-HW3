/**
 * Paket koji sadrži razrede koji su
 * rezultat rješavanja prvi zadatak 3. domaće zadaće.
 */
package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Sučelje koje definira metode odgovorne za upravljanje iteriranjem elementima po kolekciji.
 * @author Ante Miličević
 *
 */
public interface ElementsGetter {
	/**
	 * Metoda koja vraća idući element iz iteracije kolekcijom.
	 * @return idući element kolekcije
	 * @throws NoSuchElementException ako dođe do poziva nakon zadnje predanog elementa
	 */
	Object getNextElement();
	
	/**
	 * Metoda koja ispituje da li je ostalo još elemenata u iteraciji.
	 * @return true ako je ostalo još elemenata u iteraciji
	 */
	boolean hasNextElement();
	
	/**
	 * Metoda koja obrađuje sve preostale elemente s process metodom {@link Processor}-a.
	 * @param p procesor koji obrađuje elemente
	 * @throws NullPointerException ako je p == null
	 */
	default void processRemaining(Processor p) {
		Objects.requireNonNull(p);
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
