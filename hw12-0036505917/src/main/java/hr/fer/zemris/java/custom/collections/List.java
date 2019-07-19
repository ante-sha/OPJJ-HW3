/**
 * Paket koji sadrži razrede koji su
 * rezultat rješavanja prvi zadatak 3. domaće zadaće.
 */
package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje opisuje rad razreda koji su liste koja sadrži elemente koji su indeksirani i preko
 * tih indeksa im je omogućeno pristupanje.
 */
public interface List extends Collection {
	/**
	 * Metoda koja vraća referencu na objekt koji se u listi nalazi na mjestu index.
	 * @param index mjesto objekta dozvoljene vrijednosti [0,size()-1]
	 * @return objekt s pozicije index
	 * @throws IndexOutOfBoundsException ako je {@code index} izvan dozvoljenih granica
	 */
	Object get(int index);
	/**
	 * Metoda koja omogućava umetanje elementa na poziciju index u listu ako je ona u 
	 * rasponu [0,size()].
	 * @param value objekt koji se ubacuje
	 * @param position pozicija na koju se ubacuje objekt
	 * @throws IndexOutOfBoundsException ako je {@code position} izvan dozvoljenih granica
	 */
	void insert(Object value, int position);
	/**
	 * Metoda koja vraća prvo pojavljivanje objekta koji je po metodi Objects.equals() jednak
	 * objektu {@code value}, a ako se {@code value} ne nalazi u kolekciji tada je povratna vrijednost -1.
	 * @param value objekt čiji se index traži
	 * @return index prvog pojavljivanja {@code value} ili -1 ako se on ne nalazi u kolekcji
	 */
	int indexOf(Object value);
	/**
	 * Metoda koja omogućava uklanjanje objekta iz kolekcije koji se nalazi na mjestu index.
	 * Dozvoljene vrijednosti su [0,size()-1]
	 * @param index mjesto objekta koji se uklanja
	 * @throws IndexOutOfBoundsException ako je {@code index} izvan dozvoljenih granica
	 */
	void remove(int index);
}
