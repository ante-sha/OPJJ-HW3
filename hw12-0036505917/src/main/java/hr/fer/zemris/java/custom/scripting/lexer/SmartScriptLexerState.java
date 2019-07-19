/**
 * Paket u koji sadrži sve razrede koje koristi {@link SmartScriptLexer}
 */
package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeracija koja sadrži stanja u kojima se {@link SmartScriptLexer} može naći.
 * <br>
 * TAG označava stanje unutar kojeg se odvija definicija funkcije<br>
 * TEXT označava stanje u kojemu se svi znakovi tretiraju kao običan tekst
 * @author Ante Miličević
 *
 */
public enum SmartScriptLexerState {
	TEXT,
	TAG;
}
