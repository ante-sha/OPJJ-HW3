/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred koji definira element kojeg generira {@link SmartScriptParser} kada unutar taga pročita
 * decimalni broj.
 * @author Ante Miličević
 *
 */
public class ElementConstantDouble extends Element {
	/**Vrijednost pročitanog broja*/
	private double value;

	/**
	 * Konstruktor koji definira jedini parametar ElementConstantDouble razreda {@code value}.
	 * @param value
	 */
	public ElementConstantDouble(double value) {
		super();
		this.value = value;
	}
	
	/**
	 * Getter za value.
	 * @return value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Double.toString(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementConstantDouble))
			return false;
		ElementConstantDouble other = (ElementConstantDouble) obj;
		return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}
	
	
}
