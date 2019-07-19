package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Parametrizirani razred koji implementira sučelje {@link List}. Objekte čuva
 * kao elemente u dvostruko povezanoj listi. Dupliciranje elemenata je
 * omogućeno, dok spremanje elementa s vrijednosti {@code null} nije.
 * 
 * @author Ante Miličević
 * @param <T> tip elementa koji se sprema u listu
 *
 */
public class LinkedListIndexedCollection<T> implements List<T> {
	/** količina spremljenih ListNode elemenata u kolekciji */
	private int size = 0;
	/** referenca na prvi ListNode element koji je null ako je kolekcija prazna */
	private ListNode<T> first;
	/** referenca na zadnji ListNode element koji je null ako je kolekcija prazna */
	private ListNode<T> last;
	/** Broj koji LinkedElementsGetter-u služi praćenju promjena unutar liste */
	private long modificationCount = 0L;

	/** Konstruktor za stvaranje prazne kolekcije */
	public LinkedListIndexedCollection() {
		first = last = null;
	}

	/**
	 * Konstruktor za stvaranje kolekcije sa svim elementima kolekcije other. Ona
	 * mora biti barem tipa novo izgrađenog Collection-a.
	 * 
	 * @param other kolekcija čije elemente dodajemo u novu kolekciju
	 */
	public LinkedListIndexedCollection(Collection<? extends T> other) {
		addAll(other);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc} <br>
	 * Složenost metode je O(n) gdje je n broj elemenata.
	 */
	public boolean contains(Object value) {
		int index = indexOf(value);
		return index != -1 ? true : false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		Object[] rezultat = new Object[size];

		int i = 0;
		for (ListNode<T> tmp = first; tmp != null; tmp = tmp.right) {
			rezultat[i++] = tmp.value;
		}

		return rezultat;
	}

	/**
	 * {@inheritDoc} Element se dodaje na zadnju poziciju u složenosti O(1). Metoda
	 * ne dozvoljava unos {@code value} koji je null.
	 * 
	 * @throws NullPointerException ako je {@code value == null}
	 */
	public void add(T value) {
		Objects.requireNonNull(value);

		ListNode<T> novi = new ListNode<>(value, last, null);
		if (last == null) {
			first = novi;
		} else {
			last.right = novi;
		}
		last = novi;
		modificationCount++;
		size++;
	}

	/**
	 * Metoda koja vraća element kolekcije na index-tom mjestu. <br>
	 * Složenost metode je O(n) gdje je n broj elemenata u kolekciji.
	 * 
	 * @param index mjesto traženog elementa u kolekciji
	 * @return vrijednost elementa
	 */
	public T get(int index) {
		provjeraRaspona(index, 0, size - 1);

		ListNode<T> cvor = getListNodeSaPozicije(index);
		return cvor.value;
	}

	/**
	 * Pomočna metoda za dohvat index-tog čvora u listi. <br>
	 * Složenost operacije je O(n) gdje je n broj elemenata.
	 * 
	 * @param index index čvora za dohvat
	 * @return čvor s index-tog mjesta
	 */
	private ListNode<T> getListNodeSaPozicije(int index) {
		provjeraRaspona(index, 0, size - 1);

		int odmak;
		boolean početak;
		if (index <= size / 2) {
			odmak = index;
			početak = true;
		} else {
			odmak = size - 1 - index;
			početak = false;
		}
		ListNode<T> tmp = početak ? first : last;
		for (; odmak > 0; tmp = početak ? tmp.right : tmp.left, odmak--)
			;
		return tmp;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		first = last = null;
		size = 0;
		modificationCount++;
	}

	/**
	 * Metoda koja ubacuje element na poziciju {@code position} <br>
	 * Složenost metode je O(n) gdje je n broj elemenata u kolekciji.
	 * 
	 * @param value    vrijednost koju se ubacuje
	 * @param position pozicija na koju se ubacuje
	 * @throws IndexOutOfBoundsException ako je position izvan rapona [0,size()]
	 * @throws IllegalArgumentException  ako je value == null
	 */
	public void insert(T value, int position) {
		provjeraRaspona(position, 0, size);
		Objects.requireNonNull(value);

		if (position == 0 || position == size) {
			if (position == 0)
				first = new ListNode<>(value, null, first);
			else if (position == size)
				last = new ListNode<>(value, last, null);

			modificationCount++;
			size++;
			return;
		}
		ListNode<T> lijeviCvor = getListNodeSaPozicije(position - 1);

		ListNode<T> noviCvor = new ListNode<>(value, lijeviCvor, lijeviCvor.right.right);

		lijeviCvor.right = noviCvor;
		lijeviCvor.right.right.left = noviCvor;

		modificationCount++;
		size++;
	}

	/**
	 * Metoda koja vraća index elementa istog elementu value po uvjetima equals
	 * metode. <br>
	 * Složenost metode je O(n) gdje je n broj elemenata u kolekciji.
	 * 
	 * @param value element kojeg tražimo
	 * @return index elementa ili -1 ako on ne postoji
	 */
	public int indexOf(Object value) {
		if (value == null)
			return -1;

		int i = 0;
		for (ListNode<T> tmp = first; tmp != null; tmp = tmp.right, i++) {
			if (tmp.value.equals(value))
				return i;
		}
		return -1;
	}

	/**
	 * Metoda koja uklanja element s index-tog mjesta. <br>
	 * Složenost ove metode je O(n) gdje je n broj elemenata u kolekciji.
	 * 
	 * @param index mjesto s kojeg se ulanja element
	 * @throws IndexOutOfBoundsException ako je index izvan granica [0,size()-1]
	 */
	public void remove(int index) {
		provjeraRaspona(index, 0, size - 1);

		if (index == 0 || index == size - 1) {
			if (index == 0) {
				first = first.right;
			} else {
				last = last.left;
			}
			modificationCount++;
			return;
		}

		ListNode<T> lijeviCvor = getListNodeSaPozicije(index - 1);
		lijeviCvor.right = lijeviCvor.right.right;
		lijeviCvor.right.left = lijeviCvor;

		modificationCount++;
		size--;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object value) {
		if (value == null)
			return false;
		if (first == null)
			return false;
		if (first.value.equals(value)) {
			first = first.right;
			if (first == null)
				last = null;
			size--;
			modificationCount++;
			return true;
		}
		if (last.value.equals(value)) {
			size--;
			last = last.left;
			modificationCount++;
			return true;
		}

		for (ListNode<T> tmp = first; tmp.right != null; tmp = tmp.right) {
			if (tmp.right.value.equals(value)) {
				tmp.right = tmp.right.right;
				size--;
				modificationCount++;
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new LinkedElementsGetter();
	}

	/**
	 * Metoda za provjeru je li pozicija u dozvoljenom rasponu.
	 * 
	 * @param pozicija pozicija koju se ispituje
	 * @param pocetak  uključivi početak intervala
	 * @param kraj     uključivi kraj intervala
	 * @throws IndexOutOfBoundsException ako je pozicija izvan dozvoljenog intervala
	 */
	private void provjeraRaspona(int position, int pocetak, int kraj) {
		if (position < pocetak || position > kraj)
			throw new IndexOutOfBoundsException(String.format("%d nije u rasponu [0,%d]", position, pocetak, kraj));
	}

	/**
	 * Pomočni razred za ostvarenje LinkedListIndexedCollection. ListNode je jedan
	 * atom linked liste. {@code null} reference označavaju kraj liste na toj
	 * strani.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class ListNode<E> {
		/** vrijednost koju ListNode čuva */
		private E value;
		/** referenca na lijevog "susjeda" */
		private ListNode<E> left;
		/** referenca na desnog "susjeda" */
		private ListNode<E> right;

		/**
		 * Konstruktor za definiranje svih varijabli ListNode-a.
		 * 
		 * @param value vrijednost koju čuva objekt
		 * @param left  lijevi "susjed"
		 * @param right desni "susjed"
		 */
		public ListNode(E value, ListNode<E> left, ListNode<E> right) {
			this.value = value;
			this.left = left;
			this.right = right;
		}
	}

	/**
	 * Privatni razred LinkedListIndexedCollection koji omogućava iteriranje
	 * elementima kolekcije. Izmjene tokom korištenja nisu dozvoljene te se pri
	 * idućem pozivu nakon izmjene javlja ConcurrentModificationException.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private class LinkedElementsGetter implements ElementsGetter<T> {
		/** referenca na idući čvor u iteraciji */
		private ListNode<T> iduciCvor;
		/** modificationCount pri stvaranju objekta */
		private long savedModificationCount;

		/**
		 * Konstruktor koji prima kolekciju i njen trenutni modificationCount. Parametar
		 * modificationCount služi za provjeru ažuriranja kolekcije u intervalu između
		 * stvaranja i korištenja objekta.
		 * 
		 * @param kolekcija         kolekcija po kojoj se iterira
		 * @param modificationCount trenutni modificationCount kolekcije
		 */
		public LinkedElementsGetter() {
			iduciCvor = first;
			savedModificationCount = modificationCount;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se u intervalu između stvaranja i
		 *                                         korištenja dogodi izmjena
		 * @throws NoSuchElementException          ako dođe do poziva nakon zadnje
		 *                                         predanog elementa
		 */
		@Override
		public T getNextElement() {
			provjeriPromjenu();
			if (iduciCvor == null) {
				throw new NoSuchElementException("Nema više elemenata");
			}
			
			T rezultat = iduciCvor.value;
			iduciCvor = iduciCvor.right;
			return rezultat;
		}

		/**
		 * Pomoćna metoda za provjeravanje izmjene u kolekciji.
		 */
		private void provjeriPromjenu() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se u intervalu između stvaranja i
		 *                                         korištenja dogodi izmjena
		 */
		@Override
		public boolean hasNextElement() {
			provjeriPromjenu();
			return iduciCvor!=null;
		}
	}
}
