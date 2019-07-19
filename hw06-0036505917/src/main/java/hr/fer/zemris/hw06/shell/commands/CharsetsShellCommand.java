package hr.fer.zemris.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu za ispisivanje svih podržanih charseta na
 * izlazni stream. Naredba ne prima argumente.
 * 
 * @author Ante Miličević
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	/**
	 * Ime naredbe
	 */
	private static final String name = "charsets";
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Arrays.asList("Lists available charsets on this JVM"
				+ "Takes no arguments");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			run(env, arguments);
		} catch (ArgumentLexerException | OperationFailedException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna privatna metoda koja pokreće i izvodi operaciju, dok se upravljanje
	 * iznimaka vrši u metodi
	 * {@link CharsetsShellCommand#executeCommand(Environment, String)}.
	 * 
	 * @param env       okolina ljuske
	 * @param arguments argumenti naredbe
	 * 
	 * @throws OperationFailedException ako je naveden krivi broj argumenata
	 */
	private void run(Environment env, String arguments) {
		List<String> params = CommandUtil.tokenizeArguments(arguments);

		CommandUtil.checkTheNumberOfArguments(params, 0, name);

		Charset.availableCharsets().keySet().forEach(name -> env.writeln(name));
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

}
