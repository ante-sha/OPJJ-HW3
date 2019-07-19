/**
 * Razred s ciljem demonstracije rada razreda iz paketa hr.fer.zemris.java.custom.collections
 */
package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.List;

/**
 * Demonstracija rada kolekcije i liste koja uz to ukazuje na smjer nasljeđivanja metode implementiranih
 * sučelja koja se nasljeđuju.
 * @author Ante Miličević
 *
 */
public class DemoList {
	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		List col1 = new ArrayIndexedCollection();
		List col2 = new LinkedListIndexedCollection();
		col1.add("Ivana");
		col2.add("Jasna");
		Collection col3 = col1;
		Collection col4 = col2;
		col1.get(0);
		col2.get(0);
//		col3.get(0); // neće se prevesti! Razumijete li zašto?
//		col4.get(0); // neće se prevesti! Razumijete li zašto?  Kolekcija nema metodu get
		col1.forEach(System.out::println); // Ivana
		col2.forEach(System.out::println); // Jasna
		col3.forEach(System.out::println); // Ivana
		col4.forEach(System.out::println); // Jasna
	}
}
