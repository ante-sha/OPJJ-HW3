package hr.fer.zemris.hw06.shell.commands.lexer;

import static hr.fer.zemris.hw06.shell.commands.lexer.Token.TokenType.EOL;
import static hr.fer.zemris.hw06.shell.commands.lexer.Token.TokenType.STRING;
import static hr.fer.zemris.hw06.shell.commands.lexer.Token.TokenType.TEXT;

import java.util.Objects;

/**
 * Razred koji modelira lekser za leksiranje parametara naredbi
 * {@link ShellCommand}. Ako se unutar predanog stringa nalaze krivo definirani
 * podaci lekser baca iznimku {@link ArgumentLexerException}.
 * 
 * @author Ante Miličević
 *
 */
public class ArgumentLexer {
	/**
	 * Predani tekst spremljen u polje znakova
	 */
	private char[] dataArray;
	/**
	 * Trenutni index pri čitanju
	 */
	private int index;
	/**
	 * Zastavica koja označava da li je predan EOL token
	 */
	private boolean end;

	/**
	 * Konstruktor koji inicijalizira polje znakova konverzijom iz predanog teksta u
	 * argumentu.
	 * 
	 * @param data tekst kojeg se lekserira
	 * 
	 * @throws NullPointerException ako je data jednak null
	 */
	public ArgumentLexer(String data) {
		Objects.requireNonNull(data);
		dataArray = data.toCharArray();
	}

	/**
	 * Metoda koja generira sljedeći token i vraća ga kao povratnu vrijednost
	 * 
	 * @return sljedeći token
	 * 
	 * @throws ArgumentLexerException ako lekser naiđe na grešku definiranja
	 *                                argumenta ili ako se pozove metoda nextToken
	 *                                nakon predanog EOL tokena
	 */
	public Token nextToken() {
		if (end) {
			throw new ArgumentLexerException("EOL token already has been sent");
		}
		return generateToken();
	}

	/**
	 * Metoda koja generira token čitajući polje znakova od znaka
	 * {@code dataArray[index]}
	 * 
	 * @return novo izgenerirani token
	 * 
	 * @throws IllegalArgumentException ako argumenti nisu dobro definirani
	 */
	private Token generateToken() {
		skipSpaces();
		if (index >= dataArray.length) {
			end = true;
			return new Token(null, EOL);
		} else if (dataArray[index] == '"') {
			return readString();
		} else {
			return readText();
		}
	}

	/**
	 * Metoda koja interpretira sljedeći niz znakova kao tekst i onda ga vraća kao
	 * token kroz povratnu vrijednost.
	 * 
	 * @return token tipa TEXT
	 */
	private Token readText() {
		StringBuilder sb = new StringBuilder();

		for (int n = dataArray.length; index < n; index++) {
			if (Character.isWhitespace(dataArray[index])) {
				break;
			}
			sb.append(dataArray[index]);
		}

		return new Token(sb.toString(), TEXT);
	}

	/**
	 * Metoda koja interpretira sljedeći niz znakova kao tekst s navodnicima na
	 * početku i kraju. Dozvoljene escape sekvence su '\"' i '\\' ostale se
	 * zanemaruju i tretiraju kao dva obična znaka. U token se pohranjuju navodnici.
	 * 
	 * @return token tipa STRING
	 * 
	 * @throws ArgumentLexerException ako se na kraju stringa ne nalazi ne escape-an
	 *                                znak '"'
	 */
	private Token readString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dataArray[index++]);
		;
		boolean closed = false;
		for (int n = dataArray.length; index < n; index++) {
			if (dataArray[index] == '\\') {
				if (checkIfEscapeSequence()) {
					index++;
				}
			} else if (dataArray[index] == '"') {
				if (index + 1 < dataArray.length && !Character.isWhitespace(dataArray[index+1])) {
					throw new ArgumentLexerException("String literal can not be concatenated with other text");
				}
				sb.append(dataArray[index++]);
				closed = true;
				break;
			}
			sb.append(dataArray[index]);
		}
		if (!closed) {
			throw new ArgumentLexerException("String literal is not closed!");
		}
		return new Token(sb.toString(), STRING);
	}

	/**
	 * Metoda koja provjerava da li dolazi do escape-anja idućeg znaka
	 * 
	 * @return true ako dolazi, false inače
	 */
	private boolean checkIfEscapeSequence() {
		if (index + 1 >= dataArray.length) {
			return false;
		}
		if (dataArray[index + 1] == '"') {
			return true;
		} else if (dataArray[index + 1] == '\\') {
			return true;
		}
		return false;
	}

	/**
	 * Metoda koja preskače znakove koji predstavljaju praznine
	 */
	private void skipSpaces() {
		while (index < dataArray.length && Character.isWhitespace(dataArray[index])) {
			index++;
		}
	}
}
