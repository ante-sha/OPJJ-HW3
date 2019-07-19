/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Razred koji definira element kojeg generira {@link SmartScriptParser} kada unutar taga pročita
 * funkciju. Format naziva funkcije u regexu je "[a-zA-Z][_|a-zA-Z|[0-9]]*".
 * @author Ante Miličević
 *
 */
public class ElementFunction extends Element {
	/**Ime funkcije*/
	private String name;

	/**
	 * Konstruktor koji definira ime funkcije
	 * @param name ime funkcije
	 */
	public ElementFunction(String name) {
		super();
		if(!name.matches("[a-zA-Z][_|[a-zA-Z]|[0-9]]*"))
			throw new IllegalArgumentException("Funkcija je krivo nazvana " + name);
		this.name = name;
	}
	
	/**
	 * Getter za name atribut
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
		return "@" + name;
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
		if (!(obj instanceof ElementFunction))
			return false;
		ElementFunction other = (ElementFunction) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
