package hr.fer.zemris.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;
import hr.fer.zemris.hw06.shell.commands.namebuilder.NameBuilder;
import hr.fer.zemris.hw06.shell.commands.namebuilder.NameBuilderParser;

/**
 * Razred modelira naredbu za masovno preimenovanje/premještanja datoteka koje
 * se nalaze direktno unutar direktorija predanog u prvom argumentu.<br>
 * Oblik narebe je sljedeći: 'massrename DIR1 DIR2 CMD MASKA FORMAT'<br>
 * DIR1 - staza do izvornog direktorija<br>
 * DIR2 - staza do odredišnog direktorija<br>
 * MASKA - regex kod za prepoznavanje željenih datoteka za preimenovanje<br>
 * FORMAT - argument koji se korisni samo kod podnaredbi show i execute gdje
 * označava format preimenovanja<br>
 * CMD - naziv podnaredbe ( 'filter', 'group', 'show', 'execute' )<br>
 * Jedino 'execute' podnaredba utječe na odabrane datoteke.
 * 
 * @author Ante Miličević
 *
 */
public class MassrenameShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "massrename";

	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Collections.unmodifiableList(Arrays.asList("Takes 4 or 5 arguments depending on subcommand",
				"Format of command is 'massrename SRC_DIR DEST_DIR SUB_CMD MASK FORMAT'", "SRC_DIR is source directory",
				"DEST_DIR is destionation directory",
				"SUB_CMD can be one of the next subcommands 'filter','group,'show','execute'",
				"MASK defines regex based on which the files are selected",
				"FORMAT is argument based on which are files renamed (used in 'show' and 'execute'",
				"'filter' filters the files based on MASK",
				"'group' filters the files and generates groups defined in MASK",
				"'show' prints the new names of filtered files based od FORMAT",
				"'execute' renames the filtered folders in the way that 'show' printed"));
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> params = CommandUtil.tokenizeArguments(arguments);
			CommandUtil.checkTheNumberOfArguments(params, 4, 5, name);

			Path dir1 = CommandUtil.verifyDirectory(CommandUtil.returnNormalizedPath(params.get(0), env));
			Path dir2 = CommandUtil.verifyDirectory(CommandUtil.returnNormalizedPath(params.get(1), env));

			List<FilterResult> result = filter(dir1, params.get(3));
			if (params.size() == 4) {
				switch (params.get(2)) {
				case "filter":
					filterCMD(result, env);
					break;
				case "groups":
					groupsCMD(result, env);
					break;
				default:
					throw new IllegalStateException("Unknown command name '" + params.get(2) + "'");
				}
			} else {
				switch (params.get(2)) {
				case "show":
					showCMD(result, env, params.get(4));
					break;
				case "execute":
					executeCMD(result, env, params.get(4), dir1, dir2);
					break;
				default:
					throw new IllegalStateException("Unknown command name '" + params.get(2) + "'");
				}
			}
		} catch (ArgumentLexerException | IllegalStateException | IndexOutOfBoundsException
				| OperationFailedException | IllegalArgumentException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.writeln("IO Error => " + e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Metoda koja mijenja imena koja su rezultat filtriranja datoteka iz
	 * direktorija {@code dir1} te ih smješta u direktorij {@code dir2}.
	 * Preimenovanje se vrši temeljem teksta predanog u {@code renameString}-u.
	 * 
	 * @param result       rezultat filtriranja datoteka
	 * @param env          okolina u kojoj se izvodi naredba
	 * @param renameString tekst temeljem kojeg se provodi preimenovanje (gradi
	 *                     {@link NameBuilder}
	 * @param dir1         izvorišni direktorij
	 * @param dir2         odredišni direktorij
	 * 
	 * @throws IllegalArgumentException  ako je tekst generiranja imena
	 *                                   {@code string} krivo definiran
	 * @throws IndexOutOfBoundsException ako je tražena grupa nepostojeća
	 * 
	 * @throws OperationFailedException  ako preimenovanje ne uspije
	 */
	private void executeCMD(List<FilterResult> result, Environment env, String renameString, Path dir1, Path dir2) {
		Map<String, String> renameMap = mapNewNames(result, renameString);

		renameMap.forEach((oldName, newName) -> {
			try {
				Files.move(dir1.resolve(oldName), dir2.resolve(newName));
			} catch (IOException e) {
				throw new OperationFailedException("Can not rename file '" + oldName + "' to '" + newName + "'");
			}
			env.writeln(String.format("%s => %s", dir1.resolve(oldName), dir2.resolve(newName)));
		});
	}

	/**
	 * Metoda koja ispisuje trenutna imena te njihova generirana imena po pravilima
	 * navedenim u {@code renameString}
	 * 
	 * @param result       rezultat filtriranja
	 * @param env          okolina u kojoj se izvodi naredba
	 * @param renameString tekst temeljem kojeg se provodi preimenovanje (gradi
	 *                     {@link NameBuilder}
	 * 
	 * @throws IllegalArgumentException ako je tekst generiranja imena
	 *                                  {@code string} krivo definiran
	 * @throws IndexOutOfBounds         ako je tražena grupa nepostojeća
	 */
	private void showCMD(List<FilterResult> result, Environment env, String renameString) {
		Map<String, String> renameMap = mapNewNames(result, renameString);

		renameMap.forEach((oldName, newName) -> env.writeln(String.format("%s => %s", oldName, newName)));
	}

	/**
	 * Metoda koja mapira imena datoteka i njihova generirana imena po pravilima
	 * navedenim u {@code renameString}-u
	 * 
	 * @param result       rezultat filtriranja imena datoteka
	 * @param renameString tekst temeljem kojeg se provodi preimenovanje (gradi
	 *                     {@link NameBuilder}
	 * @return mapu imena i njihovih generiranih imena po pravilima iz
	 *         renameString-a
	 */
	private Map<String, String> mapNewNames(List<FilterResult> result, String renameString) {
		NameBuilderParser parser = new NameBuilderParser(renameString);
		NameBuilder builder = parser.getNameBuilder();

		return result.stream().collect(Collectors.toMap((filterResult) -> filterResult.toString(), (filterResult) -> {
			StringBuilder sb = new StringBuilder();
			builder.execute(filterResult, sb);

			return sb.toString();
		}));
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

	/**
	 * Metoda koja ispisuje filtrirane staze.
	 * 
	 * @param result filtrirani podaci
	 * @param env    okolina u kojoj se odvija naredba massrename
	 */
	private void filterCMD(List<FilterResult> result, Environment env) {
		result.forEach(filterResult -> env.writeln(filterResult.toString()));
	}

	/**
	 * Metoda koja ispisuje grupe unutar filtriranih rezultata.
	 * 
	 * @param result filtrirani podaci
	 * @param env    okolina u kojoj se odvija naredba massrename
	 */
	private void groupsCMD(List<FilterResult> result, Environment env) {
		result.forEach(filterResult -> {
			StringBuilder sb = new StringBuilder();

			sb.append(filterResult.toString());
			for (int i = 0, n = filterResult.numberOfGroups(); i <= n; i++) {
				sb.append(" " + i + ": ");
				sb.append(filterResult.group(i));
			}

			env.writeln(sb.toString());
		});
	}

	/**
	 * Metoda koja filtrira datoteke iz direktorija {@code dir} temeljem regex-a
	 * predanog u {@code pattern}-u. Rezultat svake uspješne operacije filtriranja
	 * zapisuje se u {@link FilterResult}.
	 * 
	 * @param dir     direktorij nad kojim se obavlja filtriranje
	 * @param pattern uzorak filtra
	 * @return lista uspješnih rezultata filtriranja
	 * 
	 * @throws IOException           ako se dogodi greška pri pristupanju
	 *                               direktoriju {@code dir}
	 * 
	 * @throws IllegalStateException ako se {@code pattern} ne može protumačiti kao
	 *                               regularni izraz
	 */
	private static List<FilterResult> filter(Path dir, String pattern) throws IOException {
		try (Stream<Path> stream = Files.walk(dir)) {
			Pattern regex = Pattern.compile("^" + pattern + "$", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Iterator<Path> it = stream.iterator();
			List<FilterResult> result = new LinkedList<>();

			while (it.hasNext()) {
				Path path = it.next();

				if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
					continue;

				Matcher matcher = regex.matcher(path.getFileName().toString());

				if (!matcher.find()) {
					continue;
				}

				result.add(new FilterResult(matcher));
			}

			return result;
		} catch (PatternSyntaxException e) {
			throw new IllegalStateException("Unknow pattern '" + pattern + "'");
		}
	}

	/**
	 * Pomoćni razred za zapisivanje rezultata filtriranja imena datoteka.
	 * 
	 * @author Ante Miličević
	 *
	 */
	public static class FilterResult {
		/**
		 * Informacije o filtriranju
		 */
		private Matcher matcher;

		/**
		 * Konstruktor koji inicijalizira informacije o filtriranju
		 * 
		 * @param matcher informacije o filtriranju
		 */
		private FilterResult(Matcher matcher) {
			super();
			this.matcher = matcher;
		}

		/**
		 * Broj grupa u filtru
		 * 
		 * @return broj grupa u filtru
		 */
		public int numberOfGroups() {
			return matcher.groupCount();
		}

		/**
		 * Metoda koja dohvaća {@code index}-tu grupu
		 * 
		 * @param index redni broj grupe
		 * @return dio teksta koji se podudara s definicijom {@code index}-te grupe
		 * 
		 * @throws IndexOutOfBoundsException ako je index izvan granica
		 */
		public String group(int index) {
			return matcher.group(index);
		}

		@Override
		public String toString() {
			return matcher.group(0);
		}
	}

}
