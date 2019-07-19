/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred koji definira element kojeg generira {@link SmartScriptParser} kada unutar taga pročita
 * operator. Dozvoljeni operatori su '+','-','*','/','^'.
 * @author Ante Miličević
 *
 */
public class ElementOperator extends Element {
	/**Simbol operatora*/
	private String symbol;
	private static final char[] operatori = {'+','-','*','/','^'};
	/**
	 * Konstruktor koji definira operator preko Stringa symbol
	 * @param symbol string koji u sebi sadrži operator
	 * @throws IllegalArgumentException ako je ulazni string duljine različite od 1
	 * <br>ako je simbol nepodržani znak
	 */
	public ElementOperator(String symbol) {
		if(symbol.length() != 1)
			throw new IllegalArgumentException("Operator ne može imati dva znaka!");
		dobarOperator(symbol.charAt(0));
		this.symbol = symbol;
	}
	
	/**
	 * Dodatna privatna metoda za validaciju podržanosti operatora c.
	 * @param c operator
	 * @throws IllegalArgumentException ako je operator nepodržan
	 */
	private void dobarOperator(char c) {
		for(int i = 0;i<operatori.length;i++) {
			if(c == operatori[i])
				return;
		}
		throw new IllegalArgumentException("Nedozvoljen operator " + c);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asText() {
		return symbol;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(symbol);
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
		if (!(obj instanceof ElementOperator))
			return false;
		ElementOperator other = (ElementOperator) obj;
		return Objects.equals(symbol, other.symbol);
	}
	
	
}
