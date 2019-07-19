package hr.fer.zemris.java.hw17.prob1.shell.commands;

import static hr.fer.zemris.java.hw17.prob1.shell.commands.CommandUtil.calculateCos;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import hr.fer.zemris.java.hw17.prob1.shell.Shell;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommandStatus;
import hr.fer.zemris.java.hw17.prob1.shell.ShellException;

/**
 * Naredba type koja ispisuje rezultat s indeksom koji je predan u argumentu naredbe.
 * 
 * @author Ante Miličević
 */
public class TypeShellCommand implements ShellCommand {

	@Override
	public ShellCommandStatus execute(String arguments, PrintStream printer) throws ShellException {
		int n = 0;
		try {
			n = Integer.parseInt(arguments.trim());
		} catch(NumberFormatException e) {
			throw new ShellException("Wrong arguments");
		}
		if(n >= Shell.documents.size() || n < 0) {
			throw new ShellException("Index out of range");
		}
		
		if(Shell.lastQuery == null || calculateCos(Shell.lastQuery.getVector(), Shell.documents.get(n).getVector()) < 1E-6) {
			throw new ShellException("Query did not gave result with index " + n);
		}
		
		try {
			Path pathToFile = Shell.documents.get(n).getPathToFile();
			printLine(printer);
			printer.printf("Dokument: %s%n", pathToFile.normalize().toAbsolutePath());
			printLine(printer);
			Files.readAllLines(pathToFile, StandardCharsets.UTF_8)
				.stream().forEach(printer::println);
			printLine(printer);
		} catch (IOException e) {
			throw new ShellException(e);
		}
		return ShellCommandStatus.CONTINUE;
	}

	/**
	 * Metoda za ispis graničnika tekstnog dokumenta
	 * 
	 * @param printer objekt za ispis teksta
	 */
	private void printLine(PrintStream printer) {
		printer.println("----------------------------------------------------------------");
	}

}
