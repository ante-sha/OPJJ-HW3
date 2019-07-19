package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji sadrži glavne usporedne operatore {@link IComparisonOperator}
 * koji operiraju nad dva stringa. Kroz javnu statičku metodu
 * {@link #interpretStringAsOperator(String)} tumači string kao operator i vraća
 * pripadajuću statičku konstantnu varijablu {@link IComparisonOperator}-a.
 * 
 * @author Ante Miličević
 *
 */
public class ComparisonOperators {
	/**
	 * Operator koji vraća true ako je {@code string1} po prirodnom poretku manji od
	 * {@code string2}
	 */
	public static final IComparisonOperator LESS = (string1, string2) -> string1.compareTo(string2) < 0;
	/**
	 * String reprezentacija {@link #LESS} operatora
	 */
	public static final String less = "<";
	/**
	 * Operator koji vraća true ako je {@code string1} po prirodnom poretku veći od
	 * {@code string2}
	 */
	public static final IComparisonOperator GREATER = (string1, string2) -> string1.compareTo(string2) > 0;
	/**
	 * String reprezentacija {@link #GREATER} operatora
	 */
	public static final String greater = ">";
	/**
	 * Operator koji vraća true ako je {@code string1} jednak {@code string2}
	 */
	public static final IComparisonOperator EQUALS = (string1, string2) -> string1.equals(string2);
	/**
	 * String reprezentacija {@link #EQUALS} operatora
	 */
	public static final String equals = "=";
	/**
	 * Operator koji vraća true ako je {@code string1} različit od {@code string2}
	 */
	public static final IComparisonOperator NOT_EQUALS = (string1, string2) -> !string1.equals(string2);
	/**
	 * String reprezentacija {@link #NOT_EQUALS} operatora
	 */
	public static final String notEquals = "!=";
	/**
	 * Operator koji vraća true ako je {@code string1} po prirodnom poretku manji
	 * ili jedak {@code string2}
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (string1, string2) -> string1.compareTo(string2) <= 0;
	/**
	 * String reprezentacija {@link #LESS_OR_EQUALS} operatora
	 */
	public static final String lessOrEquals = "<=";
	/**
	 * Operator koji vraća true ako je {@code string1} po prirodnom poretku veći ili
	 * jednak {@code string2}
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (string1, string2) -> string1.compareTo(string2) >= 0;
	/**
	 * String reprezentacija {@link #GREATER_OR_EQUALS} operatora
	 */
	public static final String greaterOrEquals = ">=";
	/**
	 * Operator koji vraća true ako je {@code string1} poput {@code string2}<br>
	 * Operacija poput se definira tako da se unutar {@code string2} može nalaziti
	 * wildcard '*' koji označava da se umjesto njega može nalaziti 0 ili više
	 * proizvoljnih znakova
	 */
	public static final IComparisonOperator LIKE = (string1, string2) -> {
		if (string2.contains("*")) {
			int wildcard = string2.indexOf('*');
			String firstPart = string2.substring(0, wildcard);
			string2 = string2.substring(wildcard + 1);
			if (string2.contains("*")) {
				throw new IllegalArgumentException("Filter can not contains more than one * wild card");
			}
			//making expression regular and than testing it with regex method matches
			return string1.matches(firstPart + ".*" + string2);
		} else {
			return string1.equals(string2);
		}
	};

	/**
	 * String reprezentacija {@link #LIKE} operatora
	 */
	public static final String like = "LIKE";
	/**
	 * Metoda koja interpretira {@code operator} kao {@link IComparisonOperator}
	 * implementiran unutar razreda
	 * 
	 * @param operator znakovni prikaz operatora
	 * @return IComparisonOperator koji je istoznačan interpretaciji znakovnog niza
	 *         {@code operator}
	 * @throws IllegalArgumentException ako taj operator ne postoji
	 * @throws NullPointerException ako je operator null
	 */
	public static IComparisonOperator interpretStringAsOperator(String operator) {
		Objects.requireNonNull(operator);
		if (operator.equals(equals)) {
			return EQUALS;
		} else if (operator.equals(greater)) {
			return GREATER;
		} else if (operator.equals(less)) {
			return LESS;
		} else if (operator.equals(notEquals)) {
			return NOT_EQUALS;
		} else if (operator.equals(lessOrEquals)) {
			return LESS_OR_EQUALS;
		} else if (operator.equals(greaterOrEquals)) {
			return GREATER_OR_EQUALS;
		} else if (operator.equals(like)) {
			return LIKE;
		} else {
			throw new IllegalArgumentException("Operator " + operator + " is not defined!");
		}
	}
}
