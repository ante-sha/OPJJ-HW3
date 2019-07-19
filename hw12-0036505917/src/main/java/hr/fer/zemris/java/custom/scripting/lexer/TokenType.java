/**
 * Paket u koji sadrži sve razrede koje koristi {@link SmartScriptLexer}
 */
package hr.fer.zemris.java.custom.scripting.lexer;
/**
 * Razred koji modelira tipove {@link Token}-a koje generira {@link SmartScriptLexer}.
 * <p>Definicije tipova tokena:<br>
 * <div>{@code OTVOREN_TAG} označava token "{$" kojeg se generira unutar TEXT stanja</div>
 * <div>{@code ZATVOREN_TAG} označava token "$}" kojeg se generira unutar TAG stanja</div>
 * <div>{@code TEXT_TYPE} token koji se generira unutar TEXT stanja, dozvoljene escape sekvence su "\\" i "\{"
 * obje se tretiraju kao znak tako da se prvi escape zanemari</div>
 * <div>{@code OPERATOR} token koji se generira unutar TAG stanja, dozvoljeni operatori su '+','-','*','/','^';
 * također su definirani privatnom statičkom varijablom u razredu {@link SmartScriptLexer}</div>
 * <div>{@code STRING} token koji se generira unutar TAG stanja, dozvoljene escape sekvence su "\\" i "\""
 * obje se tretiraju kao znak tako da se prvi escape zanemari</div>
 * <div>{@code DECIMALNI} token koji se generira unutar TAG stanja, dozvoljeni su decimalni brojevi
 * koji se mogu zapisati kao double varijabla i cijeli brojevi
 * koji se mogu zapisati kao int varijabla</div>
 * <div>{@code JEDNAKO} token koji se generira unutar TAG stanja, dozvoljen je samo na početku taga
 * i tada označava da je riječ o ECHO tagu</div>
 * <div>{@code FUNKCIJA} token koji se generira unutar TAG stanja, zapis mora biti formata kojeg prihvaća
 * regex {@code "@[a-zA-z][a-zA-Z|_|[0-9]]*"}</div>
 * <div>{@code VARIJABLA} token koji se generira unutar TAG stanja, zapis mora biti formata kojeg prihvaća
 * regex {@code "[a-zA-Z][a-zA-Z|_|[0-9]]*"}</div>
 * <div>{@code EOF} token koji se generira na kraju dokumenta
 * </p>
 * Napomena: stanje TAG je ekvivalentno sa SmartScriptLexerState.TAG, a stanje TEXT sa SmartScriptLexerState.TEXT
 * @author Ante Miličević
 *
 */
public enum TokenType {
	OTVOREN_TAG,
	ZATVOREN_TAG,
	TEXT_TYPE,
	OPERATOR,
	STRING,
	DECIMALNI,
	JEDNAKO,
	FUNKCIJA,
	VARIJABLA,
	EOF;
}
