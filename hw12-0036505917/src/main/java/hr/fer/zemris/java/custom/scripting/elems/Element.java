/**
 * Paket u kojem se nalaze svi mogući elementi koje generira {@link SmartScriptParser}
 * tumačenjem tokena {@link SmartScriptLexer}.
 */
package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Vršni razred iz kojeg će svi izvedeni razredi biti svi mogući elementi koje generira
 * {@link SmartScriptParser} unutar Tag-ova.
 * @author Ante Miličević
 *
 */
public class Element {
	/**
	 * Metoda koja vraća reprezentaciju elementa u njegovom izvornom obliku.
	 * @return element u tekstualnom obliku
	 */
	public String asText() {
		return "";
	}
}
