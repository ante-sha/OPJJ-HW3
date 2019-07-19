package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji modelira izraz usporedbe gdje je rezultat metode
 * {@link IFieldValueGetter#get(StudentRecord)} lijevi operand,
 * {@link IComparisonOperator#satisfied(String, String)} operator usporedbe, a
 * {@link String} drugi operand.
 * 
 * @author Ante Miličević
 *
 */
public class ConditionalExpression {
	/**
	 * Ekstraktor za razred {@link StudentRecord}
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * Operator nad dva stringa operacije tipa zadovoljava/ne zadovoljava
	 */
	private IComparisonOperator operator;
	/**
	 * Drugi operand pri operaciji usporedbe
	 */
	private String literal;

	/**
	 * Konstruktor koji inicijalizira sve članske varijable
	 * 
	 * @param fieldGetter
	 * @param literal
	 * @param operator
	 * @throws NullPointerException ako je barem jedan parametar null
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String literal, IComparisonOperator operator) {
		super();
		this.fieldGetter = Objects.requireNonNull(fieldGetter);
		this.operator = Objects.requireNonNull(operator);
		this.literal = validateLiteral(literal);
	}

	/**
	 * Pomoćna metoda koja validira ispravnost zapisa literala
	 * 
	 * @param literal
	 * @return literal
	 * 
	 * @throws NullPointerException      ako je literal null
	 * @throws IllegalArguementException ako literal sadrži više wildcard-ova *
	 */
	private String validateLiteral(String literal) {
		Objects.requireNonNull(literal);
		if (operator != ComparisonOperators.LIKE)
			return literal;
		if (literal.indexOf('*') != literal.lastIndexOf('*')) {
			throw new IllegalArgumentException("Literal can not have more than one * wildcard");
		}
		return literal;
	}

	/**
	 * Getter za fieldGetter
	 * 
	 * @return fieldGetter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Getter za operator
	 * 
	 * @return operator
	 */
	public IComparisonOperator getOperator() {
		return operator;
	}

	/**
	 * Getter za literal
	 * 
	 * @return literal
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Metoda koja testira {@code record} na temelju podataka predanih u
	 * konstruktoru
	 * 
	 * @param record zapis koji se testira
	 * @return true ako zadovoljava izraz false inače
	 */
	public boolean testRecord(StudentRecord record) {
		return operator.satisfied(fieldGetter.get(record), literal);
	}

}
