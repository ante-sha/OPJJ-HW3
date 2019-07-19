package hr.fer.zemris.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred modelira naredbu koja ispisuje trenutni direktorij okoline.
 * 
 * @author Ante Miličević
 *
 */
public class PwdShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "pwd";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Takes no arguments", "Prints working directory"));
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> params = CommandUtil.tokenizeArguments(arguments);
			CommandUtil.checkTheNumberOfArguments(params, 0, name);

			env.writeln(env.getCurrentDirectory().toString());
		} catch (IllegalArgumentException | ArgumentLexerException | OperationFailedException e) {
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
