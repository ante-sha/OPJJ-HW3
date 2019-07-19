package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Razred koji modelira ponašanje mape. Zapise sprema u pomoćni razred
 * {@link TableEntry}. Svaki zapis sadrži 3 varijable od kojih su 2 ključ i
 * vrijednost. Ključevi unutar mape se ne mogu ponavljati i ne smiju biti
 * {@code null}. Zapisi se spremaju u Hash tablicu. Slot tablice se određuje
 * pomoću {@link #hashCode()} vrijednosti ključa. Preljevi se rješavaju
 * dodavanjem novog elementa na kraj ulančane liste. <br>
 * Kada se popunjenost mape poveća na 75% broja slotova tablica, broj slotova
 * tablice se udvostruči.
 * 
 * @author Ante Miličević
 *
 * @param <K> tip kjuča
 * @param <V> tip vrijednosti
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	/** Hash tablica koja sadrži pointere na ulančane liste elemenata */
	private TableEntry<K, V>[] tablica;
	/** Količina zapisa u tablici */
	private int size;
	/** Kontrolna vrijednost za {@link Iterator} */
	private long modificationCount = 0L;
	/** Defaultna velicina tablice */
	private static final int DEFAULT_VELICINA = 16;

	/**
	 * Defaultni konstruktor
	 */
	public SimpleHashtable() {
		this(DEFAULT_VELICINA);
	}

	/**
	 * Konstruktor koji kao argument prima veličinu tablice
	 * 
	 * @param capacity kapacitet tablice
	 * @throw IllegalArgumentException ako je {@code capacity} < 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException("Tablica ne može imati kapacitet manji od 1");
		int i;
		for (i = 1; i < capacity; i *= 2);

		tablica = (TableEntry<K, V>[]) new TableEntry[i];
	}

	/**
	 * Metoda koja unosi novi zapis u mapu. Ako se unutar mape već nalazi unos s
	 * ključem {@code key} tada se u njemu samo ažurira vrijednost koju sadrži na
	 * {@code value}
	 * 
	 * @param key   ključ
	 * @param value vrijednost
	 * @throws NullPointerException ako je key == null
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);
		provjeriKapacitetIVelicinu();

		int slot = generirajSlotIzKljuca(key);
		if (tablica[slot] == null) {
			tablica[slot] = new TableEntry<K, V>(key, value, null);
			changeSize(true);
			return;
		}
		TableEntry<K, V> entry = null;
		for (entry = tablica[slot]; entry.next != null && !entry.key.equals(key); entry = entry.next);

		if (entry.key.equals(key)) {
			entry.setValue(value);
		} else {
			entry.next = new TableEntry<K, V>(key, value, null);
			changeSize(true);
		}
	}

	/**
	 * Pomoćna metoda koja uz jediničnu promjenu veličine mijenja i kontrolni broj.
	 * 
	 * @param up boolean zastavica koja označava da li se veličina povečava
	 */
	private void changeSize(boolean up) {
		if (up) {
			size++;
		} else {
			size--;
		}
		modificationCount++;
	}

	/**
	 * Metoda koja dohvaća vrijednost {@link TableEntry}-a koji u sebi sadrži ključ
	 * koji je jednak {@code key}.
	 * 
	 * @param key ključ po kojem tražimo unos
	 * @return vrijednost koja je uparena sa ključem ili null ako ključ ne postoji u
	 *         mapi
	 */
	public V get(K key) {
		if (key == null) {
			return null;
		}

		int slot = generirajSlotIzKljuca(key);
		for (TableEntry<K, V> entry = tablica[slot]; entry != null; entry = entry.next) {
			if (entry.key.equals(key)) {
				return entry.value;
			}
		}
		return null;
	}

	/**
	 * Metoda koja briše {@link TableEntry} iz mape koji ima ključ jednak
	 * {@code key}. Ako se u metodi ne nalazi takav ključ ne događa se ništa.
	 * 
	 * @param key ključ unosa kojeg se briše
	 */
	public void remove(Object key) {
		if (key == null) {
			return;
		}

		int slot = generirajSlotIzKljuca(key);
		if (key.equals(tablica[slot].key)) {
			tablica[slot] = tablica[slot].next;
			changeSize(false);
			return;
		}

		TableEntry<K, V> entry = null;
		for (entry = tablica[slot]; entry.next != null && !entry.next.key.equals(key); entry = entry.next);

		if (entry.next == null) {
			return;
		}

		entry.next = entry.next.next;
		changeSize(false);
	}

	/**
	 * Metoda koja vraća broj unosa u mapi
	 * 
	 * @return broj unosa u mapi
	 */
	public int size() {
		return size;
	}

	/**
	 * Metoda koja provjerava da li se u mapi nalazi unos s vrijednošću ključa
	 * {@code key}.
	 * 
	 * @param key ključ preko kojeg tražimo unos
	 * @return true - ako se nalazi u mapi<br>
	 *         false - ako ne
	 */
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}

		int slot = generirajSlotIzKljuca(key);
		for (TableEntry<K, V> entry = tablica[slot]; entry != null; entry = entry.next) {
			if (entry.key.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Metoda koja provjerava da li se u mapi nalazi unos s vrijednošću
	 * {@code value}.
	 * 
	 * @param value vrijednost koju se traži u unosima
	 * @return true - ako postoji<br>
	 *         false - ako je postoji
	 */
	public boolean containsValue(Object value) {
		for (int i = 0; i < tablica.length; i++) {
			for (TableEntry<K, V> entry = tablica[i]; entry != null; entry = entry.next) {
				if (Objects.equals(entry.value, value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Pomoćna metoda koja iz ključa vraća vrijednost slota u kojem se njegov unos
	 * treba nalaziti. Ako je hash ključa Integer.MIN_VALUE vraća se 0
	 * 
	 * @param kljuc kljuc za kojeg se traži slot
	 * @return vrijednost slota
	 */
	private int generirajSlotIzKljuca(Object kljuc) {
		int slot = Math.abs(kljuc.hashCode()) % tablica.length;
		if (slot < 0) {
			slot = 0;
		}
		return slot;
	}

	/**
	 * Metoda skuplja stringove unosa mape počevši od 0-tog slota pa do zadnjeg
	 * prolazeći kroz sve ulančane liste. {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(100);
		builder.append("[");
		for (int i = 0; i < tablica.length; i++) {
			for (TableEntry<K, V> entry = tablica[i]; entry != null; entry = entry.next) {
				if (builder.length() > 1) {
					builder.append(", ");
				}
				builder.append(entry.toString());
			}
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Metoda koja briše sve unose iz mape
	 */
	public void clear() {
		for (int i = 0; i < tablica.length; i++) {
			tablica[i] = null;
		}
		size = 0;
		modificationCount++;
	}

	/**
	 * Metoda koja provjerava odnos količine upisa i kapaciteta tablice i ako je on
	 * >= 0.75 tada se tablica povećava na dvostruku veličinu i ponovno se u nju
	 * zapisuju svi stari elementi.
	 */
	@SuppressWarnings("unchecked")
	public void provjeriKapacitetIVelicinu() {
		if (!(size >= 0.75 * tablica.length)) {
			return;
		}

		TableEntry<K, V>[] container = staviSveUnoseUPolje();

		tablica = (TableEntry<K, V>[]) new TableEntry[tablica.length * 2];

		postaviSveUnoseNaMjesto(container);
	}

	/**
	 * Pomoćna metoda koja postavlja sve unose iz {@code spremnik}-a u tablicu.
	 * 
	 * @param spremnik polje unosa
	 */
	private void postaviSveUnoseNaMjesto(TableEntry<K, V>[] spremnik) {
		for (int i = 0, n = spremnik.length; i < n; i++) {
			spremnik[i].next = null;
			int slot = generirajSlotIzKljuca(spremnik[i].key);
			if (tablica[slot] == null) {
				tablica[slot] = spremnik[i];
				continue;
			}
			TableEntry<K, V> entry = null;
			for (entry = tablica[slot]; entry.next != null; entry = entry.next);
			
			entry.next = spremnik[i];
		}
	}

	/**
	 * Metoda koja kopira sve unose u polje i vraća ga kao rezultat operacije.
	 * 
	 * @return polje svih unosa mape
	 */
	@SuppressWarnings("unchecked")
	private TableEntry<K, V>[] staviSveUnoseUPolje() {
		TableEntry<K, V>[] spremnik = (TableEntry<K, V>[]) new TableEntry[size];
		int index = 0;
		for (int i = 0; i < tablica.length; i++) {
			for (TableEntry<K, V> entry = tablica[i]; entry != null; entry = entry.next) {
				spremnik[index++] = entry;
			}
		}
		return spremnik;
	}

	/**
	 * Pomoćni razred koji modelira jedan unos ključa i vrijednosti u
	 * {@link SimpleHashtable} mapi. Ključ ne smije biti null.
	 * 
	 * @author Ante Miličević
	 *
	 * @param <K> tip ključa
	 * @param <V> tip vrijednosti
	 */
	public static class TableEntry<K, V> {
		/** Ključ unosa */
		private K key;
		/** Vrijednost unosa */
		private V value;
		/** Referenca na idući unos u listi */
		private TableEntry<K, V> next;

		/**
		 * Konstruktor koji definira sve članske varijable
		 * 
		 * @param key   ključ
		 * @param value vrijednost
		 * @param next  referenca na idući unos u listi
		 */
		private TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Getter za ključ
		 * 
		 * @return key
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Getter za vrijednost
		 * 
		 * @return value
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Setter za vrijednost
		 * 
		 * @param value vrijednost
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return String.format("%s=%s", key, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Privatni razred koji modelira {@link Iterator} za {@link SimpleHashtable}. Uz
	 * obvezne metode iz sučelja Iterator razred implementira i metodu remove() koja
	 * uklanja elemente pri iteriranju.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		/** Slot u kojem se iterator trenutno nalazi */
		private int slot;
		/** Idući element koji se treba predati */
		private TableEntry<K, V> iduci;
		/** Zadnji predani element */
		private TableEntry<K, V> zadnji;
		/**
		 * Kontrolni broj koji se radi konzistentnosti s mapom provjerava s
		 * modificationCount
		 */
		private long savedModificationCount;

		/**
		 * Defaultni konstruktor
		 */
		public IteratorImpl() {
			slot = -1;
			iduci = null;
			savedModificationCount = modificationCount;
			prepareNext();
		}

		/**
		 * Pomoćna metoda koja dohvaća element kojeg će predati pri idućem pozivu
		 * {@link #next()}.
		 */
		private void prepareNext() {
			if (iduci != null && iduci.next != null) {
				iduci = iduci.next;
			} else {
				iduci = null;
				for (slot++; slot < tablica.length; slot++) {
					if (tablica[slot] != null) {
						iduci = tablica[slot];
						break;
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			provjeriPromjene();
			if (iduci == null) {
				return false;
			} else {
				return true;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public TableEntry<K, V> next() {
			provjeriPromjene();
			if (iduci == null) {
				throw new NoSuchElementException("There is no elements left!");
			}

			zadnji = iduci;
			prepareNext();
			return zadnji;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			provjeriPromjene();

			if (zadnji == null) {
				throw new IllegalStateException("Last element has already been removed!");
			}

			SimpleHashtable.this.remove(zadnji.key);

			zadnji = null;

			savedModificationCount = modificationCount;
		}

		/**
		 * Pomoćna metoda koja provjerava da li je bilo nepoznatih promjena u intervalu
		 * između ovog i zadnjeg korištenja.
		 * 
		 * @throws ConcurrentModificationException ako je došlo mijenjanja mape izvan
		 *                                         iteratora
		 */
		private void provjeriPromjene() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException(
						"Hashtable has changed in the period between of creating iterator and use of it!");
			}
		}

	}
}
