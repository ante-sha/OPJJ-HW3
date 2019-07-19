package hr.fer.zemris.java.hw17.prob1.shell.commands;

import java.io.PrintStream;

import hr.fer.zemris.java.hw17.prob1.shell.Shell;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommandStatus;
import hr.fer.zemris.java.hw17.prob1.shell.ShellException;

/**
 * Naredba result koja ispisuje 10 najboljih rezultata ili onoliko koliko ih
 * ima.
 * 
 * @author Ante Miličević
 *
 */
public class ResultsShellCommand implements ShellCommand {

	@Override
	public ShellCommandStatus execute(String arguments, PrintStream printer) throws ShellException {
		if (!arguments.trim().equals("")) {
			throw new ShellException("Results command takes no arguments");
		}
		if (Shell.lastQuery == null) {
			throw new ShellException("Query has not been placed yet");
		}
		
		CommandUtil.printResults(Shell.lastQuery, printer);
		
		return ShellCommandStatus.CONTINUE;
	}

}
