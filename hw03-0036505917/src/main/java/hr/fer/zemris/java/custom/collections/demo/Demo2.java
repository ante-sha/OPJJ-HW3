/**
 * Razred s ciljem demonstracije rada razreda iz paketa hr.fer.zemris.java.custom.collections
 */
package hr.fer.zemris.java.custom.collections.demo;

import java.util.NoSuchElementException;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Razred s ciljem demonstracije ElementsGetter-a i poziva getNextElement metode nakon predavanja 
 * zadnjeg elementa.
 * --------------------------
 * Napomena:kod je izmjenjen u dvije linije zato što je zbog daljnjeg razvoja ElementsGetter-a dolazilo
 * do bacanja ConcurrentModificationException-a kada se pozivala metoda getNextElement() nakon poziva 
 * col.clear().
 * @author Ante Miličević
 *
 */
public class Demo2 {

	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		ElementsGetter getter = col.createElementsGetter();
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		try {
			System.out.println("Jedan element: " + getter.getNextElement());
			System.out.println("Neočekivano");
		} catch(NoSuchElementException e) {
			System.out.println("Očekivan odgovor");
		}
		
		
		Collection col2 = new LinkedListIndexedCollection();
		col2.add("Ivo");
		col2.add("Ana");
		ElementsGetter getter2 = col2.createElementsGetter();
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		try {
			System.out.println("Jedan element: " + getter2.getNextElement());
			System.out.println("Neočekivano");
		} catch(NoSuchElementException e) {
			System.out.println("Očekivan odgovor");
		}
	}
}
