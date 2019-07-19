package hr.fer.zemris.hw06.shell;

import java.util.SortedMap;

/**
 * Sučelje koje definira okolinu kojom se služi {@link MyShell} i sve njegove
 * podržane naredbe koje su oblika {@link ShellCommand}.
 * 
 * @author Ante Miličević
 *
 */
public interface Environment {
	/**
	 * Metoda koja čita redak u komunikaciji sa korisnikom.
	 * 
	 * @return pročitana linije
	 * 
	 * @throws ShellIOException ako čitanje ne uspije
	 */
	String readLine() throws ShellIOException;

	/**
	 * Metoda koja korisniku ispisuje tekst predan u argumentu.
	 * 
	 * @param text tekst koji se ispisuje korisniku
	 * 
	 * @throws ShellIOException ako ispisivanje ne uspije
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Metoda koja korisniku ispisuje tekst predan u argumentu i znak '\n' iza
	 * njega.
	 * 
	 * @param text tekst koji se uz '\n' ispisuje korisniku
	 * 
	 * @throws ShellIOException ako ispisivanje ne uspije
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Metoda koja vraća nepromjenjivu mapu koja pod ključevima sadrži nazive
	 * naredbi, a pod vrijednostima {@link ShellCommand} tj. njihove implementacije
	 * 
	 * @return nepromjenjiva mapa naredbi
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Metoda koja vraća simbol koji se ispisuje na ekranu ako se argumenti naredbe
	 * pišu kroz više redaka
	 * 
	 * @return simbol multiline
	 */
	Character getMultilineSymbol();

	/**
	 * Metoda koja postavlja simbol koji se ispisuje na ekranu ako se argumenti
	 * naredbe pišu kroz više redaka
	 * 
	 * @param symbol novi simbol
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Metoda koja vraća simbol koji se ispisuje na ekranu prije svakog upisivanja
	 * naredbe
	 * 
	 * @return simbol prompt
	 */
	Character getPromptSymbol();

	/**
	 * Metoda koja postavlja simbol koji se ispisuje na ekranu prije svakog
	 * upisivanja naredbe
	 * 
	 * @param symbol novi simbol
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Metoda koja vraća simbol kojeg je potrebno pisati na kraju retka ako se želi
	 * napisati naredbu kroz više redaka
	 * 
	 * @return simbol morelines
	 */
	Character getMorelinesSymbol();

	/**
	 * Metoda koja postavlja simbol kojeg je potrebno pisati na kraju retka ako se želi
	 * napisati naredbu kroz više redaka
	 * 
	 * @param symbol novi simbol
	 */
	void setMorelinesSymbol(Character symbol);

	/**
	 * Metoda koja ispisuje prompt simbol i razmak
	 * 
	 * @throws ShellIOException ako ispisivanje ne uspije
	 */
	void writePrompt() throws ShellIOException;

	/**
	 * Metoda koja ispisuje multiline simbol i razmak
	 * 
	 * @throws ShellIOException ako ispisivanje ne uspije
	 */
	void writeMultiline() throws ShellIOException;
}
