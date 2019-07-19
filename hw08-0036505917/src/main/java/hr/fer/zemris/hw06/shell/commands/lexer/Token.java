package hr.fer.zemris.hw06.shell.commands.lexer;

/**
 * Razred koji definira strukturu tokena koje generira {@link ArgumentLexer}.
 * Tipovi tokena određeni su enumeracijom {@link TokenType}.
 * 
 * @author Ante Miličević
 *
 */
public class Token {
	/**
	 * Vrijednost koju token čuva
	 */
	private String value;
	/**
	 * Oznaka tokena
	 */
	private TokenType type;

	/**
	 * Konstruktor koji definira vrijednost i oznaku tokena.
	 * 
	 * @param value vrijednost koju token čuva
	 * @param type  oznaka tokena
	 */
	public Token(String value, TokenType type) {
		this.value = value;
		this.type = type;
	}

	/**
	 * Getter za vrijednost tokena
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

	/**
	 * Enumeracija koja označava tipove tokena {@link Token}.<br>
	 * TEXT - bilo kakav skup znakova koji ne započinje sa znakom '"'<br>
	 * STRING - skup znakova koji počinje i završava sa znakom '"', dozvoljene
	 * escape sekvence su '\"' i '\\', ostale kombinacije sa znakom '\' će se
	 * interpretirati kao 2 zasebna znaka.<br>
	 * EOL - označava kraj čitanja
	 * 
	 * @author Ante Miličević
	 *
	 */
	public enum TokenType {
		TEXT, STRING, EOL;
	}
}
