package hr.fer.zemris.java.hw05.db;

import java.util.NoSuchElementException;
import java.util.Objects;

import static hr.fer.zemris.java.hw05.db.Token.TokenType.*;

/**
 * Pomoćni razred koji tokenizira tekst za razred {@link QueryParser}. Vrste
 * tokena se nalaze unutar enumeracije {@link Token.TokenType}, dok je svaki
 * token modeliran razredom {@link Token}. Dozvoljeni operatori su sadržani u
 * privatnoj statičkoj konstatni {@code operators}.
 * 
 * @author Ante Miličević
 *
 */
public class QueryLexer {
	/**
	 * Polje znakova koje se tokenizira
	 */
	private char[] characters;
	/**
	 * Trenutni index pri čitanju
	 */
	private int index;
	/**
	 * Dozvoljeni operatori unutar predanog teksta
	 */
	private static final String[] operators = { "=", ">", "<", ">=", "<=", "!=" };
	/**
	 * Zastavica koja označava da je token EOF poslan
	 */
	private boolean end;

	/**
	 * Konstruktor koji sprema predani tekst ({@code query}) kao tekst kojeg se
	 * tokenizira
	 * 
	 * @param query tekst kojeg lexer tokenizira
	 * 
	 * @throws NullPointerException ako je query = null
	 */
	public QueryLexer(String query) {
		characters = Objects.requireNonNull(query).toCharArray();
	}

	/**
	 * Metoda koja ispituje da li su pročitani svi znakovi ulaznog niza
	 * 
	 * @return true ako jesu, inače false
	 */
	public boolean getEnd() {
		return end;
	}

	/**
	 * Metoda koji generira idući token čitajući znakove od mjesta {@code index}.
	 * 
	 * @return {@link Token} reprezentaciju grupe znakova
	 * 
	 * @throws NoSuchElementException   ako se zahtjeva čitanje idućeg tokena, a svi
	 *                                  znakovi su pročitani
	 * @throws IllegalArgumentException ako se u tekstu nalaze ne dozvoljeni simboli
	 *                                  ili ako literal nije omeđen navodnicima s
	 *                                  desne strane
	 */
	public Token nextToken() {
		if (getEnd()) {
			throw new NoSuchElementException("All characters had been read!");
		}

		for (; index < characters.length; index++) {
			if (Character.isWhitespace(characters[index]))
				continue;
			else if (Character.isLetter(characters[index]))
				return readText();
			else if (characters[index] == '"')
				return readLiteral();
			else
				return readOperator();
		}
		end = true;
		return new Token(null, EOF);
	}

	/**
	 * Pomoćna metoda za interpretaciju idućeg niza znaka u operator definiran u
	 * varijabli {@code operators}
	 * 
	 * @return token reprezentacija operatora
	 * 
	 * @throws IllegalArgumentException ako operacija za skup simbola nije
	 *                                  definirana
	 */
	private Token readOperator() {
		StringBuilder result = new StringBuilder();

		for (; index < characters.length; index++) {
			if (Character.isLetter(characters[index]))
				break;
			if (characters[index] == '"')
				break;
			if (Character.isWhitespace(characters[index]))
				break;
			result.append(characters[index]);
		}
		String operator = result.toString();
		checkOperator(operator);

		return new Token(operator, OPERATOR);
	}

	/**
	 * Validacija operatora preko članske varijable {@code operators}
	 * 
	 * @param operator provjeravani operator
	 * 
	 * @throws IllegalArgumentException ako operator nije podržan
	 */
	private void checkOperator(String operator) {
		for (String s : operators) {
			if (s.equals(operator))
				return;
		}
		throw new IllegalArgumentException("Unsupported operator " + operator);
	}

	/**
	 * Pomoćna metoda za interpretaciju idućeg niza znaka u tekst koji sadrži
	 * samo slova
	 * 
	 * @return token reprezentacija teksta
	 */
	private Token readText() {
		StringBuilder result = new StringBuilder();
		result.append(characters[index++]);
		for (; index < characters.length; index++) {
			if (!Character.isLetter(characters[index]))
				break;
			result.append(characters[index]);
		}
		return new Token(result.toString(), TEXT);
	}

	/**
	 * Pomoćna metoda za interpretaciju idućeg niza znaka u literal, tj. skup znakova
	 * omeđen navodnicima
	 * 
	 * @return token reprezentacija literala
	 * @return
	 */
	private Token readLiteral() {
		StringBuilder result = new StringBuilder();
		index++;
		for (; index < characters.length; index++) {
			if (characters[index] == '"') {
				index++;
				return new Token(result.toString(), LITERAL);
			}
			result.append(characters[index]);
		}
		throw new IllegalArgumentException("Literal is not closed!");
	}
}
