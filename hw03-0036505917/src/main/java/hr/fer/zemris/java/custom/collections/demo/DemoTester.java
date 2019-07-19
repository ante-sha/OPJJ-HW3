/**
 * Razred s ciljem demonstracije rada razreda iz paketa hr.fer.zemris.java.custom.collections
 */
package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Demonstracija rada razreda koji implementiraju sučelje Tester.
 * @author Ante Miličević
 *
 */
public class DemoTester {
	
	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		
			Tester t = new EvenIntegerTester();
			System.out.println(t.test("Ivo"));
			System.out.println(t.test(22));
			System.out.println(t.test(3));
	}
	
	/**
	 * Primjer razreda koji implementira sučelje Tester u svrhe demonstracije.
	 * @author Ante Miličević
	 *
	 */
	public static class EvenIntegerTester implements Tester {
		/**
		 * @return true-ako je argument parni cijeli broj
		 * <br>inače
		 */
		 public boolean test(Object obj) {
		 if(!(obj instanceof Integer)) return false;
		 Integer i = (Integer)obj;
		 return i % 2 == 0;
		 }
	}
	
}
