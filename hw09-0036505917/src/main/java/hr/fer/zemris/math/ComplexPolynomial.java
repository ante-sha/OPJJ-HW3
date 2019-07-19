package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Razred modelira kompleksni polinom koji se zadaje tako da se index faktora u
 * polju tretira kao broj potencije uz koji faktor "stoji".
 * 
 * @author Ante Miličević
 *
 */
public class ComplexPolynomial {
	/**
	 * Faktori polinoma
	 */
	private Complex[] factors;

	/**
	 * Konstruktor u kojem se redom zadaju potencije od 0 pa do n
	 * 
	 * @param factors faktori polinoma
	 * 
	 * @throws IllegalArgumentException ako je u konstruktoru predan null ili prazno
	 *                                  polje
	 * @throws NullPointerException     ako je neki od faktora null
	 */
	public ComplexPolynomial(Complex... factors) {
		if (factors == null || factors.length == 0) {
			throw new IllegalArgumentException("There is no data provided!");
		}

		this.factors = new Complex[factors.length];
		for (int i = 0; i < factors.length; i++) {
			this.factors[i] = Objects.requireNonNull(factors[i]);
		}
	}

	/**
	 * Metoda koja čita red polinoma
	 * 
	 * @return red polinoma
	 */
	public short order() {
		return Integer.valueOf(factors.length - 1).shortValue();
	}

	/**
	 * Metoda koja množi polinome
	 * 
	 * @param p
	 * @return rezultat njihovog množenja
	 * 
	 * @throws NullPointerException ako je p null
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p);

		Complex[] resultF = new Complex[order() + p.order() + 1];

		for (int i = 0, n = resultF.length; i < n; i++) {
			resultF[i] = Complex.ZERO;
		}

		for (int i = 0, n = factors.length; i < n; i++) {
			for (int j = 0, m = p.factors.length; j < m; j++) {
				resultF[i + j] = resultF[i + j].add(factors[i].multiply(p.factors[j]));
			}
		}

		return new ComplexPolynomial(resultF);
	}

	/**
	 * Metoda derivira polinom i za njegovu derivaciju stvara polinom koji vraća
	 * kroz rezultat funkcije
	 * 
	 * @return derivirani polinom
	 */
	public ComplexPolynomial derive() {
		Complex[] derivedFactors = new Complex[factors.length - 1];

		for (int i = 1, n = factors.length; i < n; i++) {
			derivedFactors[i - 1] = factors[i].multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(derivedFactors);
	}

	/**
	 * Metoda koja unutar polinoma uvrštava vrijednost z
	 * 
	 * @param z vrijednost koja se uvrštava u polinom
	 * @return vrijednost polinoma u točki z
	 * 
	 * @throws NullPointerException ako je z null
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z);
		Complex result = Complex.ZERO;

		int n = 0;
		for (Complex factor : factors) {
			result = result.add(factor.multiply(z.power(n++)));
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = factors.length - 1; i >= 0; i--) {
			sb.append(factors[i].toString());
			sb.append(i == 0 ? "" : String.format("*z^%d", i));
			if (i != 0) {
				sb.append('+');
			}
		}

		return sb.toString();
	}
}
