package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Razred modelira kompleksni broj i nudi osnovne operacije nad njim. Unutar
 * razreda nalaze se i osnovni kompleksni brojevi
 * ZERO(0),ONE(1),ONE_NEG(-1),IM(i),IM_NEG(-i). Primjerci razreda su ne
 * promjenjivi.
 * 
 * @author Ante Miličević
 *
 */
public class Complex {

	/**
	 * Realni dio broja
	 */
	private double re;
	/**
	 * Imaginarni dio broja
	 */
	private double im;

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Prazan konstruktor koji stvara kompleksni broj (0,0)
	 */
	public Complex() {
	}

	/**
	 * Konstruktor koji definira realni i imaginarni dio kompleksnog broja
	 * 
	 * @param re realni dio
	 * @param im imaginarni dio
	 */
	public Complex(double re, double im) {
		super();
		this.re = re;
		this.im = im;
	}

	/**
	 * Metoda za izračun modula kompleksnog broja
	 * 
	 * @return modul kompleksnog broja
	 */
	public double module() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Metoda množni kompleksni broj s kompleksnim brojem predanim u argumentu
	 * metode te za rezultat vraća novi kompleksni broj koji je rezultat množenja
	 * 
	 * @param other
	 * @return rezultat množenja
	 * 
	 * @throws NullPointerException ako je other null
	 */
	public Complex multiply(Complex other) {
		Objects.requireNonNull(other);
		return new Complex(re * other.re - im * other.im, re * other.im + im * other.re);
	}

	/**
	 * Metoda dijeli kompleksni broj s {@code other} i rezultat sprema u novi
	 * kompleksni broj kojeg vraća kroz rezultat metode.
	 * 
	 * @param other dijelitelj
	 * @return rezultat dijeljenja
	 * 
	 * @throws IllegalArgumentException ako je dijelitelj nula
	 * @throws NullPointerException     ako je other null
	 */
	public Complex divide(Complex other) {
		Objects.requireNonNull(other);
		if (other.re == 0 && other.im == 0)
			throw new IllegalArgumentException("Can not divide with complex number amplitude 0!");

		double denominator = other.re * other.re + other.im * other.im;
		double re = (this.re * other.re + this.im * other.im) / denominator;
		double im = (this.im * other.re - this.re * other.im) / denominator;
		return new Complex(re, im);
	}

	/**
	 * Metoda zbraja kompleksne zbroje i rezultat zapisuje u povratni kompleksni
	 * broj
	 * 
	 * @param other drugi operand zbrajanja
	 * @return zbroj
	 * 
	 * @throws NullPointerException ako je other null
	 */
	public Complex add(Complex other) {
		Objects.requireNonNull(other);
		return new Complex(re + other.re, im + other.im);
	}

	/**
	 * Metoda oduzima kompleksne brojeve i rezultat zapisuje u povratni kompleksni
	 * broj
	 * 
	 * @param other drugi operand oduzimanja
	 * @return rezultat oduzimanja
	 * 
	 * @throws NullPointerException ako je other null
	 */
	public Complex sub(Complex other) {
		Objects.requireNonNull(other);
		return new Complex(re - other.re, im - other.im);
	}

	/**
	 * Metoda koja vraća kompleksan broj gdje i realni i imaginarni dio imaju
	 * različit predznak od trenutnog kompleksnog broja
	 * 
	 * @return negirani kompleksni broj
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Metoda koja potencira kompleksni broj. Potencija može biti bilo koji ne
	 * negativan cijeli broj.
	 * 
	 * @param n potencija
	 * @return potencirani kompleksan broj
	 * 
	 * @throws NullPointerException     ako je other null
	 * @throws IllegalArgumentException ako je potencija negativna
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Power can not be 0!");
		}
		double magnitude = Math.pow(module(), n);
		double angle = angle() * n;

		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Metoda računa korijene kompleksnog broja.
	 * 
	 * @param n broj s kojim se korjenuje kompleksan broj
	 * @return n korijena kompleksnog broja
	 * 
	 * @throws NullPointerException     ako je other null
	 * @throws IllegalArgumentException ako je n negativan ili nula
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Root must be defined with positive number!");
		}
		List<Complex> result = new ArrayList<>();

		double magnitude = Math.pow(module(), 1. / n);
		double angle = angle();

		for (int i = 0; i < n; i++) {
			double tmpAngle = (angle + 2 * Math.PI * i) / n;

			result.add(new Complex(magnitude * Math.cos(tmpAngle), magnitude * Math.sin(tmpAngle)));
		}

		return result;
	}

	/**
	 * Metoda za izračun kuta kompleksnog broja
	 * 
	 * @return kut
	 */
	private double angle() {
		return Math.atan2(im, re);
	}

	@Override
	public String toString() {
		return String.format("(%.2f%ci%.2f)", re,im >= 0 ? '+' : '-', im >= 0 ? im : -im);
	}
}
