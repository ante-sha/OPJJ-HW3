/**
 * Paket u kojem se nalaze razredi koji su rezultat rješavanja
 * 2. zadatka 3. domaće zadaće.
 */
package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeracija vrsta tokena {@link Token} koji se koriste u {@link Lexer}
 * @author Ante Miličević
 *
 */
public enum TokenType {
	EOF,
	WORD,
	NUMBER,
	SYMBOL;
}
