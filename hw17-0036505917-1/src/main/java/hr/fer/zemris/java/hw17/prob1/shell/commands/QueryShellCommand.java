package hr.fer.zemris.java.hw17.prob1.shell.commands;

import static hr.fer.zemris.java.hw17.prob1.shell.commands.CommandUtil.calculateCos;
import static hr.fer.zemris.java.hw17.prob1.shell.commands.CommandUtil.printResults;

import java.io.PrintStream;
import java.util.Set;

import hr.fer.zemris.java.hw17.prob1.data.Query;
import hr.fer.zemris.java.hw17.prob1.shell.Shell;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommandStatus;
import hr.fer.zemris.java.hw17.prob1.shell.ShellException;

/**
 * Naredba query za niz riječi predan u argumentu gradi vektor koji je usporediv
 * s vektorima dokumentata i tada na temelju uspoređivanja tih vektora naredba
 * vraća rezultate. Ocjena sličnosti se računa pomoću kosinusa kuta između
 * vektora.
 * 
 * @author Ante Miličević
 *
 */
public class QueryShellCommand implements ShellCommand {

	@Override
	public ShellCommandStatus execute(String arguments, PrintStream printer) throws ShellException {
		Query query = new Query(arguments, Shell.dData.getVocabulary());
		query.buildVector(Shell.dData);

		Shell.documents.sort((d1, d2) -> Double.compare(calculateCos(d2.getVector(), query.getVector()),
				calculateCos(d1.getVector(), query.getVector())));
		Shell.lastQuery = query;

		printHeader(query, printer);
		printResults(query, printer);

		return ShellCommandStatus.CONTINUE;
	}

	/**
	 * Ispis zaglavlja
	 * 
	 * @param query upit
	 * @param printer objekt za ispis
	 */
	private void printHeader(Query query, PrintStream printer) {
		Set<String> words = query.getWordCount().keySet();

		printer.print("Query is: [");

		boolean first = true;
		for (String word : words) {
			if (first) {
				first = false;
			} else {
				printer.print(", ");
			}
			printer.print(word);
		}
		printer.println("]");
	}

}
