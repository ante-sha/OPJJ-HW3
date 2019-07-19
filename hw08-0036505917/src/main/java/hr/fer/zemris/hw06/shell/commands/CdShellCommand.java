package hr.fer.zemris.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred modelira naredbu koja mijenja trenutni direktorij na temelju kojeg se
 * razrješavaju relativne staze u naredbama. Naredba prima jedan argument koji
 * mora biti staza do postojećeg direktorija.
 * 
 * @author Ante Miličević
 *
 */
public class CdShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "cd";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(
				Arrays.asList("Command takes one argument and that argument must be path to existing directory",
						"That directory will become current directory of an operating environment",
						"All relative paths will be resolved with current directory"));
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<Path> paths = CommandUtil.returnPaths(arguments, env);
			CommandUtil.checkTheNumberOfArguments(paths, 1, name);

			env.setCurrentDirectory(paths.get(0));
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
