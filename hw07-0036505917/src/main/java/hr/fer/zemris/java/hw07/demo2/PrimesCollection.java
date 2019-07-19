package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Razred PrimesCollection predstavlja tok prostih brojeva. Količina prostih
 * brojeva se zadaje u konstruktoru.
 * 
 * @author Ante Miličević
 *
 */
public class PrimesCollection implements Iterable<Integer> {

	/**
	 * Količina prostih brojeva u svakoj iteraciji
	 */
	private final int count;

	/**
	 * Konstruktor koji prima količinu prostih brojeva u jednoj iteraciji
	 * 
	 * @param count količina prostih brojeva u jednoj iteraciji
	 * 
	 * @throws IllegalArgumentException ako je {@code count} manji od 0
	 */
	public PrimesCollection(int count) {
		if (count < 0) {
			throw new IllegalArgumentException("Can not iterate negative number of times " + count);
		}
		this.count = count;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator(count);
	}

	/**
	 * Implementacija iteratora kroz proste brojeve. Količina brojeva u iteraciji se
	 * određuje se u konstruktoru.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class PrimeIterator implements Iterator<Integer> {
		/**
		 * Brojač preostale količine brojeva u iteriranju
		 */
		private int counter;
		/**
		 * Zadnji predani prosti broj
		 */
		private int last = 1;

		/**
		 * Konstruktor u kojem se zadaje količina brojeva u iteriranju
		 * 
		 * @param counter količina brojeva u iteriranju
		 */
		public PrimeIterator(int counter) {
			this.counter = counter;
		}

		@Override
		public boolean hasNext() {
			return counter > 0;
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There is no elements left");
			}

			for (last++;; last++) {
				if (isPrime(last)) {
					break;
				}
			}

			counter--;
			return Integer.valueOf(last);
		}

		/**
		 * Metoda koja provjerava je li broj prost
		 * 
		 * @param value broj koji se provjerava
		 * @return true ako je, false inače
		 */
		private static boolean isPrime(int value) {
			if (value > 3 && value % 3 == 0)
				return false;
			if (value > 2 && value % 2 == 0)
				return false;

			// step is 2 because of second if statement
			for (int i = 5; i <= (int) Math.floor(Math.sqrt(value)); i += 2) {
				if (value % i == 0) {
					return false;
				}
			}
			return true;
		}

	}
}
