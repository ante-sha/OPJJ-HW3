package hr.fer.zemris.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.hw06.shell.ConfigurationConstants;
import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred modelira naredbu koja prima jedan argument koji mora biti staza do
 * postojećeg direktorija. Pri izvršavanju naredbe trenutni direktorij okoline
 * se stavlja na stog, a direktorij predan u argumentu postaje trenutni
 * direktorij okoline.
 * 
 * @author Ante Miličević
 *
 */
public class PushdShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "pushd";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Takes one argument that must be existing directory",
				"Pushes the current directory to the stack and sets current directory to the one from argument"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<Path> paths = CommandUtil.returnPaths(arguments, env);
			CommandUtil.checkTheNumberOfArguments(paths, 1, name);

			Path newDir = CommandUtil.verifyDirectory(paths.get(0));

			LinkedList<Path> cdstack = (LinkedList<Path>) env.getSharedData(ConfigurationConstants.directoryStackName);

			//if stack don't exist create one
			if (cdstack == null) {
				cdstack = new LinkedList<Path>();
				env.setSharedData(ConfigurationConstants.directoryStackName, cdstack);
			}

			cdstack.push(env.getCurrentDirectory());

			env.setCurrentDirectory(newDir);
		} catch (IllegalArgumentException | OperationFailedException | ArgumentLexerException e) {
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
