/**
 * Paket u kojem se nalaze razredi koji su rezultat rješavanja
 * 2. zadatka 3. domaće zadaće.
 */
package hr.fer.zemris.java.hw03.prob1;

/**
 * Razred koji modelira tokene koje generira {@link Lexer}. Sadrže dva svojsta
 * tip tokena i vrijednost koju token čuva.
 * @author Ante Miličević
 *
 */
public class Token {
	/**tip tokena*/
	private TokenType type;
	/**vrijednost asocirana s tipom tokena*/
	private Object value;
	
	/**
	 * Konstruktor koji definira oba svojstva tokena.
	 * @param type tip tokena
	 * @param value vrijednost povezana s tipom tokena
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Getter za svojstvo value
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * Getter za svojstvo type
	 * @return type
	 */
	public TokenType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "["+type.toString()+","+value+"]";
	}
}
