package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Razred koji modelira polinom s kompleksnim faktorima tako da je zadan s
 * koeficijentom i nul točkama. Nad primjerkom razreda se može pozvati metoda
 * {@link #toComplexPolynom()} za pretvaranje u oblik polinoma definiranog
 * faktorima uz potencije.
 * 
 * @author Ante Miličević
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Korijeni polinoma
	 */
	private Complex[] roots;
	/**
	 * Konstanta polinoma
	 */
	private Complex constant;

	/**
	 * Konstruktor koji definira konstantu i korijene polinoma
	 * 
	 * @param constant konstanta polinoma
	 * @param roots    korijeni polinoma
	 * 
	 * @throws NullPointerException     ako je konstanta ili neki od korijena null
	 * @throws IllegalArgumentException ako je roots null ili ako ne sadrži ni jedan
	 *                                  korijen
	 */
	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		this.constant = Objects.requireNonNull(constant);

		if (roots == null || roots.length == 0) {
			throw new IllegalArgumentException("Roots must be present");
		}

		this.roots = new Complex[roots.length];
		for (int i = 0; i < roots.length; i++) {
			this.roots[i] = Objects.requireNonNull(roots[i]);
		}
	}

	/**
	 * Metoda koja u računa vrijednost polinoma u točki z
	 * 
	 * @param z kompleksni broj za koji se traži vrijednost polinoma
	 * @return vrijednost polinoma u z
	 * 
	 * @throws NullPointerException ako je z null
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z);
		Complex result = constant;
		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}
		return result;
	}

	/**
	 * Metoda koja pretvara polinom iz zapisa s korijenima u zapis faktoriziran po
	 * potencijama
	 * 
	 * @return polinom u faktoriziranom obliku po potencijama
	 */
	public ComplexPolynomial toComplexPolynom() {
		Complex[] factors = new Complex[roots.length + 1];

		for (int i = 0, n = factors.length; i < n; i++) {
			factors[i] = Complex.ZERO;
		}

		factors[0] = constant;
		for (int i = 0, n = roots.length; i < n; i++) {
			factors[i + 1] = factors[i];
			for (int j = i; j > 0; j--) {
				factors[j] = factors[j - 1].add(factors[j].multiply(roots[i].negate()));
			}
			factors[0] = factors[0].multiply(roots[i].negate());
		}

		return new ComplexPolynomial(factors);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(constant);
		for (Complex root : roots) {
			sb.append(String.format("*(z-%s)", root.toString()));
		}
		return sb.toString();
	}

	/**
	 * Metoda koja traži prvi kompleksni broj koji je po apsolutnoj vrijednosti
	 * udaljen od z za treshold
	 * 
	 * @param z        uzorak za kojeg se traži korijen
	 * @param treshold tolerancija
	 * 
	 * @return -1 ako takav ne postoji, inače index korijena
	 * 
	 * @throws NullPointerException ako je z null
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		Objects.requireNonNull(z);

		for (int i = 0, n = roots.length; i < n; i++) {
			if (Math.abs(z.sub(roots[i]).module()) < treshold) {
				return i + 1;
			}
		}
		return -1;
	}
}
