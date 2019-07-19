/**
 * Razred s ciljem demonstracije rada razreda iz paketa hr.fer.zemris.java.custom.collections
 */
package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Razred koji demonstrira rad sa ElementsGetter-om
 * @author Ante Miličević
 *
 */
public class Demo1 {

	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		Collection col1 = new ArrayIndexedCollection();
		Collection col2 = new ArrayIndexedCollection();
		col1.add("Ivo");
		col1.add("Ana");
		col1.add("Jasna");
		col2.add("Jasmina");
		col2.add("Štefanija");
		col2.add("Karmela");
		ElementsGetter getter1 = col1.createElementsGetter();
		ElementsGetter getter2 = col1.createElementsGetter();
		ElementsGetter getter3 = col2.createElementsGetter();
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		
		System.out.println();
		
		Collection col3 = new LinkedListIndexedCollection();
		Collection col4 = new LinkedListIndexedCollection();
		col3.add("Ivo");
		col3.add("Ana");
		col3.add("Jasna");
		col4.add("Jasmina");
		col4.add("Štefanija");
		col4.add("Karmela");
		ElementsGetter getter4 = col3.createElementsGetter();
		ElementsGetter getter5 = col3.createElementsGetter();
		ElementsGetter getter6 = col4.createElementsGetter();
		System.out.println("Jedan element: " + getter4.getNextElement());
		System.out.println("Jedan element: " + getter4.getNextElement());
		System.out.println("Jedan element: " + getter5.getNextElement());
		System.out.println("Jedan element: " + getter6.getNextElement());
		System.out.println("Jedan element: " + getter6.getNextElement());
	}

}
