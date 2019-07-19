package hr.fer.zemris.hw06.shell;

import static hr.fer.zemris.hw06.shell.ConfigurationConstants.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.hw06.shell.commands.*;

/**
 * Implementacija sučelja {@link Environment} koja sadrži sve naredbe
 * {@link ShellCommand} koje su dopuštene trenutnom korisniku, objekt
 * {@link Scanner} preko kojeg ostvaruje komunikaciju sa korisnikom, te posebne
 * znakove za komunikaciju sa korisnikom.
 * 
 * @author Ante Miličević
 *
 */
public class EnvironmentImpl implements Environment {
	/**
	 * Objekt pomoću kojeg se ostvaruje komunikacija s korisnikom
	 */
	private Scanner sc;
	/**
	 * Mapa koja sadrži sve naredbe i njihova imena koje su dopuštene korisniku
	 */
	private SortedMap<String, ShellCommand> commands = new TreeMap<>();
	/**
	 * Mapa koja sadrži sve posebne simbole
	 */
	private Map<String, Character> symbols = new HashMap<>();

	/**
	 * Konstuktor koji inicijalizira sve naredbe, posebne znakove i sredstvo
	 * komunikacije s korisnikom koje postavlja na {@link Scanner} predan u
	 * argumentu
	 * 
	 * @param sc sredstvo komunikacije s korisnikom
	 */
	public EnvironmentImpl(Scanner sc) {
		this.sc = Objects.requireNonNull(sc);
		commands.put("ls", new LsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		symbols.put(prompt, defaultPrompt);
		symbols.put(multiline, defaultMultiline);
		symbols.put(morelines, defaultMorelines);
	}

	@Override
	public String readLine() throws ShellIOException {
		try {
			return sc.nextLine();
		} catch (Exception e) {
			throw new ShellIOException(e.getMessage());
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		try {
			System.out.print(text);
		} catch (Exception e) {
			throw new ShellIOException(e.getMessage());
		}
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		try {
			System.out.println(text);
		} catch (Exception e) {
			throw new ShellIOException(e.getMessage());
		}

	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return symbols.get(multiline);
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		symbols.put(multiline, Objects.requireNonNull(symbol));
	}

	@Override
	public Character getPromptSymbol() {
		return symbols.get(prompt);
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		symbols.put(prompt, Objects.requireNonNull(symbol));
	}

	@Override
	public Character getMorelinesSymbol() {
		return symbols.get(morelines);
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		symbols.put(morelines, Objects.requireNonNull(symbol));
	}

	@Override
	public void writePrompt() {
		write(getPromptSymbol() + " ");
	}

	@Override
	public void writeMultiline() {
		write(getMultilineSymbol() + " ");
	}
}
