package hr.fer.zemris.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.hw06.shell.ConfigurationConstants;
import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira help naredbu. Ako naredba primi jedan argument koji je
 * ime druge naredbe tada se ispisuje njen opis, a ako naredba ne prima argument
 * tada se ispisuje popis svih naredbi.
 * 
 * @author Ante Miličević
 *
 */
public class HelpShellCommand implements ShellCommand {
	/**
	 * Ime naredbe
	 */
	private static final String name = "help";
	
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Arrays.asList(
				String.format("Command represents command manual for %s", ConfigurationConstants.shellName),
				"There are two ways of using it", "First: with no arguments you get a list of avalable commands",
				"Second: with one argument which is command name you get a description of command");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			run(env, arguments);
		} catch (ArgumentLexerException | IllegalArgumentException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna privatna metoda koja pokreće i izvodi operaciju, dok se upravljanje
	 * iznimaka vrši u metodi
	 * {@link HelpShellCommand#executeCommand(Environment, String)}.
	 * 
	 * @param env       okolina ljuske
	 * @param arguments argumenti naredbe
	 * 
	 * @throws OperationFailedException ako je naveden krivi broj argumenata
	 * @throws IllegalArgumentException ako naredba s predanim imenom ne postoji
	 */
	private void run(Environment env, String arguments) {
		SortedMap<String, ShellCommand> commands = env.commands();
		arguments = arguments.trim();

		List<String> params = CommandUtil.tokenizeArguments(arguments);

		CommandUtil.checkTheNumberOfArguments(params, 0, 1, name);

		if (params.size() == 0) {
			env.writeln("List of available commands:");
			commands.keySet().stream().forEach((name) -> env.writeln(name));

		} else {
			String commandName = params.get(0);
			if (commands.containsKey(commandName)) {
				ShellCommand command = commands.get(commandName);
				env.writeln("Command name: " + command.getCommandName());

				command.getCommandDescription().stream().forEach((row) -> env.writeln(row));
			} else {
				throw new IllegalArgumentException("Command " + commandName + " not found");
			}
		}
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
