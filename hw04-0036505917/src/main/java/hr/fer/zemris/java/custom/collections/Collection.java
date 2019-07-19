package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Parametrizirano sučelje koji služi za generičku pohranu objekata.
 * 
 * @author Ante Miličević
 * @param <E> tip elemenata koje sadrži kolekcija
 *
 */
public interface Collection<E> {

	/**
	 * Metoda koja provjerava je li kolekcija prazna.
	 * 
	 * @return true ako je kolekcija prazna
	 */
	default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Metoda koja vraća broj trenutnih objekata u kolekciji.
	 * 
	 * @return broj trenutnih objekata u kolekciji
	 */
	int size();

	/**
	 * Metoda koja dodaje objekt u kolekciju.
	 * 
	 * @param value objekt koji se dodaje
	 */
	void add(E value);

	/**
	 * Metoda koja provjerava sadrži li kolekcija objekt.
	 * 
	 * @param value objekt kojeg se traži u kolekciji
	 * @return true ako se objekt nalazi u kolekciji
	 */
	boolean contains(Object value);

	/**
	 * Metoda koja uklanja objekt iz kolekcije ako se on nalazi u njoj.
	 * 
	 * @param value objekt kojeg se uklanja
	 * @return
	 *         <p>
	 *         true - ako se objekt nalazio u kolekciji i sada je uklonjen
	 *         </p>
	 *         <p>
	 *         false - ako se objekt nije nalazio u kolekciji i ništa nije uklonjeno
	 *         </p>
	 */
	boolean remove(Object value);

	/**
	 * Metoda koja zauzima polje objekata iste veličine kao broj elemenata u
	 * kolekciji te ga puni objektima i vraća to polje.
	 * 
	 * @return polje svih elemenata kolekcije
	 */
	Object[] toArray();

	/**
	 * Metoda kojom se obrađuje svaki element kolekcije metodom
	 * processor.process(element) i pri tome ostavlja kolekciju ne promijenjenu.
	 * Processor mora "znati" obrađivati elemente koji su minimalno tipa
	 * Collection-a. Redoslijed slanja u ovu metodu je nedefiniran.
	 * 
	 * @param processor objekt pomoću kojega se obrađuju elementi
	 */
	default void forEach(Processor<? super E> processor) {
		Objects.requireNonNull(processor);

		ElementsGetter<E> getter = createElementsGetter();

		while (getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}

	/**
	 * Metoda koja dodaje sve elemente kolekcije other u kolekciju i pri tome
	 * ostavlja kolekciju other ne promijenjenu. {@link Collection} {@code other} je
	 * barem tipa ovog Collection-a.
	 * 
	 * @param other
	 * @throws NullPointerException ako je other == null
	 */
	default void addAll(Collection<? extends E> other) {
		Objects.requireNonNull(other);

		Processor<E> dodavac = (objekt) -> this.add(objekt);

		other.forEach(dodavac);
	}

	/**
	 * Metoda koja briše sve elemente kolekcije.
	 */
	void clear();

	/**
	 * Metoda koja vraća {@link ElementsGetter}. Pomoću njega omogućen je prolazak
	 * svim elementima redom u najboljoj složenosti za tu kolekciju.
	 * 
	 * @return ElementsGetter kolekcije
	 */
	ElementsGetter<E> createElementsGetter();

	/**
	 * Metoda koja dodaje sve elemente u kolekciju iz kolekcije {@code col} koji
	 * zadovoljavaju uvjete {@code tester}-ove metode tester.test().
	 * {@link Collection} {@code col} mora biti barem tipa kao i ovaj Collection dok
	 * tester mora "znati" obrađivati elemente koji su minimalno tipa kojeg sadrži
	 * kolekcija {@code other}.
	 * 
	 * @param col    kolekcija ciji se elementi dodaju
	 * @param tester objekt koji metodom test() odlučuje prihvaća li se element
	 *               kolekcije
	 */
	default <T extends E> void addAllSatisfying(Collection<T> col, Tester<? super T> tester) {
		Objects.requireNonNull(col);
		Objects.requireNonNull(tester);
		ElementsGetter<T> getter = col.createElementsGetter();

		while (getter.hasNextElement()) {
			T element = getter.getNextElement();
			if (tester.test(element))
				add(element);
		}
	}
}
