package hr.fer.zemris.java.hw17.prob1.shell;

import java.io.PrintStream;

/**
 * Sučelje koje definira naredbu koju koristi {@link Shell}.
 * @author Ante Miličević
 *
 */
public interface ShellCommand {
	/**
	 * Metoda za izvršavanje naredbe
	 * 
	 * @param arguments argumenti
	 * @param printer objekt za ispis
	 * @return status
	 * 
	 * @throws ShellException ako izvođenje naredbe ne uspije
	 */
	public ShellCommandStatus execute(String arguments, PrintStream printer) throws ShellException;
}
