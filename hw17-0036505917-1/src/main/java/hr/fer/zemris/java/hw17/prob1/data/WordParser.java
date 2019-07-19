package hr.fer.zemris.java.hw17.prob1.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Parser koji grupira slova u riječi i stavlja ih u listu, a sve ostalo zanemaruje.
 * 
 * @author Ante Miličević
 *
 */
public class WordParser {
	/**
	 * Metoda koja parsira tekst
	 * 
	 * @param string tekst
	 * @return lista riječi
	 */
	public static List<String> parseString(String string) {
		List<String> result = new LinkedList<>();
		
		char[] chars = string.toCharArray();
		
		StringBuilder sb = new StringBuilder();
		boolean wordReady = false;
		for(int i = 0; i < chars.length; i++) {
			if(Character.isAlphabetic(chars[i])) {
				sb.append(chars[i]);
				wordReady = true;
			} else if (wordReady) {
				result.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		if (wordReady) {
			result.add(sb.toString());
		}
		
		return result;
	}
}
