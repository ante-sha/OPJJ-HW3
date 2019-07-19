package hr.fer.zemris.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import hr.fer.zemris.hw06.shell.ConfigurationConstants;
import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred modelira naredbu koja direktorij s vrha stoga postavlja za trenutni
 * direktorij okoline te ga zatim briše sa stoga.
 * 
 * @author Ante Miličević
 *
 */
public class PopdShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "popd";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Takes no arguments",
				"Pops directory from the top of the stack and set it as current directory in environment",
				"If directory is erased in between pushing and poping path is just removed from stack"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> strings = CommandUtil.tokenizeArguments(arguments);
			CommandUtil.checkTheNumberOfArguments(strings, 0, name);

			LinkedList<Path> paths = (LinkedList<Path>) env.getSharedData(ConfigurationConstants.directoryStackName);

			Path dir = paths.pop();

			if (Files.exists(dir, LinkOption.NOFOLLOW_LINKS)) {
				env.setCurrentDirectory(dir);
			} else {
				env.writeln("Directory '" + dir + "' was erased");
			}

		} catch (IllegalStateException | OperationFailedException | ArgumentLexerException e) {
			env.writeln(e.getMessage());
		} catch (NoSuchElementException e) {
			env.writeln("There are no directories on the stack");
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
