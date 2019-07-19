package hr.fer.zemris.java.hw17.prob1.shell;

/**
 * Status preko kojeg naredba ljusci daje povratnu informaciju
 * o tijeku izvođenja programa.
 * 
 * @author Ante Miličević
 *
 */
public enum ShellCommandStatus {
	/**
	 * Nastavi s izvođenjem
	 */
	CONTINUE, 
	/**
	 * Prekini izvođenje
	 */
	TERMINATE;
}
