package hr.fer.zemris.hw06.crypto;

import java.util.Objects;

/**
 * Pomoćni razred koji sadrži konverzije binarnih i heksadekadskih zapisa u oba
 * smjera.
 * 
 * @author Ante Miličević
 *
 */
public class Util {

	/**
	 * Metoda prima zapis heksadekadskog broja u stringu i vrača njegovu binarnu
	 * reprezentaciju u polju. Dozvoljena su velika i mala slova koja su simboli
	 * heksadekadskih brojeva.
	 * 
	 * @param hex string reprezentacija heksadekadskog broja
	 * @return binarna reprezentacija po bajtovima
	 * 
	 * @throws NullPointerException     ako je {@code hex} jednak null
	 * @throws IllegalArgumentException ako se predani string sadrži znakove koje
	 *                                  nisu znamenke u heksadekadskom sustavu
	 */
	public static byte[] hextobyte(String hex) {
		Objects.requireNonNull(hex);
		if (!hex.matches("[0-9|a-f|A-F]*")) {
			throw new IllegalArgumentException("Argument is not hex data");
		}
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}
		char[] digit = hex.toCharArray();
		byte[] result = new byte[digit.length / 2];

		for (int i = 0; i < digit.length; i = i + 2) {
			result[i / 2] = (byte) (Character.digit(digit[i], 16) * 16 + Character.digit(digit[i + 1], 16));
		}

		return result;
	}

	/**
	 * Metoda prima polje bajtova i vraća njihovu heksadekadsku reprezentaciju u
	 * stringu.
	 * 
	 * @param array polje bajtova
	 * @return heksadekadska reprezentacija u stringu
	 * 
	 * @throws NullPointerException ako je array null
	 */
	public static String bytetohex(byte[] array) {
		Objects.requireNonNull(array);
		char[] result = new char[array.length * 2];

		for (int i = 0, n = array.length; i < n; i++) {
			result[2 * i] = Character.forDigit((int) (array[i] >>> 4 & 15), 16);
			result[2 * i + 1] = Character.forDigit((int) (array[i] & 15), 16);
		}

		return new String(result);
	}
}
