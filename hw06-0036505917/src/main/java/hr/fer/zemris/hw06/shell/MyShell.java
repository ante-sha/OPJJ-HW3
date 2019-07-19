package hr.fer.zemris.hw06.shell;

import java.util.Scanner;
import java.util.SortedMap;

import hr.fer.zemris.hw06.shell.parser.ShellParser;
import hr.fer.zemris.hw06.shell.parser.ShellParserException;

/**
 * Program koji obnaša rad ljuske koja u komunikaciji s {@link Environment}-om i
 * njegovim naredbama {@link ShellCommand} izvršava predanu naredbu.
 * 
 * @author Ante Miličević
 * @version 1.0
 *
 */
public class MyShell {

	/**
	 * Ulazna točka programa.
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			Environment env = new EnvironmentImpl(sc);
			SortedMap<String, ShellCommand> commands = env.commands();

			env.writeln(helloMessage());

			ShellStatus status = ShellStatus.CONTINUE;
			ShellParser parser = new ShellParser(env);
			
			do {
				env.writePrompt();
				try {
					parser.readCommand();
				} catch (ShellParserException e) {
					env.writeln("Error => " + e.getMessage());
					continue;
				}

				String commandName = parser.getCommand();

				if (!commands.containsKey(commandName)) {
					commandNotFound(commandName, env);
				} else {
					status = commands.get(commandName).executeCommand(env, parser.getArguments());
				}

			} while (status == ShellStatus.CONTINUE);
			
			env.writeln(goodbyeMessage());
		} catch (ShellIOException e) {
			System.out.println("Something went wrong => " + e.getMessage());
		}

	}
	
	/**
	 * Metoda koja vraća poruku koja se ispisuje pri izlasku iz shell-a
	 * 
	 * @return poruka
	 */
	private static String goodbyeMessage() {
		return "Goodbye!";
	}

	/**
	 * Metoda koja vraća poruku koja se ispisuje pri pokretanju shell-a
	 * 
	 * @return poruka
	 */
	private static String helloMessage() {
		return "Welcome to " + ConfigurationConstants.shellName;
	}

	/**
	 * Metoda koja ispisuje poruku ako unešena naredba ne postoji
	 * 
	 * @param commandName ime naredba
	 * @param env trenutna okolina ljuske
	 */
	private static void commandNotFound(String commandName, Environment env) {
		env.writeln("Command not found");
	}
}
