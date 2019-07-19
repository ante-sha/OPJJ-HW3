package hr.fer.zemris.java.hw17.prob1.shell.commands;

import java.io.PrintStream;

import hr.fer.zemris.java.hw17.prob1.shell.ShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommandStatus;
import hr.fer.zemris.java.hw17.prob1.shell.ShellException;

/**
 * Naredba za izlazak iz ljuske.
 * 
 * @author Ante Miličević
 *
 */
public class ExitShellCommand implements ShellCommand {

	@Override
	public ShellCommandStatus execute(String arguments, PrintStream printer) throws ShellException {
		if(!arguments.trim().equals("")) {
			throw new ShellException("Exit command takes no arguments");
		}
		
		printer.println("Goodbye!");
		
		return ShellCommandStatus.TERMINATE;
	}

}
