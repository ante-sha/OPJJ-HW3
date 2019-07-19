package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji služi kao wraper za ArrayIndexedCollection tako da se on kroz
 * ovaj razred koristi kao stog. Null elementi nisu dozvoljeni, dupliciranje je
 * dozvoljeno.
 * 
 * @author Ante Miličević
 * @param <T> elementi koji se spremaju na stog
 *
 */
public class ObjectStack<T> {
	/** Spremnik podataka, "stog" */
	private ArrayIndexedCollection<T> spremnik = new ArrayIndexedCollection<>();

	/**
	 * Metoda koja provjerava je li stog prazan.
	 * 
	 * @return true ako je stog prazan <br>
	 *         false ako nije
	 */
	public boolean isEmpty() {
		return spremnik.isEmpty();
	}

	/**
	 * Metoda koja ispituje broj trenutnih elemenata na stogu.
	 * 
	 * @return broj trenutnih elemenata na stogu
	 */
	public int size() {
		return spremnik.size();
	}

	/**
	 * Metoda koja dodaje element na stog.
	 * 
	 * @param value element koji se dodaje
	 * @throws NullPointerException ako je {@code value==null}
	 */
	public void push(T value) {
		spremnik.add(value);
	}

	/**
	 * Metoda koja skida zadnji element sa stoga.
	 * 
	 * @return element s vrha stoga
	 * @throws EmptyStackException ako je stog prazan pri pozivu metode
	 */
	public T pop() {
		T rezultat = peek();
		spremnik.remove(spremnik.size() - 1);

		return rezultat;
	}

	/**
	 * Metoda koja vraća zadnji element sa stoga, ali ga ne skida.
	 * 
	 * @return element s vrha stoga
	 * @throws EmptyStackException ako je stog prazan pri pozivu metode
	 */
	public T peek() {
		if (isEmpty())
			throw new EmptyStackException("Stog je prazan!");
		return spremnik.get(spremnik.size() - 1);
	}

	/**
	 * Brisanje spremnika/stoga.
	 */
	public void clear() {
		spremnik.clear();
	}

}
