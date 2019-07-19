package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Razred koji predstavlja adapter za {@link ArrayIndexedCollection}. Adaptira
 * ga se na način da se kroz ovaj razred s njim može pričati kao mapom.
 * Jedinični zapis mape se sastoji od ključa i vrijednosti. Ključ mora biti
 * jedinstven i ne smije biti null, dok vrijednost ne mora biti jedinstvena i
 * smije biti null.
 * 
 * @author Ante Miličević
 *
 * @param <K> tip ključa
 * @param <V> tip vrijednosti
 */
public class Dictionary<K, V> {

	/** Kolekcija u kojoj čuvamo sve zapise */
	private ArrayIndexedCollection<Entry<K, V>> kolekcija = new ArrayIndexedCollection<>();

	/**
	 * Metoda koja provjerava da li {@link DictionaryTest} sadrži upisa.
	 * 
	 * @return true - ako ne sadrži<br>
	 *         false - ako sadrži
	 */
	public boolean isEmpty() {
		return kolekcija.size() == 0;
	}

	/**
	 * Metoda koja vraća broj zapisa.
	 * 
	 * @return broj zapisa
	 */
	public int size() {
		return kolekcija.size();
	}

	/**
	 * Metoda koja briše sve zapise
	 */
	public void clear() {
		kolekcija.clear();
	}

	/**
	 * Metoda koja unosi novi zapis u mapu ako se trenutno u mapi već ne nalazi unos
	 * s ključem {@code key}. Ako se u mapi nalazi takav ključ tada se stara
	 * vrijednost samo pregazi.<br>
	 * Ključ ne smije biti {@code null}.
	 * 
	 * @param key   ključ novog zapisa
	 * @param value vrijednost novog zapisa
	 * 
	 * @throws NullPointerException ako je key == null
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);

		Entry<K, V> oldEntry = vratiUnosPrekoKljuca(key);
		if (oldEntry == null) {
			Entry<K, V> entry = new Entry<>(key, value);
			kolekcija.add(entry);
		} else {
			oldEntry.setValue(value);
		}
	}

	/**
	 * Metoda koja dohvaća vrijednost koja je uparena s ključem {@code key}. Ako
	 * takav ključ ne postoji metoda vraća {@code null}.
	 * 
	 * @param key ključ pomoću kojeg se traži vrijednost
	 * @return vrijednost uparena s ključem ili {@code null} ako ključ ne postoji
	 */
	public V get(Object key) {
		Entry<K, V> entry = vratiUnosPrekoKljuca(key);

		if (entry == null) {
			return null;
		}

		return entry.vrijednost;
	}

	/**
	 * Privatna metoda koja pomoću ključa vraća {@link Entry} s istim ključem. Ako u
	 * mapi ne postoji takav ključ vraća se null;
	 * 
	 * @param key ključ za kojeg tražimo unos
	 * @return {@link Entry} koji ima key za vrijednost ključa ili null ako je
	 *         postoji takav
	 */
	private Entry<K, V> vratiUnosPrekoKljuca(Object key) {
		if (key == null) {
			return null;
		}

		ElementsGetter<Entry<K, V>> getter = kolekcija.createElementsGetter();

		while (getter.hasNextElement()) {
			Entry<K, V> entry = getter.getNextElement();
			if (key.equals(entry.kljuc)) {
				return entry;
			}
		}

		return null;
	}

	/**
	 * Pomoćni razred koji modelira jedan unos u mapi. Sadrži ključ i vrijednost
	 * gdje ključ ne može biti {@code null}.
	 * 
	 * @author Ante Miličević
	 *
	 * @param <K> tip ključa
	 * @param <V> tip vrijednosti
	 */
	private static class Entry<K, V> {
		/** Kljuc unosa */
		private K kljuc;
		/** Vrijednost unosa */
		private V vrijednost;

		/**
		 * Konstruktor koji definira sve varijable unosa
		 * 
		 * @param key   kljuc
		 * @param value vrijednost
		 * 
		 * @throws NullPointerException ako je key == null
		 */
		public Entry(K key, V value) {
			this.kljuc = Objects.requireNonNull(key);
			this.vrijednost = value;
		}

		/**
		 * Setter za vrijednost koji nakon postavljanja nove vrijednosti vraća staru kao
		 * rezultat operacije.
		 * 
		 * @param value nova vrijednost
		 * @return stara vrijednost
		 */
		public V setValue(V value) {
			V stara = this.vrijednost;
			this.vrijednost = value;
			return stara;
		}
	}
}
