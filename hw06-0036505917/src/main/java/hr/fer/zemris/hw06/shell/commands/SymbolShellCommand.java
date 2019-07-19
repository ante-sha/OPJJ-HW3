package hr.fer.zemris.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import hr.fer.zemris.hw06.shell.ConfigurationConstants;
import hr.fer.zemris.hw06.shell.Environment;
import hr.fer.zemris.hw06.shell.ShellCommand;
import hr.fer.zemris.hw06.shell.ShellStatus;
import hr.fer.zemris.hw06.shell.commands.lexer.ArgumentLexerException;

/**
 * Razred koji modelira naredbu koja mijenja specijalne simbole u ljuski. Nazivi
 * i njihove defaultne vrijednosti se nalaze u razredu
 * {@link ConfigurationConstants}. <br>
 * Naredba može primiti jedan argument koji mora biti naziv simbola i tada ga
 * ispisuje, ako se u naredbi nalaze dva argumenta tada prvi treba biti naziv
 * simbola, a drugi znak na kojeg ga se postavlja.
 * 
 * @author Ante Miličević
 *
 */
public class SymbolShellCommand implements ShellCommand {

	/**
	 * Naziv naredbe
	 */
	private static final String name = "symbol";
	
	/**
	 * Opis naredbe
	 */
	private static final List<String> description;
	static {
		description = Arrays.asList("Customizes or returns shell symbols",
				String.format("Symbol names are: %s, %s, %s", ConfigurationConstants.prompt,
						ConfigurationConstants.morelines, ConfigurationConstants.multiline),
				"For customization first argument is symbol name and the second is replacement character",
				"For printing out the symbol argument is symbol name");
	}
	/**
	 * Pomoćna mapa za dohvat znaka iz njegovog naziva
	 */
	private static Map<String, Function<Environment, Character>> symbolNames;
	static {
		symbolNames = new HashMap<>();
		symbolNames.put(ConfigurationConstants.prompt, (env) -> env.getPromptSymbol());
		symbolNames.put(ConfigurationConstants.multiline, (env) -> env.getMultilineSymbol());
		symbolNames.put(ConfigurationConstants.morelines, (env) -> env.getMorelinesSymbol());
	}
	/**
	 * Pomoćna mapa za postavljanje znaka iz njegovog naziva
	 */
	private static Map<String, BiConsumer<Environment, Character>> changeSymbolName;
	static {
		changeSymbolName = new HashMap<>();
		changeSymbolName.put(ConfigurationConstants.prompt, (env, c) -> env.setPromptSymbol(c));
		changeSymbolName.put(ConfigurationConstants.multiline, (env, c) -> env.setMultilineSymbol(c));
		changeSymbolName.put(ConfigurationConstants.morelines, (env, c) -> env.setMorelinesSymbol(c));
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> params = CommandUtil.tokenizeArguments(arguments);
			CommandUtil.checkTheNumberOfArguments(params, 1, 2, name);

			if (params.size() == 1) {
				printSymbol(env, params.get(0));
			} else {
				changeSymbol(env, params);
			}

		} catch (IllegalArgumentException | ArgumentLexerException | OperationFailedException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Metoda koja mijenja simbol čiji je naziv naveden u prvom parametru argumenta
	 * u znak koji je naveden kao drugi parametar argumenta.
	 * 
	 * @param env    okolina u kojoj se izvršava naredba
	 * @param params parametri naredbe
	 * 
	 * @throws IllegalArgumentException ako je krivo navedeno ime simbola ili ako se
	 *                                  drugi parametar ne može protumačiti kao znak
	 */
	private void changeSymbol(Environment env, List<String> params) {
		BiConsumer<Environment, Character> changer = changeSymbolName.get(params.get(0));
		if (changer == null) {
			throw new IllegalArgumentException("Symbol name " + params.get(0) + " is not defined");
		}
		String newSymbol = params.get(1);
		if (newSymbol.length() != 1) {
			throw new IllegalArgumentException("Symbol must have length 1 => " + newSymbol);
		}
		Character symbol = newSymbol.charAt(0);
		Character old = symbolNames.get(params.get(0)).apply(env);

		changer.accept(env, symbol);

		env.writeln(String.format("Symbol for %s changed from '%c' to '%c'", params.get(0), old.charValue(),
				symbol.charValue()));
	}

	/**
	 * Metoda za ispis željenog simbola koji je definiran u argumentu.
	 * 
	 * @param env        okolina u kojoj se izvršava naredba
	 * @param symbolName naziv simbola
	 * 
	 * @throws IllegalArgumentException ako simbol naziva predanog u argumentu ne
	 *                                  postoji
	 */
	private void printSymbol(Environment env, String symbolName) {
		Function<Environment, Character> fun = symbolNames.get(symbolName);
		if (fun == null) {
			throw new IllegalArgumentException("Symbol name " + symbolName + " is not defined");
		}
		Character c = fun.apply(env);
		env.writeln(String.format("Symbol for %s is '%c'", symbolName, c.charValue()));
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

}
