/**
 * Paket u koji sadrži sve razrede koje koristi {@link SmartScriptLexer}
 */
package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * Razred koji modelira token koje generira {@link SmartScriptLexer}.
 * Tipovi tokena definirani su enumeracijom {@link TokenType}
 * @author Ante Miličević
 *
 */
public class Token {
	/**Tip tokena*/
	private TokenType type;
	/**Vrijednost tokena*/
	private Object value;
	
	/**
	 * Konstruktor koji inicijalizira sve vrijednosti
	 * @param type tip tokena
	 * @param value vrijednost tokena
	 * @throws NullPointerException ako je tip tokena null
	 */
	public Token(TokenType type, Object value) {
		Objects.requireNonNull(type);
		this.type = type;
		this.value = value;
	}

	/**
	 * Getter za tip tokena
	 * @return type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Getter za vrijednost tokena
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return type + " " + value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
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
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}
