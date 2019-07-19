package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Parametrizirano sučelje koje definira metode odgovorne za upravljanje
 * iteriranjem elementima po kolekciji.
 * 
 * @author Ante Miličević
 * @param <E> tip elementa koje vraća metoda {@link #getNextElement()}
 *
 */
public interface ElementsGetter<E> {
	/**
	 * Metoda koja vraća idući element iz iteracije kolekcijom.
	 * 
	 * @return idući element kolekcije
	 * @throws NoSuchElementException ako dođe do poziva nakon zadnje predanog
	 *                                elementa
	 */
	E getNextElement();

	/**
	 * Metoda koja ispituje da li je ostalo još elemenata u iteraciji.
	 * 
	 * @return true ako je ostalo još elemenata u iteraciji
	 */
	boolean hasNextElement();

	/**
	 * Metoda koja obrađuje sve preostale elemente s process metodom
	 * {@link Processor}-a. Processor mora minimalno "znati" obrađivati tipove koje
	 * predaje metoda {@link #getNextElement()}.
	 * 
	 * @param p procesor koji obrađuje elemente
	 * @throws NullPointerException ako je p == null
	 */
	default void processRemaining(Processor<? super E> p) {
		Objects.requireNonNull(p);
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
