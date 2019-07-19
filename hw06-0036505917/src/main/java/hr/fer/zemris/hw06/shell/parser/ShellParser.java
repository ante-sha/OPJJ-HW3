package hr.fer.zemris.hw06.shell.parser;

import java.util.Objects;

import hr.fer.zemris.hw06.shell.Environment;

/**
 * Razred koji služi za konkatenaciju dijelova naredbe pročitane u
 * {@link MyShell}-u. Naredba se može odvajati u više redaka pod uvjetom da je
 * zadnji znak u retku {@link Environment#getMorelinesSymbol()}. Na kraju obrade
 * tekst se parsira i ekstaktiraju se naredba i argumenti.
 * 
 * @author Ante Miličević
 *
 */
public class ShellParser {
	/**
	 * Okolina u kojoj se izvodi ljuska
	 */
	private Environment env;
	/**
	 * Naziv naredbe
	 */
	private String command;
	/**
	 * Argumenti naredbe
	 */
	private String arguments;

	/**
	 * Konstruktor koji definira okolinu rada parsera.
	 * 
	 * @param env okolina u kojoj se izvodi ljuska
	 */
	public ShellParser(Environment env) {
		this.env = Objects.requireNonNull(env);
	}

	/**
	 * Metoda koja čita naredbu s ulaza okoline i parsira ju u naziv naredbe i
	 * argumente.
	 * 
	 * @throws ShellParserException ako naziv naredbe nije naveden
	 */
	public void readCommand() {
		String wholeCommand = accumulateParams();

		parseCommandAndArguments(wholeCommand);
	}

	/**
	 * Metoda koja iz predanog teksta ekstraktira naziv naredbe i argumente koji su
	 * sve ono što u tekstu nalazi iza naziva naredbe.
	 * 
	 * @param string tekst koji se parsira
	 * 
	 * @throws ShellParserException ako naziv naredbe nije naveden
	 */
	private void parseCommandAndArguments(String string) {
		string = string.trim();
		if (string.length() == 0) {
			throw new ShellParserException("There is no command name");
		}
		command = string.split("[ |\t]+")[0];
		arguments = string.substring(command.length());
	}

	/**
	 * Metoda koja učitava podatke i akumulira ih u StringBuilderu sve dok
	 * se na kraju retka nalazi znak {@link Environment#getMorelinesSymbol()}.
	 * Znakovi morelines ne ulaze u akumulirani string.
	 * 
	 * @return akumulirani string
	 */
	private String accumulateParams() {
		StringBuilder sb = new StringBuilder();
		String string = env.readLine();
		do {
			if (string.endsWith(env.getMorelinesSymbol().toString())) {
				sb.append(string.substring(0, string.length() - 1));
			} else {
				sb.append(string);
				break;
			}

			env.writeMultiline();
			string = env.readLine();
		} while (true);

		return sb.toString();
	}

	/**
	 * Getter za naziv naredbe
	 * 
	 * @return command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Getter za argumente naredbe
	 * 
	 * @return arguments
	 */
	public String getArguments() {
		return arguments;
	}
}
