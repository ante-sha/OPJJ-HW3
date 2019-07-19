/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred koji definira element kojeg generira {@link SmartScriptParser} kada unutar taga pročitaju 
 * znakovi između navodnika ("[tekst]"). Unutar teksta se mogu nalaziti dodatni navodnici ako su napisani
 * kao {@code \"} u izvornom tekstu, uz to u tekstu se može nalaziti i znak '\' ako je u izvornom 
 * tekstu kao {@code \\}
 * @author Ante Miličević
 *
 */
public class ElementString extends Element {
	/**Tekst bez navodnika*/
	private String value;

	/**
	 * Konstruktor koji inicijalizira tekst koji se sprema u varijabli value.
	 * @param value tekst elementa
	 */
	public ElementString(String value) {
		super();
		this.value = value;
	}
	
	/**
	 * Getter za value
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asText() {
		return value;
	}

	/**
	 * <p>Zbog pravila {@link SmartScriptLexer}-a i {@link SmartScriptParser}-a te rekreiranja
	 * istoznačnog dokumenta pri prebacivanju stringa nazad u text vraćaju se nazad znakovi '\' na 
	 * nužno potrebna mjesta da bi ponovnim parsiranjem dokument imao isto značenje.</p><br>
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = value.replaceAll("(\\\\)","\\\\$1");
		result = result.replaceAll("(\")", "\\\\$1");
		return "\"" + result + "\"";
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
		if (!(obj instanceof ElementString))
			return false;
		ElementString other = (ElementString) obj;
		return Objects.equals(value, other.value);
	}
	
	
}
