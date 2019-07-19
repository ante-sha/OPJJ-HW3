package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Razred koji omogućava skladištenje vrijednosti. Ako je tip vrijednosti
 * Double,Integer,String ili null omogućen je i poziv osnovnih aritmetičkih
 * operacija nad njima po sljedećim pravilima:<br>
 * 1) null vrijednosti se tretiraju kao Integer.valueOf(0)<br>
 * 2) ako je operator operacije objekt tipa String on se pretvara u odgovarajuću
 * numeričku vrijednost ili se baca iznimka<br>
 * 3) Ako se u operaciji kao argumenti nađu 2 Integera rezultat je Integer inače
 * je rezultat Double (iznimka je numCompare gdje je rezultat uvijek
 * Integer)<br>
 * U svim operacijama osim numCompare rezultat se sprema u vrijednost
 * ValueWrapper-a.
 * 
 * @author Ante Miličević
 *
 */
public class ValueWrapper {
	/**
	 * Vrijednost koju čuva objekt
	 */
	private Object value;

	/**
	 * Defaultni validator za sve operacije
	 */
	private static final BiPredicate<Number, Number> defaultOperationValidator = (a, b) -> true;
	/**
	 * Validator za provjeru argumenata operacije dijeljenja
	 */
	private static final BiPredicate<Number, Number> divideValidator = (a, b) -> b.doubleValue() != 0;
	/**
	 * Funkcija za zbrajanje Integer-a
	 */
	private static final BiFunction<Integer, Integer, Integer> addInt = (a, b) -> a + b;
	/**
	 * Funkcija za zbrajanje Double-ova
	 */
	private static final BiFunction<Double, Double, Double> addDouble = (a, b) -> a + b;
	/**
	 * Funkcija za oduzimanje Integer-a
	 */
	private static final BiFunction<Integer, Integer, Integer> subtractInt = (a, b) -> a - b;
	/**
	 * Funkcija za oduzimanje Double-ova
	 */
	private static final BiFunction<Double, Double, Double> subtractDouble = (a, b) -> a - b;
	/**
	 * Funkcija za množenje Integer-a
	 */
	private static final BiFunction<Integer, Integer, Integer> multiplyInt = (a, b) -> a * b;
	/**
	 * Funkcija za množenje Double-ova
	 */
	private static final BiFunction<Double, Double, Double> multiplyDouble = (a, b) -> a * b;
	/**
	 * Funkcija za dijeljenje Integer-a
	 */
	private static final BiFunction<Integer, Integer, Integer> divideInt = (a, b) -> a / b;
	/**
	 * Funkcija za dijeljenje Double-ova
	 */
	private static final BiFunction<Double, Double, Double> divideDouble = (a, b) -> a / b;
	/**
	 * Funkcija za usporedbu Integer-a
	 */
	private static final BiFunction<Integer, Integer, Integer> numCompareInt = Integer::compare;
	/**
	 * Funkcija za usporedbu Double-ova
	 */
	private static final BiFunction<Double, Double, Integer> numCompareDouble = Double::compare;

	/**
	 * Konstruktor s inicijalnom vrijednosti.
	 * 
	 * @param value inicijalna vrijednost
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Getter za vrijednost
	 * 
	 * @return value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter za vrijednost
	 * 
	 * @param value nova vrijednost
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Metoda za zbrajanje vrijednosti ValueWrapper-a s incValue Rezultat se sprema
	 * u varijablu {@code value}.
	 * 
	 * @param incValue drugi operand pri zbrajanju
	 * 
	 * @throws RuntimeException ako se operandi ne mogu protumačiti kao Integer ili
	 *                          Double
	 */
	public void add(Object incValue) {
		value = operate(addInt, addDouble, convertValue(value), convertValue(incValue), defaultOperationValidator);
	}

	/**
	 * Metoda za oduzimanje vrijednosti ValueWrapper-a s decValue Rezultat se sprema
	 * u varijablu {@code value}.
	 * 
	 * @param decValue drugi operand pri oduzimanju
	 * 
	 * @throws RuntimeException ako se operandi ne mogu protumačiti kao Integer ili
	 *                          Double
	 */
	public void subtract(Object decValue) {
		value = operate(subtractInt, subtractDouble, convertValue(value), convertValue(decValue),
				defaultOperationValidator);
	}

	/**
	 * Metoda za množenje vrijednosti ValueWrapper-a s mulValue Rezultat se sprema u
	 * varijablu {@code value}.
	 * 
	 * @param mulValue drugi operand pri množenju
	 * 
	 * @throws RuntimeException ako se operandi ne mogu protumačiti kao Integer ili
	 *                          Double
	 */
	public void multiply(Object mulValue) {
		value = operate(multiplyInt, multiplyDouble, convertValue(value), convertValue(mulValue),
				defaultOperationValidator);
	}

	/**
	 * Metoda za dijeljenje vrijednosti ValueWrapper-a s divValue. Rezultat se
	 * sprema u varijablu {@code value}.
	 * 
	 * @param divValue drugi operand pri dijeljenje
	 * 
	 * @throws RuntimeException         ako se operandi ne mogu protumačiti kao
	 *                                  Integer ili Double
	 * @throws IllegalArgumentException ako je drugi operand jednak nuli
	 */
	public void divide(Object divValue) {
		value = operate(divideInt, divideDouble, convertValue(value), convertValue(divValue), divideValidator);
	}

	/**
	 * Metoda za usporedbu vrijednosti ValueWrapper-a s incValue
	 * 
	 * @param withValue drugi operand pri usporedbi
	 * 
	 * @throws RuntimeException ako se operandi ne mogu protumačiti kao Integer ili
	 *                          Double
	 */
	public int numCompare(Object withValue) {
		return operate(numCompareInt, numCompareDouble, convertValue(value), convertValue(withValue),
				defaultOperationValidator).intValue();
	}

	/**
	 * Generička metoda koja izvršava operaciju preko funkcija predanih u argumentu
	 * ovisno o tipu podataka nad kojima se vrši operacija. Validacija operanada se
	 * vrši pomoći predikata iz argumenta.
	 * 
	 * @param funInt    funkcija koja se izvršava ako su oba operanda tipa Integer
	 * @param funDouble funkcija koja se izvršava ako je barem jedan operand tipa
	 *                  Double
	 * @param num1      prvi operand
	 * @param num2      drugi operand
	 * @param validator validator operanada
	 * @return rezultat operacije
	 * 
	 * @throws IllegalArgumentException ako operandi nisu validni
	 */
	public Number operate(BiFunction<Integer, Integer, ? extends Number> funInt,
			BiFunction<Double, Double, ? extends Number> funDouble, Number num1, Number num2,
			BiPredicate<Number, Number> validator) {

		if (!validator.test(num1, num2)) {
			throw new IllegalArgumentException("Operation not defined for arguments " + num1 + " " + num2);
		}

		if (num1 instanceof Integer && num2 instanceof Integer) {
			return funInt.apply((Integer) num1, (Integer) num2);
		} else {
			return funDouble.apply(num1.doubleValue(), num2.doubleValue());
		}
	}

	/**
	 * Metoda koja pročitanu vrijednost pretvara u Integer ili u Double tako da null
	 * tretira kao Integer.valueOf(0), a String se pretvara u zadane tipove po
	 * pravilima navedenim u {@link #convertString()}<br>
	 * 
	 * @param value vrijednost koja se pretvara
	 * @return vrijednost u dozvoljenom tipu Objekta
	 * 
	 * @throws RuntimeException ako se String ne može pretvoriti ili ako tip objekta
	 *                          value nije podržan
	 */
	private static Number convertValue(Object value) {
		if (value instanceof String) {
			value = convertString((String) value);
		}
		if (value == null) {
			return Integer.valueOf(0);
		}
		if (value instanceof Double || value instanceof Integer) {
			return (Number) value;
		}
		throw new RuntimeException("Value is not instance of permitted classes(null included)");
	}

	/**
	 * Metoda pretvara predani string u Integer ili Double tako da ako sadrži znak
	 * '.' i/ili 'e' odnosno 'E' pretvara se u Double, inače se pretvara u Integer.
	 * 
	 * @param string string kojeg se pretvara u Integer ili Double
	 * @return pretvoreni string
	 * 
	 * @throws RuntimeException ako se string ne može pretvoriti
	 */
	private static Number convertString(String string) {
		try {
			if (string.toUpperCase().contains("E") || string.contains(".")) {
				return Double.parseDouble(string);
			} else {
				return Integer.parseInt(string);
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("String " + string + " can not be converted into number");
		}
	}
}
