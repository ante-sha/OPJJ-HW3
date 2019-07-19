package hr.fer.zemris.hw06.shell.commands.namebuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Razred modelira parser i generator za kompozitni {@link NameBuilder}.<br>
 * Sve što se u tekstu nalazi između '${' i '}' tretira se kao supstitucijska
 * naredba. Supstitucijska naredba se može definirati na dva načina.<br>
 * Prvi je tako da se unutar nje nalazi samo broj koji predstavlja koja grupa se
 * treba ispisati na tom mjestu.<br>
 * Drugi unutar oznaka sadrži broj grupe na prvom mjestu te zarezom odvojen
 * minimalan broj mjesta pri formatiranju ispisa. Ako se ispred minimalnog broja
 * mjesta nalazi znak '0' mjesta se nadopunjavaju znakom '0' inače se
 * nadopunjavanje vrši s znakom ' ' (razmak).
 * 
 * @author Ante Miličević
 *
 */
public class NameBuilderParser {

	/**
	 * Uzorak koji detektira supstitucijski naredbu u tekstu
	 */
	private static final Pattern pattern = Pattern.compile("(\\$\\{[^\\}]*\\})",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	/**
	 * Informacije o parsiranju teksta
	 */
	private Matcher matcher;
	/**
	 * Izraz koji se parsira
	 */
	private String expression;
	/**
	 * Generator imena
	 */
	private NameBuilder nameBuilder;

	/**
	 * Konstruktor koji prima izraz za parsiranje
	 * 
	 * @param expression izraz za parsiranje
	 */
	public NameBuilderParser(String expression) {
		matcher = pattern.matcher(expression);
		this.expression = expression;
		parse();
	}

	/**
	 * Metoda koja parsira predani izraz i generira generator imena
	 */
	private void parse() {
		int index = 0;
		nameBuilder = NameBuilder.text("");

		// while there is another supstitutional command in expression
		while (matcher.find(index)) {

			// if there is text between two supstitutional commands (or begining and first
			// supstitutional command)
			if (matcher.start() != index) {
				nameBuilder = nameBuilder.then(NameBuilder.text(expression.substring(index, matcher.start())));
			}

			String match = matcher.group(1);

			match = match.replaceAll("\\s", ""); // removing spaces
			match = match.replaceAll("(\\$\\{)([^\\}]*)(\\})", "$2"); // removing open and close tag

			if (!match.contains(",")) {
				nameBuilder = nameBuilder.then(generateBasicGroupBuilder(match));
			} else {
				nameBuilder = nameBuilder.then(generateExtendedGroupBuilder(match));
			}
			index = matcher.end();
		}

		nameBuilder = nameBuilder.then(NameBuilder.text(expression.substring(index)));
	}

	/**
	 * Getter za generator imena
	 * 
	 * @return nameBuilder
	 */
	public NameBuilder getNameBuilder() {
		return nameBuilder;
	}

	/**
	 * Metoda koja generira {@link NameBuilder} iz proširene supsitucijske naredbe.
	 * 
	 * @param match proširena supstitucijska naredba bez oznaka
	 * @return NameBuilder za predane argumente
	 * 
	 * @throws IllegalArgumentException ako se zadani argumenti ne mogu
	 *                                  interpretirati kao brojevi ili ako je index
	 *                                  grupe negativan broj
	 */
	private NameBuilder generateExtendedGroupBuilder(String match) {
		String[] data = match.split(",");

		if (data.length != 2) {
			throw new IllegalArgumentException("Wrong format of group builder");
		}

		int groupId = validateGroupId(data[0]);

		boolean zeroPadding = data[1].length() > 1 && data[1].charAt(0) == '0';

		int padding;
		try {
			padding = Integer.parseInt(data[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + data[1] + "' can not be interpreted as number");
		}
		return NameBuilder.group(groupId, zeroPadding ? '0' : ' ', padding);
	}

	/**
	 * Metoda koja generira {@link NameBuilder} iz jednostavne supsitucijske
	 * naredbe.
	 * 
	 * @param match proširena supstitucijska naredba bez oznaka
	 * @return NameBuilder za predani argument
	 * 
	 * @throws IllegalArgumentException ako je index grupe negativan broj ili ako se
	 *                                  match ne može protumačiti kao broj
	 */
	private NameBuilder generateBasicGroupBuilder(String match) {
		int groupId = validateGroupId(match);

		return NameBuilder.group(groupId);
	}

	/**
	 * Metoda koja interpretira string kao index grupe te ga validira
	 * 
	 * @param string broj zapisan u String formatu
	 * 
	 * @return index grupe
	 * 
	 * @throws IllegalArgumentException ako je index grupe negativan broj ili ako se
	 *                                  match ne može protumačiti kao broj
	 */
	public int validateGroupId(String string) {
		try {
			int groupId = Integer.parseInt(string);
			if (groupId < 0) {
				throw new IllegalArgumentException("Group number can not be less than 0");
			}
			return groupId;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + string + "' can not be interpreted as groupId");
		}
	}
}
