package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji modelira token koji služi kao objekt prijenosa informacija između
 * {@link QueryLexer} i {@link QueryParser}
 * 
 * @author Ante Miličević
 *
 */
public class Token {
	/** Vrijednost koju token prenosi */
	private String value;
	/** Tip tokena */
	private TokenType type;

	/**
	 * Konstruktor koji inicijalizira sve članske varijable
	 * 
	 * @param value vrijednost koju token čuva
	 * @param type  tip tokena
	 */
	public Token(String value, TokenType type) {
		super();
		this.value = value;
		this.type = type;
	}

	/**
	 * Getter za vrijednost
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Getter za tip tokena
	 * 
	 * @return type
	 */
	public TokenType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Token))
			return false;
		Token other = (Token) obj;
		return type == other.type && Objects.equals(value, other.value);
	}

	/**
	 * Javna ugnježđena enumeracija koja označava tip tokena {@link Token}.
	 * <p>
	 * Pravila tipova tokena:<br>
	 * OPERATOR označava token koji kao vrijednost prenosi jedan od dopuštenih
	 * operatora u interakciji s {@link ComparisonOperators}<br>
	 * LITERAL token koji sadrži cijeli string koji je u izvornom tekstu bio omeđen
	 * znakom "<br>
	 * TEXT token koji sadrži običan tekst<br>
	 * EOF token koji označava kraj teksta
	 * </p>
	 * 
	 * @author Ante Miličević
	 *
	 */
	public enum TokenType {
		TEXT, LITERAL, OPERATOR, EOF;
	}
}
