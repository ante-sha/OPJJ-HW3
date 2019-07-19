/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred koji definira element kojeg generira {@link SmartScriptParser} kada unutar taga pročita
 * ime varijable. Format varijable u regexu je "[a-zA-Z][a-zA-Z|_|0-9]*".
 * @author Ante Miličević
 *
 */
public class ElementVariable extends Element {
	/**Naziv varijable*/
	private String name;
	
	/**
	 * Konstruktor koji inicijalizira naziv varijable.
	 * @param name ime varijable
	 * @throws IllegalArgumentExceptiom ako varijabla nema dobar naziv
	 */
	public ElementVariable(String name) {
		if(!name.matches("[a-zA-Z][a-zA-Z|_|0-9]*"))
			throw new IllegalArgumentException("Variabla nije dobrog naziva " + name);
		this.name = name;
	}
	
	/**
	 * Getter za name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
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
		if (!(obj instanceof ElementVariable))
			return false;
		ElementVariable other = (ElementVariable) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
