package hr.fer.zemris.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu za izlazak iz ljuske.
 * 
 * @author Ante Miličević
 *
 */
public class ExitShellCommand implements ShellCommand {
	/**
	 * Ime naredbe
	 */
	private static final String name = "exit";
	
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Exits the shell",
				"Takes no arguments"));
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			CommandUtil.checkTheNumberOfArguments(CommandUtil.tokenizeArguments(arguments), 0, name);
		} catch (ArgumentLexerException | OperationFailedException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
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
