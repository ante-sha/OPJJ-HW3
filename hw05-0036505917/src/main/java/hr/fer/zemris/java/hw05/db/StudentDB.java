package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import hr.fer.zemris.java.hw05.db.IFilter;
import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * Program koji demonstrira interakciju korisnika s bazom podataka. Ako se u
 * tekstualnom dokumentu baze podataka nalazi neka ne konzistencija program se
 * gasi sa statusom 1 i ispisom poruke na System.out. Ako korisnik upiše
 * nedopuštenu naredbu ili bilo kakav tekst u nedopuštenom formatu na ekran se
 * ispisuje poruka o grešci i program nastavlja s radom. Program se terminira
 * naredbom exit.
 * 
 * @author Ante Miličević
 *
 */
public class StudentDB {
	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 * @throws IOException ako se podaci iz baze ne mogu pročitati
	 */
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"), StandardCharsets.UTF_8);

		StudentDatabase database = null;
		try {
			database = new StudentDatabase(lines);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		Scanner sc = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("> ");
				String line = sc.nextLine().trim();
				if (line.equals("exit")) {
					break;
				}
				String arguments[] = line.split("[ |\t]+");
				if (!arguments[0].equals("query") ) {
					if(arguments[0].length() == 0) {
						throw new IllegalArgumentException("Command is missing");
					} else {
						throw new IllegalArgumentException("Command " + arguments[0] + " is not supported!");
					}
				}
				if(arguments.length <= 1) {
					throw new IllegalArgumentException("Command query must have arguments");
				}
				QueryParser parser = null;
				parser = new QueryParser(line.substring(arguments[0].length()));
				printResult(parser, database);

			} catch (IllegalArgumentException | IllegalStateException | ArrayIndexOutOfBoundsException e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Goodbye!");
		sc.close();
	}

	/**
	 * Ispis rezultata filtriranja
	 * 
	 * @param parser   parser naredbe
	 * @param database baza podataka koja sadrži sve zapise studenata
	 */
	private static void printResult(QueryParser parser, StudentDatabase database) {
		List<StudentRecord> listOfRecords = null;
		if (parser.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");
			listOfRecords = new ArrayList<StudentRecord>();
			listOfRecords.add(database.forJMBAG(parser.getQueriedJMBAG()));
		} else {
			IFilter filter = new QueryFilter(parser.getQuery());
			listOfRecords = database.filter(filter);
		}
		StudentRecordFormatter formater = new StudentRecordFormatter(listOfRecords);

		List<String> formattedLines = formater.formatRecords();

		formattedLines.forEach(System.out::println);
	}

	/**
	 * Razred koji formatira listu {@link StudentRecord} u format povoljan za ispis,
	 * na kraju ispisa se nalazi podatak o broju ispisanih zapisa.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class StudentRecordFormatter {
		/** Znak koji se nalazi na sjecištima u ispisnoj tablici */
		private static final char edgeChar = '+';
		/** Znak koji se nalazi na horizontali između 2 {@code edgeChar}-a */
		private static final char connectingChar = '=';
		/** Znak koji odvaja vrijednosti atributa zapisa studenta */
		private static final char delimiter = '|';
		/** Horizontalno poravnanje u čeliji */
		private static final int padding = 1;
		/** Veličina nadužeg imena studenta koji se nalazi u {@link #listOfRecords} */
		private int maxFirstName;
		/**
		 * Veličina nadužeg prezimena studenta koji se nalazi u {@link #listOfRecords}
		 */
		private int maxLastName;
		/** Lista studenata koju treba formatirati */
		private List<StudentRecord> listOfRecords;

		/**
		 * Konstruktor koji inicijalizira listu studenata za ispis
		 * 
		 * @param listOfRecords lista studenata za ispis
		 * 
		 * @throws NullPointerException ako je listOfRecords null
		 */
		public StudentRecordFormatter(List<StudentRecord> listOfRecords) {
			this.listOfRecords = Objects.requireNonNull(listOfRecords);
		}

		/**
		 * Metoda koja formatira listu zapisa studenata predanih u konstruktoru
		 * 
		 * @return lista formatiranih linija za ispis
		 */
		public List<String> formatRecords() {
			List<String> result = new ArrayList<String>();

			if (listOfRecords.size() == 0) {
				result.add(generateEndMessage());
				return result;
			}

			maxFirstName = 0;
			maxLastName = 0;
			for (StudentRecord record : listOfRecords) {
				if (record.getFirstName().length() > maxFirstName) {
					maxFirstName = record.getFirstName().length();
				}
				if (record.getLastName().length() > maxLastName) {
					maxLastName = record.getLastName().length();
				}
			}

			String header = generateHeader(maxFirstName, maxLastName);
			result.add(header);
			for (StudentRecord s : listOfRecords) {
				result.add(formatRecord(s));
			}
			result.add(header);
			result.add(generateEndMessage());
			return result;
		}

		/**
		 * Metoda koja generira završnu poruku u ispisu (broj ispisanih zapisa)
		 * 
		 * @return
		 */
		private String generateEndMessage() {
			return String.format("Records selected: %d%n", listOfRecords.size());
		}

		/**
		 * Metoda koja generira zaglavlje ispisa
		 * 
		 * @param maxFirstName maksimalna duljina imena u listi zapisa studenata
		 * @param maxLastName  maksimalna duljina prezimena u listi zapisa studenata
		 * @return zaglavlje
		 */
		private String generateHeader(int maxFirstName, int maxLastName) {
			StringBuilder line = new StringBuilder();
			line.append(edgeChar);
			line.append(generateStringWithNChars(connectingChar, 10 + 2 * padding));
			line.append(edgeChar);
			line.append(generateStringWithNChars(connectingChar, maxLastName + 2 * padding));
			line.append(edgeChar);
			line.append(generateStringWithNChars(connectingChar, maxFirstName + 2 * padding));
			line.append(edgeChar);
			line.append(generateStringWithNChars(connectingChar, 1 + 2 * padding));
			line.append(edgeChar);
			return line.toString();
		}

		/**
		 * Metoda koja formatira jedan zapis studenta
		 * 
		 * @param student zapis koji se formatira
		 * @return formatirani zapis
		 */
		private String formatRecord(StudentRecord student) {
			StringBuilder line = new StringBuilder();
			String whiteSpace = generateStringWithNChars(' ', padding);
			line.append(delimiter).append(whiteSpace);
			line.append(student.getJmbag());
			line.append(whiteSpace).append(delimiter).append(whiteSpace);
			line.append(student.getLastName());
			line.append(generateStringWithNChars(' ', maxLastName - student.getLastName().length()));
			line.append(whiteSpace).append(delimiter).append(whiteSpace);
			line.append(student.getFirstName());
			line.append(generateStringWithNChars(' ', maxFirstName - student.getFirstName().length()));
			line.append(whiteSpace).append(delimiter).append(whiteSpace);
			line.append(student.getFinalGrade());
			line.append(whiteSpace).append(delimiter);
			return line.toString();
		}

		/**
		 * Metoda koja generira string duljine {@code n} popunjen sa znakovima {@code c}
		 * @param c znak koji se replicira u string
		 * @param n broj replikacija
		 * @return znak c n puta repliciran i spojen i string
		 */
		private static String generateStringWithNChars(char c, int n) {
			char[] array = new char[n];
			Arrays.fill(array, c);
			return new String(array);
		}
	}
}
