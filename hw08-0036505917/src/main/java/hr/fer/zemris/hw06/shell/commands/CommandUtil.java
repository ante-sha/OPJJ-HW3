package hr.fer.zemris.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexer;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;
import hr.fer.zemris.hw06.shell.commands.lexer.Token;

/**
 * Pomoćni zajednički razred namjenjen za skladištenje zajedničkih metoda koje
 * su korisne pri interpretaciji argumenata i daljnjem operiranju naredbe.
 * 
 * @author Ante Miličević
 *
 */
public class CommandUtil {

	/**
	 * Metoda koja oklanja navodnike sa stringa ako isti postoje na početku i na
	 * kraju.
	 * 
	 * @param argument string za obradu
	 * @return string bez početnog i završnog znaka '"'
	 */
	public static String returnStringWithoutQuotes(String argument) {
		if (argument.matches("\".*\"")) {
			return argument.substring(1, argument.length() - 1);
		}
		return argument;
	}

	/**
	 * Metoda za bacanje iznimke do koje dolazi ako argumenti nisu dobro definirani.
	 * 
	 * @param name ime naredbe
	 */
	public static void operationFailed(String name) {
		throw new OperationFailedException("Wrong definition of command " + name);
	}

	/**
	 * Metoda koja tokenizira argumente i vraća ih poredane u listi.
	 * 
	 * @param arguments argumenti naredbe
	 * @return argumente poredane u listi
	 * 
	 * @throws ArgumentLexerException ako argumenti nisu u dobrom formatu
	 */
	public static List<String> tokenizeArguments(String arguments) {
		ArgumentLexer lexer = new ArgumentLexer(arguments);
		List<String> result = new ArrayList<>();
		Token token = null;
		token = lexer.nextToken();
		while (token.getType() != Token.TokenType.EOL) {
			result.add(returnStringWithoutQuotes(token.getValue()));
			token = lexer.nextToken();
		}

		return result;
	}

	/**
	 * Metoda koja tokenizira argumente pomoću {@link #tokenizeArguments(String)} i
	 * onda ih pretvara u staze. Relativne staze se razrješavaju pomoću
	 * {@link Environment#getCurrentDirectory()}
	 * 
	 * @param arguments argumenti naredbe
	 * @param env       okolina u kojoj se čita nareba
	 * @return lista staza
	 * 
	 * @throws InvalidPathException   ako se string ne može konvertirati u stazu
	 * @throws ArgumentLexerException ako argumenti nisu u dobrom stanju
	 */
	public static List<Path> returnPaths(String arguments, Environment env) {
		List<String> list = tokenizeArguments(arguments);

		return list.stream().map((stringPath) -> returnNormalizedPath(stringPath, env)).collect(Collectors.toList());
	}

	/**
	 * Metoda koja stvara ulazni stream iz predane staze.
	 * 
	 * @param path staza do datoteke nad kojom otvaramo stream
	 * @return input stream
	 * 
	 * @throws IOException ako otvaranje datoteke ne uspije
	 */
	public static BufferedInputStream createInputStream(Path path) throws IOException {
		return new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ));
	}

	/**
	 * Metoda koja provjerava da li se na odredištu staze nalazi file
	 * 
	 * @param path provjeravana staza
	 * @return provjerena staza
	 * 
	 * @throws OperationFailedExceptio ako staza ne vodi do datoteke
	 */
	public static Path verifyFile(Path path) {
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			throw new OperationFailedException("Path '" + path + "' leads to directory");
		}
		if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
			throw new OperationFailedException("Path '" + path + "' do not exist");
		}
		return path;
	}

	/**
	 * Metoda koja provjerava da li se odredištu staze nalazi direktorij
	 * 
	 * @param path provjeravana staza
	 * @return provjerena staza
	 * 
	 * @throws OperationFailedException ako staza ne vodi do direktorija
	 */
	public static Path verifyDirectory(Path path) {
		if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			throw new OperationFailedException("Path '" + path + "' do not lead to directory");
		}
		return path;
	}

	/**
	 * Metoda koja provjerava da li je broj argumenata u listi unutar raspona
	 * {@code [min,max]}
	 * 
	 * @param list lista koja sadrži argumente
	 * @param min  minimalan broj argumenata
	 * @param max  maksimalan broj argumenata
	 * @param name ime naredbe
	 * 
	 * @throws OperationFailedException ako broj argumenata nije zadovoljen
	 */
	public static <T> void checkTheNumberOfArguments(List<T> list, int min, int max, String name) {
		int n = list.size();
		if (n < min || n > max) {
			operationFailed(name);
		}
	}

	/**
	 * Metoda koja provjerava da li je broj argumenata u listi jednak {@code n}
	 * 
	 * @param list lista koja sadrži argumente
	 * @param n    broj argumenata
	 * @param name ime naredbe
	 * 
	 * @throws OperationFailedException ako broj argumenata nije zadovoljen
	 */
	public static <T> void checkTheNumberOfArguments(List<T> list, int n, String name) {
		checkTheNumberOfArguments(list, n, n, name);
	}

	/**
	 * Metoda koja vraća normaliziranu stazu, sve relativne staze razješava preko
	 * {@link Environment#getCurrentDirectory()}
	 * 
	 * @param stringPath staza koju se razrješava
	 * @param env        okolina u kojoj se čita naredba
	 * @return normalizirana staza
	 */
	public static Path returnNormalizedPath(String stringPath, Environment env) {
		return env.getCurrentDirectory().resolve(Paths.get(stringPath)).normalize();
	}
}
