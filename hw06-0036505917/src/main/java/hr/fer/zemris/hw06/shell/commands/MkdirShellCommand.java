package hr.fer.zemris.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu koja stvara novi direktorij. Naredba prima točno
 * jedan argument koji je staza do željenog mjesta kreiranja direktorija. Ako se
 * na putu kreiranja direktorija nalazi istoimena datoteka kao i trenutno
 * zahtjevani direktorij za kreiranje operacija se prekida.
 * 
 * @author Ante Miličević
 *
 */
public class MkdirShellCommand implements ShellCommand {
	/**
	 * Naziv naredbe
	 */
	private static final String name = "mkdir";
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Arrays.asList("Creates unexisting directories of the argument", 
				"Argument is path which you want to create");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			run(env, arguments);
		} catch (FileAlreadyExistsException e) {
			env.writeln("File error => File with the same name exists");
		} catch (IllegalArgumentException | ArgumentLexerException | OperationFailedException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.write("File error => " + e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna privatna metoda koja pokreće i izvodi operaciju, dok se upravljanje
	 * iznimaka vrši u metodi {@link #executeCommand(Environment, String)}.
	 * 
	 * @param env       okolina ljuske
	 * @param arguments argumenti naredbe
	 * 
	 * @throws FileAlreadyExists        ako se na stazi nalazi istoimena datoteka
	 *                                  kao i direktorij kojeg se trenutno na toj
	 *                                  razini stvara
	 * @throws OperationFailedException ako se argument ne može protumačiti kao
	 *                                  staza ili je naveden krivi broj argumenata
	 */
	public void run(Environment env, String arguments) throws IOException {
		List<Path> paths = CommandUtil.returnPaths(arguments);

		CommandUtil.checkTheNumberOfArguments(paths, 1, name);

		Path directory = paths.get(0);
		Files.createDirectories(directory);
		
		env.writeln("Directories created successfully!");
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
