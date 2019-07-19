package hr.fer.zemris.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu za izlistavanje sadržaja direktorija. Naredba
 * prima jedan argument koji mora biti staza do direktorija.
 * 
 * @author Ante Miličević
 *
 */
public class LsShellCommand implements ShellCommand {
	/**
	 * Format ispisa datuma stvaranja
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * Naziv naredbe
	 */
	private static final String name = "ls";
	/**
	 * Razmak komponenti u ispisu
	 */
	private static final String padding = " ";
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Lists files in directory", "Argument must be path to directory from which you want to list content",
				"Flags: d(directory) r(readable) w(writeable) x(executable)", "Date: creation time", "Name: name of the file"));
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
	 * @throws IOException              ako ne uspije čitanje sadržaja direktorija
	 * @throws OperationFailedException ako staza nije validna ili je naveden krivi
	 *                                  broj argumenata
	 * @throws IllegalArgumentException ako se string ne može protumačiti kao staza
	 *                                  ili ako podaci o datotekama nisu dostupni
	 * @throws ArgumentLexerException   ako argumenti nisu u dobrom formatu
	 */
	private void run(Environment env, String arguments) throws IOException {
		List<Path> list = CommandUtil.returnPaths(arguments, env);
		CommandUtil.checkTheNumberOfArguments(list, 1, name);
		
		Path dir = CommandUtil.verifyDirectory(list.get(0));

		try (Stream<Path> children = Files.list(dir)) {
			Iterator<Path> it = children.iterator();
			while (it.hasNext()) {
				env.writeln(returnFileData(it.next()));
			}
		}
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

	/**
	 * Pomoćna metoda za formatiranje podataka o datoteci/direktoriju
	 * 
	 * @param file datoteka/direktorij
	 * @return formatirani podaci
	 */
	private static String returnFileData(Path file) {
		BasicFileAttributeView view = Files.getFileAttributeView(file, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		StringBuilder sb = new StringBuilder();
		try {
			BasicFileAttributes attrs = view.readAttributes();
			sb.append(Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS) ? 'd' : '-');
			sb.append(Files.isReadable(file) ? 'r' : '-');
			sb.append(Files.isWritable(file) ? 'w' : '-');
			sb.append(Files.isExecutable(file) ? 'x' : '-');
			sb.append(padding);
			sb.append(String.format("%10s", Files.size(file)));
			sb.append(padding);
			FileTime fileTime = attrs.creationTime();
			sb.append(sdf.format(new Date(fileTime.toMillis())));
			sb.append(padding);
			sb.append(file.getFileName());

			return sb.toString();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
