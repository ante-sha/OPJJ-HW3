package hr.fer.zemris.hw06.shell.commands;

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
 * Razred modelira naredbu koja sa stoga staza direktorija briše onu sa vrha.
 * 
 * @author Ante Miličević
 *
 */
public class DropdShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "dropd";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Takes no arguments",
				"Command deletes last path to directory that was pushed to the stack"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			CommandUtil.checkTheNumberOfArguments(CommandUtil.tokenizeArguments(arguments), 0, name);

			LinkedList<Path> paths = (LinkedList<Path>) env.getSharedData(ConfigurationConstants.directoryStackName);

			paths.pop();
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
