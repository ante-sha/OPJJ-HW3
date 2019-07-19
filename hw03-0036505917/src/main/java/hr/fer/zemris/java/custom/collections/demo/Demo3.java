/**
 * Razred s ciljem demonstracije rada razreda iz paketa hr.fer.zemris.java.custom.collections
 */
package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Demonstracija processRemainig metode sučelja ElementsGetter
 * @author Ante Miličević
 *
 */
public class Demo3 {
	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		Collection col = new LinkedListIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter getter = col.createElementsGetter();
		getter.getNextElement();
		getter.processRemaining(System.out::println);
	}
}
