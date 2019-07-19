package hr.fer.zemris.java.hw17.prob1.shell;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw17.prob1.data.Document;
import hr.fer.zemris.java.hw17.prob1.data.DocumentsData;
import hr.fer.zemris.java.hw17.prob1.data.Query;
import hr.fer.zemris.java.hw17.prob1.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.commands.QueryShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.commands.ResultsShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.commands.TypeShellCommand;

/**
 * Aplikacija koja pomoću modela "Bag of words" traži sličnost između zadanog
 * upita i dokumenata spremljenih u direktoriju predanom kao argument pri
 * pokretanju aplikacije.
 * <ul>
 * Naredbe
 * <li>"query [words]" - pokretanje upita</li>
 * <li>"type num" - ispis rezultata pod brojem num</li>
 * <li>"results" - ponovni ispis rezultata zadnjeg upita</li>
 * <li>"exit" - izlazak iz aplikacije</li>
 * </ul>
 * 
 * @author Ante Miličević
 *
 */
public class Shell {
	/**
	 * Naredbe uparene sa svojim nazivom
	 */
	public static Map<String, ShellCommand> commands = new HashMap<>();
	/**
	 * Dokumenti
	 */
	public static List<Document> documents = new LinkedList<>();
	/**
	 * Zadnji upit
	 */
	public static Query lastQuery;
	/**
	 * Grupni podaci dokumenata
	 */
	public static DocumentsData dData;
	static {
		commands.put("query", new QueryShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("type", new TypeShellCommand());
		commands.put("results", new ResultsShellCommand());
	}

	/**
	 * Ulazna točka programa
	 * 
	 * @param args koristi se samo prvi element polja koji se tumači kao staza do
	 *             direktorija s dokumentima
	 */
	public static void main(String[] args) {
		try {
			validateArgs(args);

			Path dirOfFiles = Paths.get(args[0]);
			dData = new DocumentsData(dirOfFiles,
					Paths.get(new Shell().getClass().getResource("/hrvatski_stoprijeci.txt").toURI()));

			initDocuments(dirOfFiles);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(1);
		}

		Scanner sc = new Scanner(System.in);
		ShellCommandStatus status = ShellCommandStatus.CONTINUE;
		PrintStream printer = System.out;
		
		printer.printf("Veličina vokabulara je: %d%n", dData.getVocabulary().size());

		while (status.equals(ShellCommandStatus.CONTINUE)) {
			try {
				printPrompt(printer);
				String line = sc.nextLine();
				ShellCommand command = parseCommand(line);
				if (command == null) {
					printer.println("Nepoznata naredba.");
					continue;
				}
				status = command.execute(parseArguments(line), printer);

			} catch (ShellException e) {
				printer.println(e.getMessage());
			}
		}

		sc.close();

	}

	/**
	 * Ispis prompta
	 * 
	 * @param printer objekt za ispis
	 */
	private static void printPrompt(PrintStream printer) {
		printer.print("Enter command > ");
	}

	/**
	 * Metoda za parsiranje argumenata
	 * 
	 * @param line tekst
	 * 
	 * @return argumenti bez naziva naredbe
	 */
	private static String parseArguments(String line) {
		line = line.trim();
		int spaceIndex = line.indexOf(" ");
		return line.substring(spaceIndex < 0 ? line.length() : spaceIndex);
	}

	/**
	 * Metoda za parsiranje naziva naredbe
	 * 
	 * @param line tekst
	 * @return naredba ili null ako takva ne postoji
	 */
	private static ShellCommand parseCommand(String line) {
		String name = line.trim().split(" ")[0];
		return commands.get(name);
	}

	/**
	 * Metoda za inicijalizaciju dokumenata
	 * 
	 * @param dirOfFiles staza do direktorija s dokumentima
	 * @throws IOException ako čitanje ne uspije
	 */
	private static void initDocuments(Path dirOfFiles) throws IOException {
		try (Stream<Path> fileStream = Files.list(dirOfFiles)) {
			Iterator<Path> it = fileStream.iterator();
			while (it.hasNext()) {
				Path path = it.next();
				if (!Files.isReadable(path)) {
					continue;
				}

				Document document = new Document(path);
				document.countWords();
				document.getWordCount().keySet().removeIf(word -> !dData.getVocabulary().contains(word));
				document.updateDocumentsData(dData);
				documents.add(document);
			}
		}
		dData.calculateIdfComponents();
		documents.forEach(doc -> doc.buildVector(dData));
	}

	/**
	 * Metoda za validaciju ulaznih argumenata
	 * 
	 * @param args ulazni argumenti
	 */
	private static void validateArgs(String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Only one argument is permitted");
		}
		if (!Files.isDirectory(Paths.get(args[0]), LinkOption.NOFOLLOW_LINKS)) {
			throw new RuntimeException("Argument must be path to directory");
		}
	}
}
