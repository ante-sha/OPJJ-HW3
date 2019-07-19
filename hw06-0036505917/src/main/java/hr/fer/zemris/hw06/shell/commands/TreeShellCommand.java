package hr.fer.zemris.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu za ispisivanje podstabla direktorija. Naredba
 * prima jedan argument koji mora biti staza do postojećeg direktorija. Ispis
 * podstabla se obavlja pomoću {@link FileVisitor}-a.
 * 
 * @author Ante Miličević
 *
 */
public class TreeShellCommand implements ShellCommand {
	/**
	 * Naziv naredbe
	 */
	private static final String name = "tree";

	private static final List<String> description;
	static {
		description = Arrays.asList("Prints the subtree of a given directory", "Argument must be path to directory");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			run(env, arguments);
		} catch (OperationFailedException | IllegalArgumentException | ArgumentLexerException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.writeln("Operation failed => " + e.getMessage());
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
	 * @throws IOException              ako ne uspije čitanje podstabla direktorija
	 * @throws OperationFailedException ako staza nije validna ili je naveden krivi
	 *                                  broj argumenata
	 * @throws IllegalArgumentException ako se string ne može protumačiti kao staza
	 * @throws ArgumentLexerException   ako argumenti nisu u dobrom formatu
	 */
	private void run(Environment env, String arguments) throws IOException {
		List<Path> list = CommandUtil.returnPaths(arguments);

		CommandUtil.checkTheNumberOfArguments(list, 1, name);

		Path path = CommandUtil.verifyDirectory(list.get(0));

		Files.walkFileTree(path, new TreeFileVisitor(env));

	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

	/**
	 * Privatni razred koji modelira FileVisitor-a koji ispisuje podstablo predanog
	 * direktorija. Svaka nova razina se nalazi na dva razmaka udaljena od
	 * prethodne.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class TreeFileVisitor implements FileVisitor<Path> {
		/**
		 * Trenutna razina
		 */
		private int level = 0;
		/**
		 * Okolina u kojoj se izvodi ispis podstabla
		 */
		private Environment env;

		/**
		 * Konstruktor s inicijalizacijom okoline.
		 * 
		 * @param env okolina u kojoj se izvodi ispis podstabla.
		 */
		public TreeFileVisitor(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			env.writeln(String.format("%s%s", " ".repeat(level * 2),
					level == 0 ? dir.toAbsolutePath().toString() : dir.getFileName()));
			level++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			env.writeln(String.format("%s%s", " ".repeat(level * 2), file.getFileName()));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			env.writeln(String.format("Permission denied => %s", file.getFileName()));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
		}

	}

}
