package hr.fer.zemris.hw06.shell;

/**
 * Enumeracija kojom se kroz povratne vrijednosti izvršenih naredbi javlja idući
 * očekivani korak ljuske.
 * 
 * @author Ante Miličević
 *
 */
public enum ShellStatus {
	/**
	 * Nastavak rada
	 */
	CONTINUE,
	/**
	 * Prekid rada
	 */
	TERMINATE;
}
