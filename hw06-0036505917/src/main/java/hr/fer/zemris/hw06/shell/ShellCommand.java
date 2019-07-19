package hr.fer.zemris.hw06.shell;

import java.util.List;

/**
 * Sučelje koje definira naredbe ljuske koje svoju funkcionalnost ostvaruju kroz
 * okolinu {@link Environment}. Svaka naredba mora imati svoje ime i opis koje
 * predaje kao povratne vrijednosti pripadajućih metoda.
 * 
 * @author Ante Miličević
 *
 */
public interface ShellCommand {
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param env       okolina u kojoj se naredba izvršava
	 * @param arguments argumenti naredbe
	 * 
	 * @return status sljedećeg koraka ljuske
	 * 
	 * @throws ShellIOException ako se dogodi greška u komunikaciji s korisnikom
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Getter za ime naredbe
	 * 
	 * @return commandName
	 */
	String getCommandName();

	/**
	 * Metoda koje vraća opis naredbe u listi. Svaki član liste predstavlja jedan
	 * redak opisa.
	 * 
	 * @return commandDescription
	 */
	List<String> getCommandDescription();
}
