package hr.fer.zemris.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu koja ispisuje datoteku u 3 stupca.<br>
 * 1. stupac označava broj dosad pročitanih bajtova u datoteci<br>
 * 2. stupac sadrži 16 2-znamenkastih heksadekadskih brojeva koji su na pola
 * odvojeni znakom '|'<br>
 * 3. stupac sadrži prikaz bajtova u standardnoj interpretaciji, bajtove koji su
 * izvan raspona [32,127] zamjenjuje se znakom '.'.
 * 
 * @author Ante Miličević
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * Ime naredbe
	 */
	private static final String name = "hexdump";

	private static final List<String> description;
	static {
		description = Arrays.asList("Produces hex-output of file, on the right side it is converted into text using "
				+ "UTF-8 encoding, non-standard characters or spaces are replaced with character '.'");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			run(env, arguments);
		} catch (IOException e) {
			env.writeln("File error => " + e.getMessage());
		} catch (ArgumentLexerException | IllegalArgumentException | OperationFailedException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Pomoćna privatna metoda koja pokreće i izvodi operaciju, dok se upravljanje
	 * iznimaka vrši u metodi {@link #executeCommand(Environment, String)}.
	 * 
	 * @param env       okolina ljuske
	 * @param arguments argumenti naredbe
	 * 
	 * @throws IOException              ako ne uspije čitanje datoteke
	 * @throws OperationFailedException ako staza nije validna ili je naveden krivi
	 *                                  broj argumenata
	 */
	private void run(Environment env, String arguments) throws IOException {
		List<Path> paths = CommandUtil.returnPaths(arguments);

		CommandUtil.checkTheNumberOfArguments(paths, 1, name);
		Path path = CommandUtil.verifyFile(paths.get(0));

		try (BufferedInputStream input = CommandUtil.createInputStream(path)) {
			Formatter formatter = new Formatter();
			byte[] buffer = new byte[1024];
			while (true) {
				int r = input.read(buffer);
				if (r <= 0)
					break;
				formatter.fillWithNewBytes(buffer, r);

				formatter.formattedOutput().forEach((line) -> env.writeln(line));
			}
			formatter.readAllRows().forEach((line) -> env.writeln(line));

		}
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

	/**
	 * Privatni razred za formatiranje ispisa naredbe hexdump. Defaultni broj
	 * bajtova po retku je 16.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class Formatter {
		/**
		 * Spremljeni ne pročitani znakovi
		 */
		private LinkedList<Byte> bytesToProcess = new LinkedList<>();
		/**
		 * Formatirani stringovi
		 */
		private List<String> output = new ArrayList<>();
		/**
		 * Trenutni redak
		 */
		private int row = 0;
		/**
		 * Broj bajtova za čitanje po retku
		 */
		private final int bytesPerRow = 16;
		/**
		 * Defaultna kodna stranica
		 */
		private static Charset cs = Charset.forName("UTF-8");

		/**
		 * Metoda koja ubacuje nove bajtove u listu bajtova za procesuiranje i zatim
		 * procesuira bajtove ako je moguće isformatirati barem jedan redak.
		 * 
		 * @param arr spremnik bajtova
		 * @param n   broj bajtova koji se predaju iz spremnika
		 */
		public void fillWithNewBytes(byte[] arr, int n) {

			for (int i = 0; i < n; i++) {
				bytesToProcess.add(arr[i]);
			}

			while (bytesToProcess.size() > bytesPerRow) {
				formatRow();
			}
		}

		/**
		 * Metoda koja procesuira sve preostale bajtove i kao povratnu vrijednost šalje
		 * formatirani izlaz. Lista izlaza se čisti nakon poziva metode.
		 * 
		 * @return formatirani izlaz
		 */
		public List<String> readAllRows() {
			while (bytesToProcess.size() != 0) {
				formatRow();
			}
			return formattedOutput();
		}

		/**
		 * Metoda koja vraća izbufferirani formatirani izlaz i zatim čisti listu
		 * spremnika.
		 * 
		 * @return formatirani izlaz
		 */
		public List<String> formattedOutput() {
			List<String> result = output;
			output = new ArrayList<>();
			return result;
		}

		/**
		 * Metoda koja formatira jedan redak
		 */
		private void formatRow() {
			Iterator<Byte> it = bytesToProcess.iterator();
			StringBuilder hexData = new StringBuilder();
			StringBuilder text = new StringBuilder();
			int i = 0;
			for (i = 0; i < bytesPerRow; i++) {
				if (it.hasNext()) {
					Byte elem = it.next();
					hexData.append(bytetohex(elem));
					text.append(formattedChar(elem));

					it.remove();
				} else {
					hexData.append("  ");
				}
				if ((i + 1) == bytesPerRow / 2) {
					hexData.append("|");
				} else {
					hexData.append(" ");
				}

			}
			String rowCount = formattedRow();
			row++;
			output.add(String.format("%s: %s| %s", rowCount, hexData.toString(), text.toString()));
		}

		/**
		 * Metoda za pretvaranje bajtova u heksadekadski zapis.
		 * 
		 * @param elem bajt za pretvorbu
		 * @return reprezentacija u heksadekadskoj bazi
		 */
		private static String bytetohex(Byte elem) {
			return new String(new char[] { Character.forDigit((int) (elem >>> 4 & 15), 16),
					Character.forDigit((int) (elem & 15), 16) });
		}

		/**
		 * Konverzija bajta u znak pomoću defaultne kodne stranice, vrijednosti koje
		 * nisu u intervalu [32,127] su zamjenjene znakom '.'.
		 * 
		 * @param elem bajt koji se konvertira
		 * @return znak u string formatu
		 */
		private static String formattedChar(Byte elem) {

			byte value = elem.byteValue();

			if (((int) (value) & 0xFFFF) < 32 || ((int) (value) & 0xFFFF) > 127) {
				value = ".".getBytes(cs)[0];
			}

			return new String(new byte[] { value }, cs);
		}

		/**
		 * Formatiranje ispisa za označavanje retka u heksadekadskom zapisu.
		 * 
		 * @return broj dosad pročitanih znakova
		 */
		private String formattedRow() {
			StringBuilder sb = new StringBuilder();
			int n = row * bytesPerRow;
			while (n != 0) {
				sb.append(Character.forDigit(n % 16, 16));
				n /= 16;
			}
			sb.append("00000000");
			sb.setLength(8);
			return sb.reverse().toString();
		}
	}

}
