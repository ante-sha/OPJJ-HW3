package hr.fer.zemris.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu kopiranja. Naredba prima 2 argumenta koja
 * predstavljaju staze do izvorne i odredišne datoteke.<br>
 * Ako je druga staza već postojeća otvara se dialog u kojem korisnik
 * potvrđuje/odbija umetanje kopiranog dokumenta na to mjesto.<br>
 * Ako je druga staza postojeći direktorij tada se unutar tog direktorija stvara
 * datoteka s istim imenom kao i izvorna datoteka.
 * 
 * @author Ante Miličević
 *
 */
public class CopyShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "copy";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Arrays.asList("Copy the source file in destination file",
				"First argument must be path to the file",
				"Second argument must be path to the existing directiory or file in existing directory",
				"If file with given path already exists dialog would be established to determine do you want to overwrite existing file");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		try {
			run(env, arguments);
		} catch (IOException e) {
			env.writeln("File error => " + e.getMessage());
		} catch (IllegalArgumentException | ArgumentLexerException | OperationFailedException e) {
			env.writeln(e.getMessage());
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
	 * @throws IOException              ako ne uspije čitanje izvorne datoteke
	 * @throws OperationFailedException ako staze nije validna ili je naveden krivi
	 *                                  broj argumenata
	 * @throws IllegalArgumentException ako se string ne može protumačiti kao staza
	 *                                  ili ako su izvorna i odredišna staza iste
	 * @throws ArgumentLexerException   ako argumenti nisu u dobrom formatu
	 */
	private void run(Environment env, String arguments) throws IOException {
		List<Path> paths = CommandUtil.returnPaths(arguments);

		CommandUtil.checkTheNumberOfArguments(paths, 2, name);

		Path path1 = CommandUtil.verifyFile(paths.get(0));
		Path path2 = paths.get(1);

		if (Files.isDirectory(path2, LinkOption.NOFOLLOW_LINKS)) {
			path2 = Paths.get(path2.toAbsolutePath().toString(), path1.getFileName().toString());
		}

		if (path1.equals(path2)) {
			throw new IllegalArgumentException("'" + path1 + "' and '" + path2 + "' are same path!");
		}
		
		if (Files.exists(path2, LinkOption.NOFOLLOW_LINKS)) {
			if (!overwriteDialog(env)) {
				return;
			}
		}
		
		copy(path1, path2);
		env.writeln("Copying finished successfully");
	}

	/**
	 * Metoda koja odrađuje dijalog s korisnikom u vezi umetanja kopije na mjesto
	 * postojeće datoteke sve dok njegov odgovor nije potvrdan ili negativan.
	 * 
	 * @param env okolina u kojoj se izvršava naredba
	 * @return true ako je odgovor potvrdan, false ako je negativan
	 */
	private boolean overwriteDialog(Environment env) {
		env.writeln("File with this name already exist, do you want to overwrite it [y/n]");
		String answer = null;
		do {
			env.writePrompt();
			answer = env.readLine().trim();
		} while (!(answer.equals("y") || answer.equals("n")));

		if (answer.equals("n"))
			return false;
		else
			return true;
	}

	/**
	 * Metoda koja kopira sadržaj datoteke koja se nalazi na stazi {@code path1} u
	 * datoteku koja se nalazi na stazi {@code path2}.
	 * 
	 * @param path1 izvorna datoteka
	 * @param path2 odredišna datoteka
	 * 
	 * @throws IOException ako dođe do greške prilikom čitanja/pisanja
	 */
	private void copy(Path path1, Path path2) throws IOException {
		try (BufferedInputStream input = CommandUtil.createInputStream(path1);
				BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(path2,
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
			byte[] buffer = new byte[1024];
			while (true) {
				int r = input.read(buffer);
				if (r <= 0)
					break;
				output.write(buffer, 0, r);
			}
		} catch (IOException e) {
			throw new IOException("Copying failed => " + e.getMessage());
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
