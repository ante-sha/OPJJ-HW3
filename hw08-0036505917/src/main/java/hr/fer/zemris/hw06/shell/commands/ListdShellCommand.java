package hr.fer.zemris.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.ConfigurationConstants;
import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred modelira naredbu koja ispisuje listu direktorija koji se nalaze na
 * stogu počev od vrha stoga.
 * 
 * @author Ante Miličević
 *
 */
public class ListdShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "listd";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Takes no arguments",
				"List the directories on the stack where the first is on the top of the stack"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> strings = CommandUtil.tokenizeArguments(arguments);
			CommandUtil.checkTheNumberOfArguments(strings, 0, name);

			List<Path> paths = (List<Path>) env.getSharedData(ConfigurationConstants.directoryStackName);

			if (paths == null || paths.size() == 0) {
				throw new IllegalStateException("There are no directories on the stack");
			}

			paths.forEach((path) -> env.writeln(path.toString()));

		} catch (IllegalStateException | OperationFailedException | ArgumentLexerException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

}
