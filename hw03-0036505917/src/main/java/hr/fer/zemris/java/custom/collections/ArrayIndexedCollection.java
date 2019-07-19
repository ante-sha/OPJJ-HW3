/**
 * Paket koji sadrži razrede koji su
 * rezultat rješavanja prvi zadatak 3. domaće zadaće.
 */
package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Razred koji implementira sučelje Collection.
 * Objekte čuva polju promjenjive veličine. Dupliciranje elemenata je omogućeno, dok spremanje
 * elementa s vrijednosti {@code null} nije.
 * @author Ante Miličević
 *
 */
public class ArrayIndexedCollection implements List{
	/**Varijabla koja čuva veličinu kolekcije. Po defaultu jednaka nuli.*/
	private int size;
	/**Elementi kolekcije*/
	private Object[] elements;
	/**Defaultna velicina spremnika. */
	public static final int DEFAULT_VELICINA = 16;
	/**Broj koji ArrayElementsGetter-u služi praćenju promjena unutar kolekcije*/ 
	private long modificationCount = 0L;
	
	/**
	 * Defaultni konstruktor koji stvara ArrayIndexedCollection sa spremnikom veličine DEFAULT_VELICINA.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_VELICINA);
	}
	/**
	 * Konstruktor koji stvara ArrayIndexedCollection sa spremnikom veličine argumenta.
	 * @param initialCapacity početna veličina spremnika
	 * @throws IllegalArgumentException ako je argument manji od 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		elementsInicijalizacija(initialCapacity);
	}
	/**
	 * Konstuktor koji stvara ArrayIndexedCollection sa spremnikom veličine 16 ili s veličinom popunjenosti
	 * kolekcije other, ovisno o tome koja je veličina veća. Nakon stvaranja spremnika u kolekciju
	 * se dodaju svi elementi iz kolekcije other.
	 * @param other kolekcija koja se kopira u novostvorenu
	 * @throws NullPointerException ako je other = null
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other,16);
	}
	/**
	 * Konstuktor koji stvara ArrayIndexedCollection sa spremnikom veličine initialCapacity 
	 * ili s veličinom popunjenostikolekcije other, ovisno o tome koja je veličina veća. 
	 * Nakon stvaranja spremnika u kolekciju se dodaju svi elementi iz kolekcije other.
	 * @param other kolekcija koja se kopira u novostvorenu
	 * @throws NullPointerException ako je other = null
	 * @throws IllegalArgumentException ako je other.size() && initialCapacity == 0
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		Objects.requireNonNull(other);
		
		initialCapacity = other.size() > initialCapacity ? other.size() : initialCapacity;
		elementsInicijalizacija(initialCapacity);
		
		addAll(other);
	}
	
	/**
	 * Pomoćna metoda za inicijalizaciju spremnika.
	 * 
	 * @param initialCapacity početna veličina spremnika
	 * @throws IllegalArgumentException ako je initialCapacity < 1
	 */
	private void elementsInicijalizacija(int initialCapacity) {
		if(initialCapacity < 1) throw new IllegalArgumentException();
		
		elements = new Object[initialCapacity];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean contains(Object value) {
		int index = indexOf(value);
		return index != -1 ? true : false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	/**
	 * {@inheritDoc}
	 * Ako je spremnik pun, a treba se dodati element tada se spremnik realocira na duplu veličinu.
	 * <br>Složenost dodavanja je O(1).
	 * @throws NullPointerException ako je value == null
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);
		modificationCount++;
		
		if(size >= elements.length)
			elements = Arrays.copyOf(elements, elements.length*2);
		
		elements[size++] = value;
	}
	
	/**
	 * Metoda koja vraća element koji se nalati na index-tom mjestu u spremniku.
	 * <br>Složenost operacije je O(1).
	 * @param index index mjesta s kojeg povlačimo element
	 * @return element na mjestu index
	 * @throws IndexOutOfBoundsException ako je index izvan raspona [0,size()-1]
	 */
	public Object get(int index) {
		provjeraRaspona(index,0,size-1);
		return elements[index];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		modificationCount++;
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}
	
	/**
	 * Metoda koja ubacuje element u spremnik. Ako u spremniku više ne postoji mjesta spremnik
	 * se povećava na duplu veličinu. Složenost metode je O(n) gdje je n količina podataka u spremniku.
	 * @param value element koji se ubacuje
	 * @param position pozicija na koju se ubacuje {@code value}
	 * @throws IndexOutOfBoundsException ako {@code position} nije u rasponu [0,size()]
	 * @throws NullPointerException ako je {@code value} null referenca
	 */
	public void insert(Object value, int position) {
		provjeraRaspona(position,0,size);
		Objects.requireNonNull(value);
		
		modificationCount++;
		
		if(size >= elements.length)
			elements = Arrays.copyOf(elements, elements.length);
		
		for(int i = size; i > position;i--) {
			elements[i]=elements[i-1];
		}
		elements[position] = value;
		size++;
	}
	/**
	 * Metoda koja vraća index prvog pojavljivanja elementa u spremniku, po metodi equals, 
	 * istog elementu {@code value}.
	 * Ako se element ne nalazi u spremniku povratna vrijednost je -1.
	 * <br>Složenost ove metode je O(n) gdje je n količina podataka u spremniku.
	 * @param value element kojeg se traži u spremniku
	 * @return index prvog pojavljivanja ili -1 ako on ne postoji u spremniku
	 */
	public int indexOf(Object value) {
		if(value == null)
			return -1;
		
		for(int i = 0; i < size;i++) {
			if(elements[i].equals(value))
				return i;
		}
		return -1;
	}
	
	/**
	 * Metoda koja uklanja element s pozicije {@code index} i ostatak spremnika pomiče za
	 * jedno mjesto u lijevo kako bi spremnik ostao cjelovit. 
	 * <br>Složenost operacije je O(n) gdje je n količina podataka u spremniku.
	 * @param index pozicija s koje želimo ukloniti element
	 * @throws IndexOutOfBoundsException ako index nije u rasponu [0,size()-1]
	 */
	public void remove(int index) {
		provjeraRaspona(index,0,size-1);
		
		modificationCount++;
		
		for(int i = index;i<size;i++) {
			elements[i] = elements[i+1];
		}
		size--;
	}
	
	/**
	 * {@inheritDoc}
	 * <br>Složenost operacije je O(n) gdje je n količina podataka u spremniku.
	 */
	public boolean remove(Object value) {
		if(value == null)
			return false;
		
		int index = indexOf(value);
		
		if(index == -1) 
			return false;
		
		modificationCount++;
		remove(index);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new ArrayElementsGetter(this,modificationCount);
	}
	
	/**
	 * Privatni razred ArrayIndexedCollection koji omogućava iteriranje elementima kolekcije.
	 * Izmjene tokom korištenja nisu dozvoljene te se pri idućem pozivu nakon izmjene javlja
	 * ConcurrentModificationException.
	 * @author Ante Miličević
	 *
	 */
	private static class ArrayElementsGetter implements ElementsGetter{
		/**index iduceg elementa*/
		private int iduciIndex=0;
		/**zastavica koja označava prozvanost hasNext metode*/
		private boolean bilaProvjeraHasNext=false;
		/**zastavica u kojoj se čuva vrijednost pozvane hasNext 
		 * metode u intervalu između dva izbacivanja elementa*/
		private boolean imaJos;
		/**kolekcija po kojoj se iterira*/
		private ArrayIndexedCollection kolekcija;
		/**modificationCount pri stvaranju objekta*/
		private long savedModificationCount;
		
		/**
		 * Konstruktor koji prima kolekciju i njen trenutni modificationCount.
		 * Parametar modificationCount služi za provjeru ažuriranja kolekcije u intervalu između
		 * stvaranja i korištenja objekta.
		 * @param kolekcija kolekcija po kojoj se iterira
		 * @param modificationCount trenutni modificationCount kolekcije
		 */
		public ArrayElementsGetter(ArrayIndexedCollection kolekcija,long modificationCount) {
			this.kolekcija = kolekcija;
			savedModificationCount = modificationCount;
		}
		
		/**
		 * {@inheritDoc}
		 * @throws ConcurrentModificationException ako se u intervalu između 
		 * stvaranja i korištenja dogodi izmjena
		 * @throws NoSuchElementException ako dođe do poziva nakon zadnje predanog elementa
		 */
		@Override
		public Object getNextElement() {
			provjeriPromjenu();
			if(!bilaProvjeraHasNext)
				hasNextElement();
			if(!imaJos)
				throw new NoSuchElementException("Nema više elemenata!");
			bilaProvjeraHasNext = false;
			return this.kolekcija.elements[iduciIndex++];
		}

		/**
		 * Pomoćna metoda za provjeravanje izmjene u kolekciji.
		 */
		private void provjeriPromjenu() {
			if(savedModificationCount != kolekcija.modificationCount)
				throw new ConcurrentModificationException();
		}

		/**
		 * {@inheritDoc}
		 * @throws ConcurrentModificationException ako se u intervalu između 
		 * stvaranja i korištenja dogodi izmjena
		 */
		@Override
		public boolean hasNextElement() {
			provjeriPromjenu();
			if(!bilaProvjeraHasNext) {
				imaJos = this.kolekcija.size > iduciIndex;
				bilaProvjeraHasNext = true;
			}
			
			return imaJos;
		}
		
	}
	
	/**
	 * Metoda za provjeru je li pozicija u dozvoljenom rasponu.
	 * @param pozicija pozicija koju se ispituje
	 * @param pocetak uključivi početak intervala
	 * @param kraj uključivi kraj intervala
	 * @throws IndexOutOfBoundsException ako je pozicija izvan dozvoljenog intervala
	 */
	private void provjeraRaspona(int pozicija, int pocetak, int kraj) {
		if(pozicija < pocetak || pozicija > kraj)
			throw new IndexOutOfBoundsException(
					String.format("%d nije u rasponu [%d,%d]",pozicija,pocetak,kraj)
					);
	}
}
