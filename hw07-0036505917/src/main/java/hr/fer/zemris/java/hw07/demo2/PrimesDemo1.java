package hr.fer.zemris.java.hw07.demo2;

/**
 * Demonstracija rada razreda PrimesCollection
 * 
 * @author Ante Miličević
 *
 */
public class PrimesDemo1 {
	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}
}
