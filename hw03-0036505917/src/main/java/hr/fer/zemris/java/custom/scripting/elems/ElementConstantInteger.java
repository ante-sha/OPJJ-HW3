/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred koji definira element kojeg generira {@link SmartScriptParser} kada unutar taga pročita
 * cijeli broj.
 * @author Ante Miličević
 *
 */
public class ElementConstantInteger extends Element {
	/**Vrijednost cijelog boja koju čuva objekt*/
	private int value;
	
	/**
	 * Konstruktor koji definira jedini parametar ElementConstantInteger razreda {@code value}
	 * @param value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Getter za value
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Integer.toString(value);
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
		if (!(obj instanceof ElementConstantInteger))
			return false;
		ElementConstantInteger other = (ElementConstantInteger) obj;
		return value == other.value;
	}
	
	
}
