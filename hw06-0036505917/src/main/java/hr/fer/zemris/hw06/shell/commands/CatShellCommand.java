package hr.fer.zemris.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu za ispisivanje datoteke na izlazni stream, uz
 * stazu datoteke za ispis moguće je predati i charset za dekodiranje datoteke.
 * 
 * @author Ante Miličević
 *
 */
public class CatShellCommand implements ShellCommand {
	/**
	 * Naziv naredbe
	 */
	private static final String name = "cat";
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Arrays.asList("Print files on the screen", "First argument is path to the file",
				"Second argument is arbitrary and represents charset to interpret file",
				"If second argument is ommited UTF-8 is default charset",
				"-> see command \"charsets\" for available charsets");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			run(env, arguments);
		} catch (IOException e) {
			env.writeln("Invalid path to file => " + e.getMessage());
		} catch (ArgumentLexerException | OperationFailedException e) {
			env.writeln(e.getMessage());
		} catch (IllegalArgumentException e) {
			env.writeln("Undefined encoding page " + e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna privatna metoda koja pokreće i izvodi operaciju, dok se upravljanje
	 * iznimaka vrši u metodi
	 * {@link CatShellCommand#executeCommand(Environment, String)}.
	 * 
	 * @param env       okolina ljuske
	 * @param arguments argumenti naredbe
	 * 
	 * @throws IOException              ako ne uspije čitanje datoteke
	 * @throws OperationFailedException ako staza nije validna ili je naveden krivi
	 *                                  broj argumenata
	 * @throws IllegalArgumentException ako charset predan u argumentu ne postoji
	 */
	private void run(Environment env, String arguments) throws IOException {
		List<String> params = CommandUtil.tokenizeArguments(arguments);

		CommandUtil.checkTheNumberOfArguments(params, 1, 2, name);

		Path path = Paths.get(params.get(0));
		CommandUtil.verifyFile(path);

		Charset cs = null;
		if (params.size() == 2) {
			cs = Charset.forName(params.get(1));
		} else {
			cs = Charset.defaultCharset();
		}
		try (BufferedReader input = Files.newBufferedReader(path, cs)) {
			while (true) {
				String s = input.readLine();
				if (s == null)
					break;
				env.writeln(s);
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
